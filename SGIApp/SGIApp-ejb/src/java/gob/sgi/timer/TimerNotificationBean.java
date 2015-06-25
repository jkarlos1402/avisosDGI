package gob.sgi.timer;

import gob.sgi.constante.Constante;
import gob.sgi.dto.Mail;
import gob.sgi.model.ConnectionManager;
import gob.sgi.model.Movbco;
import gob.sgi.model.Plazo;
import gob.sgi.model.Psolicitud;
import gob.sgi.model.Relsolbco;
import gob.sgi.model.Revisiontemporal;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.ScheduleExpression;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ejb.Timeout;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Singleton
@LocalBean
@Startup
public class TimerNotificationBean {

//    @Resource
//    TimerService timerService;
//
//    @Resource(mappedName = "jms/QueueConnectionFactory")
//    private QueueConnectionFactory connectionFactory;
//
//    @Resource(mappedName = "QueueMail")
//    private Queue mailQueue;
//
//    @PersistenceContext(unitName = "notificacionPU")
//    private EntityManager em;
//
//    @PostConstruct
//    private void init() {
//        Properties propiedades = new Properties();
//        InputStream stream = null;
//        try {
//            stream = new FileInputStream(Constante.FILE_CONF_PATH + "/constantes.properties");
//            propiedades.load(stream);
//
//            TimerConfig timerConfig = new TimerConfig();
//            timerConfig.setInfo("CalendarProgTimerDemo_Info");
//            ScheduleExpression schedule = new ScheduleExpression();
//            System.out.println("Horario de revision de solicitudes: year " + propiedades.getProperty("YEAR_REVISION") + " - month " + propiedades.getProperty("MONTH_REVISION") + " - dayofmonth " + propiedades.getProperty("DAY_OF_MONTH_REVISION") + " - DAY_OF_WEEK_REVISION " + propiedades.getProperty("DAY_OF_WEEK_REVISION") + " - HOUR_REVISION " + propiedades.getProperty("HOUR_REVISION") + " - MINUTE_REVISION " + propiedades.getProperty("MINUTE_REVISION") + " - SECOND_REVISION " + propiedades.getProperty("SECOND_REVISION"));
//            schedule.year(propiedades.getProperty("YEAR_REVISION")).month(propiedades.getProperty("MONTH_REVISION")).dayOfMonth(propiedades.getProperty("DAY_OF_MONTH_REVISION")).dayOfWeek(propiedades.getProperty("DAY_OF_WEEK_REVISION")).hour(propiedades.getProperty("HOUR_REVISION")).minute(propiedades.getProperty("MINUTE_REVISION")).second(propiedades.getProperty("SECOND_REVISION"));
//            timerService.createCalendarTimer(schedule, timerConfig);
//        } catch (FileNotFoundException ex) {
//            System.out.println("FileNotFoundException properties: " + ex.getMessage());
//        } catch (IOException ex) {
//            System.out.println("IOException properties: " + ex.getMessage());
//        } finally {
//            if (stream != null) {
//                try {
//                    stream.close();
//                } catch (IOException e) {
//                    System.out.println("IOException properties: " + e.getMessage());
//                }
//            }
//        }
//    }
//
//    @Timeout
//    public void checkPetitionsAndStudies() {
//        List<Psolicitud> petitions = null;
//        List<Relsolbco> studies = null;
//        List<Movbco> movStudies = null;
//        List<Revisiontemporal> revisiones = null;
//        List<Revisiontemporal> revisionesRemove = null;
//        Calendar fechaActual = null;
//        Date fechaAComparar = null;
//        Mail mail = null;
//        ConnectionManager cm = new ConnectionManager();
//        Connection cn = null;
//        PreparedStatement statement = null;
//        ResultSet rs = null;
//        Psolicitud psolicitud = null;
//        Query queryRevisionTemporal = null;
//        //aqui va el control de concurrencia
//        Revisiontemporal revisionTemporal = null;
//        queryRevisionTemporal = em.createQuery("SELECT r FROM Revisiontemporal r");        
//        revisiones = queryRevisionTemporal.getResultList();
//        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
//        revisionesRemove = new ArrayList<>();
//        for (Revisiontemporal revision : revisiones) {
//            if (!formatDate.format(revision.getFechaRevision()).equals(formatDate.format(Calendar.getInstance().getTime()))) {
//                revisionesRemove.add(revision);
//            }
//        }
//        revisiones.removeAll(revisionesRemove);
//        if (revisiones.isEmpty()) {
//            String[] diasVerificar = Constante.DIAS_TO_SEND_NOTIFICACION_SOL.split(",");
//            for (int i = 0; i < diasVerificar.length; i++) {
//                //se verifica el periodo para reenviar una solicitud despues de ser revisada por el area
//                fechaActual = Calendar.getInstance();
//                fechaActual.add(Calendar.DAY_OF_YEAR, ((Constante.VIGENCIA_SOL_OBS * -1) + new Integer(diasVerificar[i])));
//                fechaAComparar = fechaActual.getTime();
//                cn = cm.conectar();
//                petitions = new ArrayList<>();
//                try {
//                    statement = cn.prepareStatement("SELECT IdSol,IdUsu FROM psolicitud WHERE IdEdoSol = ? AND IdSolPre IS NOT NULL AND FecEval = ?");
//                    statement.setInt(1, Integer.parseInt(Constante.ESTATUS_SOL_REVISADA));
//                    statement.setDate(2, new java.sql.Date(fechaAComparar.getTime()));
//                    rs = statement.executeQuery();
//                    while (rs.next()) {
//                        psolicitud = new Psolicitud();
//                        psolicitud.setIdSol(rs.getInt("IdSol"));
//                        psolicitud.setIdUsu(rs.getShort("IdUsu"));
//                        petitions.add(psolicitud);
//                    }
//                } catch (SQLException ex) {
//                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                } finally {
//                    if (rs != null) {
//                        try {
//                            rs.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                        }
//                    }
//                    if (statement != null) {
//                        try {
//                            statement.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                        }
//                    }
//                    if (cn != null) {
//                        cm.desconectar(cn);
//                    }
//                }
////                System.out.println("fechaGenerada para envio de alerta de solicitud: " + fechaAComparar);
//                try (QueueConnection connection = connectionFactory.createQueueConnection()) {
//                    QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//                    QueueSender queueSender = queueSession.createSender(null);
//                    for (Psolicitud petition : petitions) {
//                        mail = new Mail();
//                        mail.setIdSolicitud(petition.getIdSol() + "");
//                        mail.setIdUsuario(petition.getIdUsu() + "");
//                        mail.setIdRolUsu(Constante.ROL_SISTEMA);
//                        mail.setSubject("Aviso de la DGI");
//                        mail.setDiasParaNoticifacion(new Integer(diasVerificar[i]));
//                        //se crea el mensaje
//                        ObjectMessage message = queueSession.createObjectMessage(mail);
//                        //se envia el mensaje
//                        queueSender.send(mailQueue, message);
//
//                    }
//                    //se cierra la conexion
//                    connection.close();
//                } catch (JMSException ex) {
//                    System.out.println("JMSException al validar periodo solicitud: " + ex.getMessage());
//                }
//
//                //se cancelan solicitudes que no fueron atendidas despues del periodo permitido
//                fechaActual = Calendar.getInstance();
//                fechaActual.add(Calendar.DAY_OF_YEAR, ((Constante.VIGENCIA_SOL_OBS + 1) * -1));
//                fechaAComparar = fechaActual.getTime();
////                System.out.println("fechaGenerada para cancelacion de solicitud: " + fechaAComparar);
//                cn = cm.conectar();
//                petitions = new ArrayList<>();
//                try {
//                    statement = cn.prepareStatement("SELECT IdSol,IdUsu FROM psolicitud WHERE IdEdoSol = ? AND IdSolPre IS NOT NULL AND FecEval = ?");
//                    statement.setInt(1, Integer.parseInt(Constante.ESTATUS_SOL_REVISADA));
//                    statement.setDate(2, new java.sql.Date(fechaAComparar.getTime()));
//                    rs = statement.executeQuery();
//                    while (rs.next()) {
//                        psolicitud = new Psolicitud();
//                        psolicitud.setIdSol(rs.getInt("IdSol"));
//                        psolicitud.setIdUsu(rs.getShort("IdUsu"));
//                        petitions.add(psolicitud);
//                    }
//                } catch (SQLException ex) {
//                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                } finally {
//                    if (rs != null) {
//                        try {
//                            rs.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                        }
//                    }
//                    if (statement != null) {
//                        try {
//                            statement.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                        }
//                    }
//                    if (cn != null) {
//                        cm.desconectar(cn);
//                    }
//                }
//                mail = null;
//                try (QueueConnection connection = connectionFactory.createQueueConnection()) {
//                    QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//                    QueueSender queueSender = queueSession.createSender(null);
//                    for (Psolicitud petition : petitions) {
//                        mail = new Mail();
//                        mail.setIdSolicitud(petition.getIdSol() + "");
//                        mail.setIdUsuario(petition.getIdUsu() + "");
//                        mail.setIdRolUsu(Constante.ROL_SISTEMA);
//                        mail.setEstatusSolicitud(Constante.ESTATUS_SOL_CANCELADA);
//                        mail.setSubject("Aviso de la DGI");
//                        cn = cm.conectar();
//                        int res;
//                        try {
//                            statement = cn.prepareStatement("UPDATE psolicitud SET IdEdoSol = ? WHERE IdSol = ?");
//                            statement.setInt(1, Integer.parseInt(Constante.ESTATUS_SOL_CANCELADA));
//                            statement.setInt(2, petition.getIdSol());
//                            res = statement.executeUpdate();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                        } finally {
//                            if (statement != null) {
//                                try {
//                                    statement.close();
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                }
//                            }
//                            if (cn != null) {
//                                cm.desconectar(cn);
//                            }
//                        }
//                        //se crea el mensaje
//                        ObjectMessage message = queueSession.createObjectMessage(mail);
//                        //se envia el mensaje
//                        queueSender.send(mailQueue, message);
//
//                    }
//                    //se cierra la conexion
//                    connection.close();
//                } catch (JMSException ex) {
//                    System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
//                }
//            }
//            //
//            //
//            //se verifican estudios inferiores a 50 mdp
//            //
//            //
//            diasVerificar = Constante.DIAS_TO_SEND_NOTIFICACION_ES_1.split(",");
//            Relsolbco relsolbco = null;
//            for (int i = 0; i < diasVerificar.length; i++) {
////                fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_OBS_1 - new Integer(diasVerificar[i]));
////                System.out.println("fechaGenerada para para aviso de ES inferiores a 50 mdp: " + fechaAComparar);
////                fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_OBS_1);
////                System.out.println("fecha a comparar para cancelar ES inferiores a 50 mdp: " + fechaAComparar);
//                cn = cm.conectar();
//                studies = new ArrayList<>();
//                try {
//                    statement = cn.prepareStatement("SELECT IdBco,IdSol FROM relsolbco JOIN psolicitud using (IdSol) WHERE Status = ?  AND Monto < 50000000.00");
//                    statement.setInt(1, Integer.parseInt(Constante.ESTATUS_ES_OBSERVACIONES));
//                    rs = statement.executeQuery();
//                    while (rs.next()) {
//                        relsolbco = new Relsolbco();
//                        relsolbco.setIdBco(rs.getInt("IdBco"));
//                        relsolbco.setIdSol(rs.getInt("IdSol"));
//                        studies.add(relsolbco);
//                    }
//                } catch (SQLException ex) {
//                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                } finally {
//                    if (rs != null) {
//                        try {
//                            rs.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                        }
//                    }
//                    if (statement != null) {
//                        try {
//                            statement.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                        }
//                    }
//                    if (cn != null) {
//                        cm.desconectar(cn);
//                    }
//                }
//                Movbco movbcoTemp = null;
//                for (Relsolbco study : studies) {
//                    movStudies = new ArrayList<>();
//                    fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_OBS_1 - new Integer(diasVerificar[i]));
//                    cn = cm.conectar();
//                    try {
//                        statement = cn.prepareStatement("SELECT IdBco,tipMov,fecMov FROM movbco WHERE IdBco = ? AND tipMov = ? ORDER BY fecMov DESC limit 1");
//                        statement.setInt(1, study.getIdBco());
//                        statement.setInt(2, Integer.parseInt(Constante.ESTATUS_ES_OBSERVACIONES));
//                        rs = statement.executeQuery();
//                        while (rs.next()) {
//                            movbcoTemp = new Movbco();
//                            movbcoTemp.setFecMov(rs.getDate("fecMov"));
//                            movbcoTemp.setIdBco(rs.getInt("IdBco"));
//                            movbcoTemp.setTipMov(rs.getString("tipMov"));
//                            movStudies.add(movbcoTemp);
//                        }
//                    } catch (SQLException ex) {
//                        System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                    } finally {
//                        if (rs != null) {
//                            try {
//                                rs.close();
//                            } catch (SQLException ex) {
//                                System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                            }
//                        }
//                        if (statement != null) {
//                            try {
//                                statement.close();
//                            } catch (SQLException ex) {
//                                System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                            }
//                        }
//                        if (cn != null) {
//                            cm.desconectar(cn);
//                        }
//                    }
//                    if (movStudies.size() > 0) {
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                        if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
////                        System.out.println("estudio encontrado: " + movStudies.get(0).getIdmovbco());
//                            mail = null;
//                            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
//                                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//                                QueueSender queueSender = queueSession.createSender(null);
//                                mail = new Mail();
//                                mail.setIdBco(study.getIdBco() + "");
//                                cn = cm.conectar();
//                                try {
//                                    statement = cn.prepareStatement("SELECT IdUsu FROM psolicitud WHERE IdSol = ?");
//                                    statement.setInt(1, study.getIdSol());
//                                    rs = statement.executeQuery();
//                                    while (rs.next()) {
//                                        mail.setIdUsuario(rs.getInt("IdUsu") + "");
//                                    }
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                                } finally {
//                                    if (rs != null) {
//                                        try {
//                                            rs.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (statement != null) {
//                                        try {
//                                            statement.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (cn != null) {
//                                        cm.desconectar(cn);
//                                    }
//                                }
//                                mail.setIdRolUsu(Constante.ROL_SISTEMA);
//                                mail.setSubject("Aviso de la DGI");
//                                mail.setDiasParaNoticifacion(new Integer(diasVerificar[i]));
//                                //se crea el mensaje
//                                ObjectMessage message = queueSession.createObjectMessage(mail);
//                                //se envia el mensaje
//                                queueSender.send(mailQueue, message);
//
//                                //se cierra la conexion
//                                connection.close();
//                            } catch (JMSException ex) {
//                                System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
//                            }
//                        }
//                        //comparar para cancelar estudio socioeconomico//                        
//                        fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_OBS_1);
//                        if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
////                        System.out.println("estudio a cancelar encontrado: " + movStudies.get(0).getIdmovbco());
//                            mail = null;
//                            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
//                                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//                                QueueSender queueSender = queueSession.createSender(null);
//                                mail = new Mail();
//                                mail.setIdBco(study.getIdBco() + "");
//                                cn = cm.conectar();
//                                try {
//                                    statement = cn.prepareStatement("SELECT IdUsu FROM psolicitud WHERE IdSol = ?");
//                                    statement.setInt(1, study.getIdSol());
//                                    rs = statement.executeQuery();
//                                    while (rs.next()) {
//                                        mail.setIdUsuario(rs.getInt("IdUsu") + "");
//                                    }
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                                } finally {
//                                    if (rs != null) {
//                                        try {
//                                            rs.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (statement != null) {
//                                        try {
//                                            statement.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (cn != null) {
//                                        cm.desconectar(cn);
//                                    }
//                                }
//                                mail.setIdRolUsu(Constante.ROL_SISTEMA);
//                                mail.setEstatusBco(Constante.ESTATUS_ES_CANCELADO);
//                                mail.setSubject("Aviso de la DGI");
//                                cn = cm.conectar();
//                                int res;
//                                try {
//                                    statement = cn.prepareStatement("UPDATE relsolbco SET Status = ? WHERE IdBco = ?");
//                                    statement.setInt(1, Integer.parseInt(Constante.ESTATUS_ES_CANCELADO));
//                                    statement.setInt(2, study.getIdBco());
//                                    res = statement.executeUpdate();
//                                    statement = cn.prepareStatement("INSERT INTO movbco(IdBco,fecMov,tipMov,obs,status) values (?,?,?,?,?)");
//                                    statement.setInt(1, study.getIdBco());
//                                    statement.setDate(2, new java.sql.Date(new Date().getTime()));
//                                    statement.setString(3, Constante.ESTATUS_ES_CANCELADO);
//                                    statement.setString(4, "Cancelado autom\u00e1ticamente por inactividad");
//                                    statement.setString(5, Constante.ESTATUS_ES_BLOQUEADO);
//                                    res = statement.executeUpdate();
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                                } finally {
//                                    if (statement != null) {
//                                        try {
//                                            statement.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (cn != null) {
//                                        cm.desconectar(cn);
//                                    }
//                                }
//                                //se crea el mensaje
//                                ObjectMessage message = queueSession.createObjectMessage(mail);
//                                //se envia el mensaje
//                                queueSender.send(mailQueue, message);
//                                //se cierra la conexion
//                                connection.close();
//                            } catch (JMSException ex) {
//                                System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
//                            }
//                        }
//                    }
//                }
//            }
//            //
//            //
//            //se verifican estudios de 50 mdp a 500 mdp
//            //
//            //
//            diasVerificar = Constante.DIAS_TO_SEND_NOTIFICACION_ES_2.split(",");
//            relsolbco = null;
//            for (int i = 0; i < diasVerificar.length; i++) {
////                fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_OBS_2 - new Integer(diasVerificar[i]));
////                System.out.println("fechaGenerada para para aviso de ES de 50 mdp a 500 mdp: " + fechaAComparar);
////                fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_OBS_2);
////                System.out.println("fecha a comparar para cancelar ES de 50 mdp a 500 mdp: " + fechaAComparar);            
//                cn = cm.conectar();
//                studies = new ArrayList<>();
//                try {
//                    statement = cn.prepareStatement("SELECT IdBco,IdSol FROM relsolbco JOIN psolicitud using (IdSol) WHERE Status = ?  AND Monto >= 50000000.00 AND Monto <= 500000000.00");
//                    statement.setInt(1, Integer.parseInt(Constante.ESTATUS_ES_OBSERVACIONES));
//                    rs = statement.executeQuery();
//                    while (rs.next()) {
//                        relsolbco = new Relsolbco();
//                        relsolbco.setIdBco(rs.getInt("IdBco"));
//                        relsolbco.setIdSol(rs.getInt("IdSol"));
//                        studies.add(relsolbco);
//                    }
//                } catch (SQLException ex) {
//                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                } finally {
//                    if (rs != null) {
//                        try {
//                            rs.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                        }
//                    }
//                    if (statement != null) {
//                        try {
//                            statement.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                        }
//                    }
//                    if (cn != null) {
//                        cm.desconectar(cn);
//                    }
//                }
//                Movbco movbcoTemp = null;
//                for (Relsolbco study : studies) {
//                    movStudies = new ArrayList<>();
//                    fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_OBS_2 - new Integer(diasVerificar[i]));
//                    cn = cm.conectar();
//                    try {
//                        statement = cn.prepareStatement("SELECT IdBco,tipMov,fecMov FROM movbco WHERE IdBco = ? AND tipMov = ? ORDER BY fecMov DESC limit 1");
//                        statement.setInt(1, study.getIdBco());
//                        statement.setInt(2, Integer.parseInt(Constante.ESTATUS_ES_OBSERVACIONES));
//                        rs = statement.executeQuery();
//                        while (rs.next()) {
//                            movbcoTemp = new Movbco();
//                            movbcoTemp.setFecMov(rs.getDate("fecMov"));
//                            movbcoTemp.setIdBco(rs.getInt("IdBco"));
//                            movbcoTemp.setTipMov(rs.getString("tipMov"));
//                            movStudies.add(movbcoTemp);
//                        }
//                    } catch (SQLException ex) {
//                        System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                    } finally {
//                        if (rs != null) {
//                            try {
//                                rs.close();
//                            } catch (SQLException ex) {
//                                System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                            }
//                        }
//                        if (statement != null) {
//                            try {
//                                statement.close();
//                            } catch (SQLException ex) {
//                                System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                            }
//                        }
//                        if (cn != null) {
//                            cm.desconectar(cn);
//                        }
//                    }
//                    if (movStudies.size() > 0) {
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                        if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
////                        System.out.println("estudio encontrado: " + movStudies.get(0).getIdmovbco());
//                            mail = null;
//                            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
//                                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//                                QueueSender queueSender = queueSession.createSender(null);
//                                mail = new Mail();
//                                mail.setIdBco(study.getIdBco() + "");
//                                cn = cm.conectar();
//                                try {
//                                    statement = cn.prepareStatement("SELECT IdUsu FROM psolicitud WHERE IdSol = ?");
//                                    statement.setInt(1, study.getIdSol());
//                                    rs = statement.executeQuery();
//                                    while (rs.next()) {
//                                        mail.setIdUsuario(rs.getInt("IdUsu") + "");
//                                    }
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                                } finally {
//                                    if (rs != null) {
//                                        try {
//                                            rs.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (statement != null) {
//                                        try {
//                                            statement.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (cn != null) {
//                                        cm.desconectar(cn);
//                                    }
//                                }
//                                mail.setIdRolUsu(Constante.ROL_SISTEMA);
//                                mail.setSubject("Aviso de la DGI");
//                                mail.setDiasParaNoticifacion(new Integer(diasVerificar[i]));
//                                //se crea el mensaje
//                                ObjectMessage message = queueSession.createObjectMessage(mail);
//                                //se envia el mensaje
//                                queueSender.send(mailQueue, message);
//
//                                //se cierra la conexion
//                                connection.close();
//                            } catch (JMSException ex) {
//                                System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
//                            }
//                        }
//                        //comparar para cancelar estudio socioeconomico                        
//                        fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_OBS_2);
//                        if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
////                        System.out.println("estudio a cancelar encontrado: " + movStudies.get(0).getIdmovbco());
//                            mail = null;
//                            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
//                                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//                                QueueSender queueSender = queueSession.createSender(null);
//                                mail = new Mail();
//                                mail.setIdBco(study.getIdBco() + "");
//                                cn = cm.conectar();
//                                try {
//                                    statement = cn.prepareStatement("SELECT IdUsu FROM psolicitud WHERE IdSol = ?");
//                                    statement.setInt(1, study.getIdSol());
//                                    rs = statement.executeQuery();
//                                    while (rs.next()) {
//                                        mail.setIdUsuario(rs.getInt("IdUsu") + "");
//                                    }
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                                } finally {
//                                    if (rs != null) {
//                                        try {
//                                            rs.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (statement != null) {
//                                        try {
//                                            statement.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (cn != null) {
//                                        cm.desconectar(cn);
//                                    }
//                                }
//                                mail.setIdRolUsu(Constante.ROL_SISTEMA);
//                                mail.setEstatusBco(Constante.ESTATUS_ES_CANCELADO);
//                                mail.setSubject("Aviso de la DGI");
//                                cn = cm.conectar();
//                                int res;
//                                try {
//                                    statement = cn.prepareStatement("UPDATE relsolbco SET Status = ? WHERE IdBco = ?");
//                                    statement.setInt(1, Integer.parseInt(Constante.ESTATUS_ES_CANCELADO));
//                                    statement.setInt(2, study.getIdBco());
//                                    res = statement.executeUpdate();
//                                    statement = cn.prepareStatement("INSERT INTO movbco(IdBco,fecMov,tipMov,obs,status) values (?,?,?,?,?)");
//                                    statement.setInt(1, study.getIdBco());
//                                    statement.setDate(2, new java.sql.Date(new Date().getTime()));
//                                    statement.setString(3, Constante.ESTATUS_ES_CANCELADO);
//                                    statement.setString(4, "Cancelado autom\u00e1ticamente por inactividad");
//                                    statement.setString(5, Constante.ESTATUS_ES_BLOQUEADO);
//                                    res = statement.executeUpdate();
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                                } finally {
//                                    if (statement != null) {
//                                        try {
//                                            statement.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (cn != null) {
//                                        cm.desconectar(cn);
//                                    }
//                                }
//                                //se crea el mensaje
//                                ObjectMessage message = queueSession.createObjectMessage(mail);
//                                //se envia el mensaje
//                                queueSender.send(mailQueue, message);
//                                //se cierra la conexion
//                                connection.close();
//                            } catch (JMSException ex) {
//                                System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
//                            }
//                        }
//                    }
//                }
//            }
//
//            //
//            //
//            //se verifican estudios superiores a 500 mdp
//            //
//            //
//            diasVerificar = Constante.DIAS_TO_SEND_NOTIFICACION_ES_3.split(",");
//            relsolbco = null;
//            for (int i = 0; i < diasVerificar.length; i++) {
////                fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_OBS_3 - new Integer(diasVerificar[i]));
////                System.out.println("fechaGenerada para para aviso de ES superiores a 500 mdp: " + fechaAComparar);
////                fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_OBS_3);
////                System.out.println("fecha a comparar para cancelar ES superiores a 500 mdp: " + fechaAComparar);
//                cn = cm.conectar();
//                studies = new ArrayList<>();
//                try {
//                    statement = cn.prepareStatement("SELECT IdBco,IdSol FROM relsolbco JOIN psolicitud using (IdSol) WHERE Status = ?  AND Monto > 500000000.00");
//                    statement.setInt(1, Integer.parseInt(Constante.ESTATUS_ES_OBSERVACIONES));
//                    rs = statement.executeQuery();
//                    while (rs.next()) {
//                        relsolbco = new Relsolbco();
//                        relsolbco.setIdBco(rs.getInt("IdBco"));
//                        relsolbco.setIdSol(rs.getInt("IdSol"));
//                        studies.add(relsolbco);
//                    }
//                } catch (SQLException ex) {
//                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                } finally {
//                    if (rs != null) {
//                        try {
//                            rs.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                        }
//                    }
//                    if (statement != null) {
//                        try {
//                            statement.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                        }
//                    }
//                    if (cn != null) {
//                        cm.desconectar(cn);
//                    }
//                }
//                Movbco movbcoTemp = null;
//                for (Relsolbco study : studies) {
//                    movStudies = new ArrayList<>();
//                    fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_OBS_3 - new Integer(diasVerificar[i]));
//                    cn = cm.conectar();
//                    try {
//                        statement = cn.prepareStatement("SELECT IdBco,tipMov,fecMov FROM movbco WHERE IdBco = ? AND tipMov = ? ORDER BY fecMov DESC limit 1");
//                        statement.setInt(1, study.getIdBco());
//                        statement.setInt(2, Integer.parseInt(Constante.ESTATUS_ES_OBSERVACIONES));
//                        rs = statement.executeQuery();
//                        while (rs.next()) {
//                            movbcoTemp = new Movbco();
//                            movbcoTemp.setFecMov(rs.getDate("fecMov"));
//                            movbcoTemp.setIdBco(rs.getInt("IdBco"));
//                            movbcoTemp.setTipMov(rs.getString("tipMov"));
//                            movStudies.add(movbcoTemp);
//                        }
//                    } catch (SQLException ex) {
//                        System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                    } finally {
//                        if (rs != null) {
//                            try {
//                                rs.close();
//                            } catch (SQLException ex) {
//                                System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                            }
//                        }
//                        if (statement != null) {
//                            try {
//                                statement.close();
//                            } catch (SQLException ex) {
//                                System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                            }
//                        }
//                        if (cn != null) {
//                            cm.desconectar(cn);
//                        }
//                    }
//                    if (movStudies.size() > 0) {
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                        if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
////                        System.out.println("estudio encontrado: " + movStudies.get(0).getIdmovbco());
//                            mail = null;
//                            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
//                                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//                                QueueSender queueSender = queueSession.createSender(null);
//                                mail = new Mail();
//                                mail.setIdBco(study.getIdBco() + "");
//                                cn = cm.conectar();
//                                try {
//                                    statement = cn.prepareStatement("SELECT IdUsu FROM psolicitud WHERE IdSol = ?");
//                                    statement.setInt(1, study.getIdSol());
//                                    rs = statement.executeQuery();
//                                    while (rs.next()) {
//                                        mail.setIdUsuario(rs.getInt("IdUsu") + "");
//                                    }
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                                } finally {
//                                    if (rs != null) {
//                                        try {
//                                            rs.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (statement != null) {
//                                        try {
//                                            statement.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (cn != null) {
//                                        cm.desconectar(cn);
//                                    }
//                                }
//                                mail.setIdRolUsu(Constante.ROL_SISTEMA);
//                                mail.setSubject("Aviso de la DGI");
//                                mail.setDiasParaNoticifacion(new Integer(diasVerificar[i]));
//                                //se crea el mensaje
//                                ObjectMessage message = queueSession.createObjectMessage(mail);
//                                //se envia el mensaje
//                                queueSender.send(mailQueue, message);
//
//                                //se cierra la conexion
//                                connection.close();
//                            } catch (JMSException ex) {
//                                System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
//                            }
//                        }
//                        //comparar para cancelar estudio socioeconomico
//                        fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_OBS_3);
//                        if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
////                        System.out.println("estudio a cancelar encontrado: " + movStudies.get(0).getIdmovbco());
//                            mail = null;
//                            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
//                                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//                                QueueSender queueSender = queueSession.createSender(null);
//                                mail = new Mail();
//                                mail.setIdBco(study.getIdBco() + "");
//                                cn = cm.conectar();
//                                try {
//                                    statement = cn.prepareStatement("SELECT IdUsu FROM psolicitud WHERE IdSol = ?");
//                                    statement.setInt(1, study.getIdSol());
//                                    rs = statement.executeQuery();
//                                    while (rs.next()) {
//                                        mail.setIdUsuario(rs.getInt("IdUsu") + "");
//                                    }
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                                } finally {
//                                    if (rs != null) {
//                                        try {
//                                            rs.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (statement != null) {
//                                        try {
//                                            statement.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (cn != null) {
//                                        cm.desconectar(cn);
//                                    }
//                                }
//                                mail.setIdRolUsu(Constante.ROL_SISTEMA);
//                                mail.setEstatusBco(Constante.ESTATUS_ES_CANCELADO);
//                                mail.setSubject("Aviso de la DGI");
//                                cn = cm.conectar();
//                                int res;
//                                try {
//                                    statement = cn.prepareStatement("UPDATE relsolbco SET Status = ? WHERE IdBco = ?");
//                                    statement.setInt(1, Integer.parseInt(Constante.ESTATUS_ES_CANCELADO));
//                                    statement.setInt(2, study.getIdBco());
//                                    res = statement.executeUpdate();
//                                    statement = cn.prepareStatement("INSERT INTO movbco(IdBco,fecMov,tipMov,obs,status) values (?,?,?,?,?)");
//                                    statement.setInt(1, study.getIdBco());
//                                    statement.setDate(2, new java.sql.Date(new Date().getTime()));
//                                    statement.setString(3, Constante.ESTATUS_ES_CANCELADO);
//                                    statement.setString(4, "Cancelado autom\u00e1ticamente por inactividad");
//                                    statement.setString(5, Constante.ESTATUS_ES_BLOQUEADO);
//                                    res = statement.executeUpdate();
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                                } finally {
//                                    if (statement != null) {
//                                        try {
//                                            statement.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (cn != null) {
//                                        cm.desconectar(cn);
//                                    }
//                                }
//                                //se crea el mensaje
//                                ObjectMessage message = queueSession.createObjectMessage(mail);
//                                //se envia el mensaje
//                                queueSender.send(mailQueue, message);
//                                //se cierra la conexion
//                                connection.close();
//                            } catch (JMSException ex) {
//                                System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
//                            }
//                        }
//                    }
//                }
//            }
//            //
//            //
//            //se verifican estudios a ser DICTAMINADOS inferiores a 50 mdp
//            //
//            //
//            diasVerificar = Constante.DIAS_TO_SEND_NOTIFICACION_DICT_ES_1.split(",");
//            for (int i = 0; i < diasVerificar.length; i++) {
////                fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_DICTAMINACION_1 - new Integer(diasVerificar[i]));
////                System.out.println("fecha a comparar para dic inferior a 50 mdp: " + fechaAComparar);
////                fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_DICTAMINACION_1);
////                System.out.println("fecha a comparar para cancelar de dictaminacion inferior a 50 mdp: " + fechaAComparar);
//                cn = cm.conectar();
//                studies = new ArrayList<>();
//                try {
//                    statement = cn.prepareStatement("SELECT IdBco,IdSol FROM relsolbco JOIN psolicitud using (IdSol) WHERE (Status = ? OR Status = ?) AND Monto < 50000000.00");
//                    statement.setInt(1, Integer.parseInt(Constante.ESTATUS_ES_INGRESADO));
//                    statement.setInt(2, Integer.parseInt(Constante.ESTATUS_ES_REVISADO));
//                    rs = statement.executeQuery();
//                    while (rs.next()) {
//                        relsolbco = new Relsolbco();
//                        relsolbco.setIdBco(rs.getInt("IdBco"));
//                        relsolbco.setIdSol(rs.getInt("IdSol"));
//                        studies.add(relsolbco);
//                    }
//                } catch (SQLException ex) {
//                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                } finally {
//                    if (rs != null) {
//                        try {
//                            rs.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                        }
//                    }
//                    if (statement != null) {
//                        try {
//                            statement.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                        }
//                    }
//                    if (cn != null) {
//                        cm.desconectar(cn);
//                    }
//                }
//                for (Relsolbco study : studies) {
//                    movStudies = new ArrayList<>();
//                    fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_DICTAMINACION_1 - new Integer(diasVerificar[i]));
//                    cn = cm.conectar();
//                    Movbco movbcoTemp;
//                    try {
//                        statement = cn.prepareStatement("SELECT IdBco,tipMov,fecMov FROM movbco WHERE IdBco = ? AND tipMov = ? ORDER BY fecMov DESC limit 1");
//                        statement.setInt(1, study.getIdBco());
//                        statement.setInt(2, Integer.parseInt(Constante.ESTATUS_ES_INGRESADO));
//                        rs = statement.executeQuery();
//                        while (rs.next()) {
//                            movbcoTemp = new Movbco();
//                            movbcoTemp.setFecMov(rs.getDate("fecMov"));
//                            movbcoTemp.setIdBco(rs.getInt("IdBco"));
//                            movbcoTemp.setTipMov(rs.getString("tipMov"));
//                            movStudies.add(movbcoTemp);
//                        }
//                    } catch (SQLException ex) {
//                        System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                    } finally {
//                        if (rs != null) {
//                            try {
//                                rs.close();
//                            } catch (SQLException ex) {
//                                System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                            }
//                        }
//                        if (statement != null) {
//                            try {
//                                statement.close();
//                            } catch (SQLException ex) {
//                                System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                            }
//                        }
//                        if (cn != null) {
//                            cm.desconectar(cn);
//                        }
//                    }
//                    if (movStudies.size() > 0) {
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                        if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
////                        System.out.println("estudio a dictaminar encontrado: " + movStudies.get(0).getIdmovbco());
//                            mail = null;
//                            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
//                                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//                                QueueSender queueSender = queueSession.createSender(null);
//                                mail = new Mail();
//                                mail.setIdBco(study.getIdBco() + "");
//                                //no lleva usuario
//                                mail.setIdRolUsu(Constante.ROL_SISTEMA);
//                                mail.setSubject("Aviso de la DGI");
//                                mail.setDiasParaNoticifacion(new Integer(diasVerificar[i]));
//                                //se crea el mensaje
//                                ObjectMessage message = queueSession.createObjectMessage(mail);
//                                //se envia el mensaje
//                                queueSender.send(mailQueue, message);
//
//                                //se cierra la conexion
//                                connection.close();
//                            } catch (JMSException ex) {
//                                System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
//                            }
//                        }
//                        //comparar para vencimiento de dictaminacion de esudio socioeconomico//                        
//                        fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_DICTAMINACION_1);
//                        if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
////                        System.out.println("estudio vencido encontrado: " + movStudies.get(0).getIdmovbco());
//                            mail = null;
//                            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
//                                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//                                QueueSender queueSender = queueSession.createSender(null);
//                                mail = new Mail();
//                                mail.setIdBco(study.getIdBco() + "");
//                                cn = cm.conectar();
//                                try {
//                                    statement = cn.prepareStatement("SELECT IdUsu FROM psolicitud WHERE IdSol = ?");
//                                    statement.setInt(1, study.getIdSol());
//                                    rs = statement.executeQuery();
//                                    while (rs.next()) {
//                                        mail.setIdUsuario(rs.getInt("IdUsu") + "");
//                                    }
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                                } finally {
//                                    if (rs != null) {
//                                        try {
//                                            rs.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (statement != null) {
//                                        try {
//                                            statement.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (cn != null) {
//                                        cm.desconectar(cn);
//                                    }
//                                }
//                                mail.setIdRolUsu(Constante.ROL_SISTEMA);
//                                mail.setEstatusBco(Constante.ESTATUS_ES_VENCIDO);
//                                mail.setSubject("Aviso de la DGI");
//                                cn = cm.conectar();
//                                int res;
//                                try {
//                                    statement = cn.prepareStatement("UPDATE relsolbco SET Status = ? WHERE IdBco = ?");
//                                    statement.setInt(1, Integer.parseInt(Constante.ESTATUS_ES_CANCELADO));
//                                    statement.setInt(2, study.getIdBco());
//                                    res = statement.executeUpdate();
//                                    statement = cn.prepareStatement("INSERT INTO movbco(IdBco,fecMov,tipMov,obs,status) values (?,?,?,?,?)");
//                                    statement.setInt(1, study.getIdBco());
//                                    statement.setDate(2, new java.sql.Date(new Date().getTime()));
//                                    statement.setString(3, Constante.ESTATUS_ES_CANCELADO);
//                                    statement.setString(4, "Cancelado autom\u00e1ticamente por inactividad");
//                                    statement.setString(5, Constante.ESTATUS_ES_BLOQUEADO);
//                                    res = statement.executeUpdate();
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                                } finally {
//                                    if (statement != null) {
//                                        try {
//                                            statement.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (cn != null) {
//                                        cm.desconectar(cn);
//                                    }
//                                }
//                                //se crea el mensaje
//                                ObjectMessage message = queueSession.createObjectMessage(mail);
//                                //se envia el mensaje
//                                queueSender.send(mailQueue, message);
//                                //se cierra la conexion
//                                connection.close();
//                            } catch (JMSException ex) {
//                                System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
//                            }
//                        }
//                    }
//                }
//            }
//            //
//            //
//            //se verifican estudios a ser DICTAMINADOS de 50 mdp a 500 mdp
//            //
//            //
//            diasVerificar = Constante.DIAS_TO_SEND_NOTIFICACION_DICT_ES_2.split(",");
//            for (int i = 0; i < diasVerificar.length; i++) {
////                fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_DICTAMINACION_2 - new Integer(diasVerificar[i]));
////                System.out.println("fecha a comparar para dic de 50 mdp a 500 mdp: " + fechaAComparar);
////                fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_DICTAMINACION_2);
////                System.out.println("fecha a comparar para cancelar de dictaminacion de 50 mdp a 500 mdp: " + fechaAComparar);
//                cn = cm.conectar();
//                studies = new ArrayList<>();
//                try {
//                    statement = cn.prepareStatement("SELECT IdBco,IdSol FROM relsolbco JOIN psolicitud using (IdSol) WHERE (Status = ? OR Status = ?) AND Monto >= 50000000.00 AND Monto <= 500000000.00");
//                    statement.setInt(1, Integer.parseInt(Constante.ESTATUS_ES_INGRESADO));
//                    statement.setInt(2, Integer.parseInt(Constante.ESTATUS_ES_REVISADO));
//                    rs = statement.executeQuery();
//                    while (rs.next()) {
//                        relsolbco = new Relsolbco();
//                        relsolbco.setIdBco(rs.getInt("IdBco"));
//                        relsolbco.setIdSol(rs.getInt("IdSol"));
//                        studies.add(relsolbco);
//                    }
//                } catch (SQLException ex) {
//                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                } finally {
//                    if (rs != null) {
//                        try {
//                            rs.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                        }
//                    }
//                    if (statement != null) {
//                        try {
//                            statement.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                        }
//                    }
//                    if (cn != null) {
//                        cm.desconectar(cn);
//                    }
//                }
//                for (Relsolbco study : studies) {
//                    movStudies = new ArrayList<>();
//                    fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_DICTAMINACION_2 - new Integer(diasVerificar[i]));
//                    cn = cm.conectar();
//                    Movbco movbcoTemp;
//                    try {
//                        statement = cn.prepareStatement("SELECT IdBco,tipMov,fecMov FROM movbco WHERE IdBco = ? AND tipMov = ? ORDER BY fecMov DESC limit 1");
//                        statement.setInt(1, study.getIdBco());
//                        statement.setInt(2, Integer.parseInt(Constante.ESTATUS_ES_INGRESADO));
//                        rs = statement.executeQuery();
//                        while (rs.next()) {
//                            movbcoTemp = new Movbco();
//                            movbcoTemp.setFecMov(rs.getDate("fecMov"));
//                            movbcoTemp.setIdBco(rs.getInt("IdBco"));
//                            movbcoTemp.setTipMov(rs.getString("tipMov"));
//                            movStudies.add(movbcoTemp);
//                        }
//                    } catch (SQLException ex) {
//                        System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                    } finally {
//                        if (rs != null) {
//                            try {
//                                rs.close();
//                            } catch (SQLException ex) {
//                                System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                            }
//                        }
//                        if (statement != null) {
//                            try {
//                                statement.close();
//                            } catch (SQLException ex) {
//                                System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                            }
//                        }
//                        if (cn != null) {
//                            cm.desconectar(cn);
//                        }
//                    }
//                    if (movStudies.size() > 0) {
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                        if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
////                        System.out.println("estudio a dictaminar encontrado: " + movStudies.get(0).getIdmovbco());
//                            mail = null;
//                            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
//                                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//                                QueueSender queueSender = queueSession.createSender(null);
//                                mail = new Mail();
//                                mail.setIdBco(study.getIdBco() + "");
//                                //no lleva usuario
//                                mail.setIdRolUsu(Constante.ROL_SISTEMA);
//                                mail.setSubject("Aviso de la DGI");
//                                mail.setDiasParaNoticifacion(new Integer(diasVerificar[i]));
//                                //se crea el mensaje
//                                ObjectMessage message = queueSession.createObjectMessage(mail);
//                                //se envia el mensaje
//                                queueSender.send(mailQueue, message);
//
//                                //se cierra la conexion
//                                connection.close();
//                            } catch (JMSException ex) {
//                                System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
//                            }
//                        }
//                        //comparar para vencimiento de dictaminacion de esudio socioeconomico
//                        fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_DICTAMINACION_2);
//                        if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
////                        System.out.println("estudio vencido encontrado: " + movStudies.get(0).getIdmovbco());
//                            mail = null;
//                            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
//                                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//                                QueueSender queueSender = queueSession.createSender(null);
//                                mail = new Mail();
//                                mail.setIdBco(study.getIdBco() + "");
//                                cn = cm.conectar();
//                                try {
//                                    statement = cn.prepareStatement("SELECT IdUsu FROM psolicitud WHERE IdSol = ?");
//                                    statement.setInt(1, study.getIdSol());
//                                    rs = statement.executeQuery();
//                                    while (rs.next()) {
//                                        mail.setIdUsuario(rs.getInt("IdUsu") + "");
//                                    }
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                                } finally {
//                                    if (rs != null) {
//                                        try {
//                                            rs.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (statement != null) {
//                                        try {
//                                            statement.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (cn != null) {
//                                        cm.desconectar(cn);
//                                    }
//                                }
//                                mail.setIdRolUsu(Constante.ROL_SISTEMA);
//                                mail.setEstatusBco(Constante.ESTATUS_ES_VENCIDO);
//                                mail.setSubject("Aviso de la DGI");
//                                cn = cm.conectar();
//                                int res;
//                                try {
//                                    statement = cn.prepareStatement("UPDATE relsolbco SET Status = ? WHERE IdBco = ?");
//                                    statement.setInt(1, Integer.parseInt(Constante.ESTATUS_ES_CANCELADO));
//                                    statement.setInt(2, study.getIdBco());
//                                    res = statement.executeUpdate();
//                                    statement = cn.prepareStatement("INSERT INTO movbco(IdBco,fecMov,tipMov,obs,status) values (?,?,?,?,?)");
//                                    statement.setInt(1, study.getIdBco());
//                                    statement.setDate(2, new java.sql.Date(new Date().getTime()));
//                                    statement.setString(3, Constante.ESTATUS_ES_CANCELADO);
//                                    statement.setString(4, "Cancelado autom\u00e1ticamente por inactividad");
//                                    statement.setString(5, Constante.ESTATUS_ES_BLOQUEADO);
//                                    res = statement.executeUpdate();
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                                } finally {
//                                    if (statement != null) {
//                                        try {
//                                            statement.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (cn != null) {
//                                        cm.desconectar(cn);
//                                    }
//                                }
//                                //se crea el mensaje
//                                ObjectMessage message = queueSession.createObjectMessage(mail);
//                                //se envia el mensaje
//                                queueSender.send(mailQueue, message);
//                                //se cierra la conexion
//                                connection.close();
//                            } catch (JMSException ex) {
//                                System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
//                            }
//                        }
//                    }
//                }
//            }
//            //
//            //
//            //se verifican estudios a ser DICTAMINADOS superiores a 500 mdp
//            //
//            //
//            diasVerificar = Constante.DIAS_TO_SEND_NOTIFICACION_DICT_ES_3.split(",");
//            for (int i = 0; i < diasVerificar.length; i++) {
////                fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_DICTAMINACION_3 - new Integer(diasVerificar[i]));
////                System.out.println("fecha a comparar para dic superiores a 500 mdp: " + fechaAComparar);
////                fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_DICTAMINACION_3);
////                System.out.println("fecha a comparar para cancelar de dictaminacion superiores a 500 mdp: " + fechaAComparar);
//                cn = cm.conectar();
//                studies = new ArrayList<>();
//                try {
//                    statement = cn.prepareStatement("SELECT IdBco,IdSol FROM relsolbco JOIN psolicitud using (IdSol) WHERE (Status = ? OR Status = ?) AND Monto > 500000000.00");
//                    statement.setInt(1, Integer.parseInt(Constante.ESTATUS_ES_INGRESADO));
//                    statement.setInt(2, Integer.parseInt(Constante.ESTATUS_ES_REVISADO));
//                    rs = statement.executeQuery();
//                    while (rs.next()) {
//                        relsolbco = new Relsolbco();
//                        relsolbco.setIdBco(rs.getInt("IdBco"));
//                        relsolbco.setIdSol(rs.getInt("IdSol"));
//                        studies.add(relsolbco);
//                    }
//                } catch (SQLException ex) {
//                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                } finally {
//                    if (rs != null) {
//                        try {
//                            rs.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                        }
//                    }
//                    if (statement != null) {
//                        try {
//                            statement.close();
//                        } catch (SQLException ex) {
//                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                        }
//                    }
//                    if (cn != null) {
//                        cm.desconectar(cn);
//                    }
//                }
//                for (Relsolbco study : studies) {
//                    movStudies = new ArrayList<>();
//                    fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_DICTAMINACION_3 - new Integer(diasVerificar[i]));
//                    cn = cm.conectar();
//                    Movbco movbcoTemp;
//                    try {
//                        statement = cn.prepareStatement("SELECT IdBco,tipMov,fecMov FROM movbco WHERE IdBco = ? AND tipMov = ? ORDER BY fecMov DESC limit 1");
//                        statement.setInt(1, study.getIdBco());
//                        statement.setInt(2, Integer.parseInt(Constante.ESTATUS_ES_INGRESADO));
//                        rs = statement.executeQuery();
//                        while (rs.next()) {
//                            movbcoTemp = new Movbco();
//                            movbcoTemp.setFecMov(rs.getDate("fecMov"));
//                            movbcoTemp.setIdBco(rs.getInt("IdBco"));
//                            movbcoTemp.setTipMov(rs.getString("tipMov"));
//                            movStudies.add(movbcoTemp);
//                        }
//                    } catch (SQLException ex) {
//                        System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                    } finally {
//                        if (rs != null) {
//                            try {
//                                rs.close();
//                            } catch (SQLException ex) {
//                                System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                            }
//                        }
//                        if (statement != null) {
//                            try {
//                                statement.close();
//                            } catch (SQLException ex) {
//                                System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                            }
//                        }
//                        if (cn != null) {
//                            cm.desconectar(cn);
//                        }
//                    }
//                    if (movStudies.size() > 0) {
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//                        if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
////                        System.out.println("estudio a dictaminar encontrado: " + movStudies.get(0).getIdmovbco());
//                            mail = null;
//                            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
//                                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//                                QueueSender queueSender = queueSession.createSender(null);
//                                mail = new Mail();
//                                mail.setIdBco(study.getIdBco() + "");
//                                //no lleva usuario
//                                mail.setIdRolUsu(Constante.ROL_SISTEMA);
//                                mail.setSubject("Aviso de la DGI");
//                                mail.setDiasParaNoticifacion(new Integer(diasVerificar[i]));
//                                //se crea el mensaje
//                                ObjectMessage message = queueSession.createObjectMessage(mail);
//                                //se envia el mensaje
//                                queueSender.send(mailQueue, message);
//
//                                //se cierra la conexion
//                                connection.close();
//                            } catch (JMSException ex) {
//                                System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
//                            }
//                        }
//                        //comparar para vencimiento de dictaminacion de esudio socioeconomico
//                        fechaAComparar = Plazo.obtieneFechaPlazo(Constante.VIGENCIA_ES_DICTAMINACION_3);
//                        if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
////                        System.out.println("estudio vencido encontrado: " + movStudies.get(0).getIdmovbco());
//                            mail = null;
//                            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
//                                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
//                                QueueSender queueSender = queueSession.createSender(null);
//                                mail = new Mail();
//                                mail.setIdBco(study.getIdBco() + "");
//                                cn = cm.conectar();
//                                try {
//                                    statement = cn.prepareStatement("SELECT IdUsu FROM psolicitud WHERE IdSol = ?");
//                                    statement.setInt(1, study.getIdSol());
//                                    rs = statement.executeQuery();
//                                    while (rs.next()) {
//                                        mail.setIdUsuario(rs.getInt("IdUsu") + "");
//                                    }
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                                } finally {
//                                    if (rs != null) {
//                                        try {
//                                            rs.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (statement != null) {
//                                        try {
//                                            statement.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (cn != null) {
//                                        cm.desconectar(cn);
//                                    }
//                                }
//                                mail.setIdRolUsu(Constante.ROL_SISTEMA);
//                                mail.setEstatusBco(Constante.ESTATUS_ES_VENCIDO);
//                                mail.setSubject("Aviso de la DGI");
//                                cn = cm.conectar();
//                                int res;
//                                try {
//                                    statement = cn.prepareStatement("UPDATE relsolbco SET Status = ? WHERE IdBco = ?");
//                                    statement.setInt(1, Integer.parseInt(Constante.ESTATUS_ES_CANCELADO));
//                                    statement.setInt(2, study.getIdBco());
//                                    res = statement.executeUpdate();
//                                    statement = cn.prepareStatement("INSERT INTO movbco(IdBco,fecMov,tipMov,obs,status) values (?,?,?,?,?)");
//                                    statement.setInt(1, study.getIdBco());
//                                    statement.setDate(2, new java.sql.Date(new Date().getTime()));
//                                    statement.setString(3, Constante.ESTATUS_ES_CANCELADO);
//                                    statement.setString(4, "Cancelado autom\u00e1ticamente por inactividad");
//                                    statement.setString(5, Constante.ESTATUS_ES_BLOQUEADO);
//                                    res = statement.executeUpdate();
//                                } catch (SQLException ex) {
//                                    System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//                                } finally {
//                                    if (statement != null) {
//                                        try {
//                                            statement.close();
//                                        } catch (SQLException ex) {
//                                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                                        }
//                                    }
//                                    if (cn != null) {
//                                        cm.desconectar(cn);
//                                    }
//                                }
//                                //se crea el mensaje
//                                ObjectMessage message = queueSession.createObjectMessage(mail);
//                                //se envia el mensaje
//                                queueSender.send(mailQueue, message);
//                                //se cierra la conexion
//                                connection.close();
//                            } catch (JMSException ex) {
//                                System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
//                            }
//                        }
//                    }
//                }
//            }
//            revisionTemporal = new Revisiontemporal();
//            revisionTemporal.setFechaRevision(Calendar.getInstance().getTime());
//            revisionTemporal.setRevisado(Boolean.TRUE);
//            revisionTemporal.setTipoRevision("solicitud");
//            em.persist(revisionTemporal);
//            System.out.println("Se realizo revision: " + Calendar.getInstance().getTime());
//        } else {
//            System.out.println("No se realizo revision: " + Calendar.getInstance().getTime());
//        }
//    }
//
//    @Schedule(dayOfWeek = Constante.DIA_DE_LA_SEMANA, month = Constante.MES_DEL_ANHO, hour = "*", dayOfMonth = Constante.DIAS_DEL_MES, year = Constante.ANHO, minute = "30", second = "0")
//    public void checkNotifications() {
//        Calendar fechaActual = Calendar.getInstance();
//        fechaActual.add(Calendar.DAY_OF_YEAR, (Constante.VIGENCIA_NOTIFICACIONES * -1));
//        Date fechaAComparar = fechaActual.getTime();
//        ConnectionManager cm = new ConnectionManager();
//        Connection cn;
//        PreparedStatement statement = null;
//        cn = cm.conectar();
//        int res = 0;
//        try {
//            statement = cn.prepareStatement("DELETE FROM notificacion WHERE fechaNotificacion < ?");
//            statement.setDate(1, new java.sql.Date(fechaAComparar.getTime()));
//            res = statement.executeUpdate();
//        } catch (SQLException ex) {
//            System.out.println("SQLException checkPetitionsAndStudies: " + ex.getMessage());
//        } finally {
//            if (statement != null) {
//                try {
//                    statement.close();
//                } catch (SQLException ex) {
//                    System.out.println("SQLException al cerrar statement: " + ex.getMessage());
//                }
//            }
//            if (cn != null) {
//                cm.desconectar(cn);
//            }
//        }
//        System.out.println("Se eliminaron " + res + " notificaciones.");
//    }
//
//    @Schedule(dayOfWeek = Constante.DIA_DE_LA_SEMANA, month = Constante.MES_DEL_ANHO, hour = Constante.HORAS_DEL_DIA, dayOfMonth = Constante.DIAS_DEL_MES, year = Constante.ANHO, minute = Constante.MINUTOS_DE_LA_HORA, second = "0")
//    public void deleteCheck() {
//        Query queryCheckDelete = null;
//        queryCheckDelete = em.createQuery("DELETE FROM Revisiontemporal r");
//        int numNotifi = queryCheckDelete.executeUpdate();
//        System.out.println("Se eliminaron " + numNotifi + " chequeos.");
//    }
}
