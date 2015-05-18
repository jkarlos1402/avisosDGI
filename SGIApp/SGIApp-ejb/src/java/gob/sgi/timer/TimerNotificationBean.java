/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.sgi.timer;

import gob.sgi.constante.Constante;
import gob.sgi.dto.Mail;
import gob.sgi.model.Movbco;
import gob.sgi.model.Psolicitud;
import gob.sgi.model.Relsolbco;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
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
import javax.persistence.TemporalType;

@Singleton
@LocalBean
public class TimerNotificationBean {

    @Resource
    TimerService timerService;

    @PersistenceContext
    private EntityManager em;

    @Resource(mappedName = "jms/QueueConnectionFactory")
    private QueueConnectionFactory connectionFactory;

    @Resource(mappedName = "QueueMail")
    private Queue mailQueue;

    @Schedule(dayOfWeek = Constante.DIA_DE_LA_SEMANA, month = Constante.MES_DEL_ANHO, hour = Constante.HORAS_DEL_DIA, dayOfMonth = Constante.DIAS_DEL_MES, year = Constante.ANHO, minute = Constante.MINUTOS_DE_LA_HORA, second = "0")
    public void checkPetitionsAndStudies() {
        List<Psolicitud> petitions = null;
        List<Relsolbco> studies = null;
        List<Movbco> movStudies = null;
        Calendar fechaActual = null;
        Date fechaAComparar = null;
        Query queryPetitions = null;
        Query queryStudies = null;
        Query queryMovStudies = null;
        Mail mail = null;
        String[] diasVerificar = Constante.DIAS_TO_SEND_NOTIFICACION_SOL.split(",");
        for (int i = 0; i < diasVerificar.length; i++) {
            //se verifica el periodo para reenviar una solicitud despues de ser revisada por el area
            fechaActual = Calendar.getInstance();
//            System.out.println("dias para verificar solicitudes: " + diasVerificar[i]);
            fechaActual.add(Calendar.DAY_OF_YEAR, ((Constante.VIGENCIA_SOL_OBS * -1) + new Integer(diasVerificar[i])));
            fechaAComparar = fechaActual.getTime();
            queryPetitions = em.createQuery("SELECT p FROM Psolicitud p WHERE p.idEdoSol = " + Constante.ESTATUS_SOL_REVISADA + " AND p.idSolPre IS NOT NULL AND p.fecEval = :fechaGenerada", Psolicitud.class);
            queryPetitions.setParameter("fechaGenerada", fechaAComparar, TemporalType.DATE);
//            System.out.println("fechaGenerada para envio de alerta de solicitud: " + fechaAComparar);
            petitions = queryPetitions.getResultList();
            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                QueueSender queueSender = queueSession.createSender(null);
                for (Psolicitud petition : petitions) {
                    mail = new Mail();
                    mail.setIdSolicitud(petition.getIdSol() + "");
                    mail.setIdUsuario(petition.getIdUsu() + "");
                    mail.setIdRolUsu(Constante.ROL_SISTEMA);
                    mail.setSubject("Aviso de la DGI");
                    mail.setDiasParaNoticifacion(new Integer(diasVerificar[i]));
                    //se crea el mensaje
                    ObjectMessage message = queueSession.createObjectMessage(mail);
                    //se envia el mensaje
                    queueSender.send(mailQueue, message);

                }
                //se cierra la conexion
                connection.close();
            } catch (JMSException ex) {
                System.out.println("JMSException al validar periodo solicitud: " + ex.getMessage());
            }

            //se cancelan solicitudes que no fueron atendidas despues del periodo permitido
            fechaActual = Calendar.getInstance();
            fechaActual.add(Calendar.DAY_OF_YEAR, ((Constante.VIGENCIA_SOL_OBS + 1) * -1));
            fechaAComparar = fechaActual.getTime();
            queryPetitions = em.createQuery("SELECT p FROM Psolicitud p WHERE p.idEdoSol = " + Constante.ESTATUS_SOL_REVISADA + " AND p.idSolPre IS NOT NULL AND p.fecEval = :fechaGenerada", Psolicitud.class);
            queryPetitions.setParameter("fechaGenerada", fechaAComparar, TemporalType.DATE);
//            System.out.println("fechaGenerada para cancelar solicitud: " + fechaAComparar);
            petitions = queryPetitions.getResultList();
            mail = null;
            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                QueueSender queueSender = queueSession.createSender(null);
                for (Psolicitud petition : petitions) {
                    mail = new Mail();
                    mail.setIdSolicitud(petition.getIdSol() + "");
                    mail.setIdUsuario(petition.getIdUsu() + "");
                    mail.setIdRolUsu(Constante.ROL_SISTEMA);
                    mail.setEstatusSolicitud(Constante.ESTATUS_SOL_CANCELADA);
                    mail.setSubject("Aviso de la DGI");
                    petition.setIdEdoSol(new Short(Constante.ESTATUS_SOL_CANCELADA));
                    em.merge(petition);
                    //se crea el mensaje
                    ObjectMessage message = queueSession.createObjectMessage(mail);
                    //se envia el mensaje
                    queueSender.send(mailQueue, message);

                }
                //se cierra la conexion
                connection.close();
            } catch (JMSException ex) {
                System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
            }
        }
        //se verifican estudios
        diasVerificar = Constante.DIAS_TO_SEND_NOTIFICACION_ES.split(",");
        for (int i = 0; i < diasVerificar.length; i++) {
//            System.out.println("dias para verificar estudios: " + diasVerificar[i]);
            queryStudies = em.createQuery("SELECT e FROM Relsolbco e WHERE e.status = " + Constante.ESTATUS_ES_OBSERVACIONES, Relsolbco.class);
            studies = queryStudies.getResultList();
            Movbco movbcoTemp = null;
            for (Relsolbco study : studies) {
                movStudies = null;
                queryMovStudies = null;
                fechaActual = Calendar.getInstance();
                fechaActual.add(Calendar.DAY_OF_YEAR, ((Constante.VIGENCIA_ES_OBS * -1) + new Integer(diasVerificar[i])));
                fechaAComparar = fechaActual.getTime();
                queryMovStudies = em.createQuery("SELECT m FROM Movbco m WHERE m.idBco = " + study.getIdBco() + " AND m.tipMov = " + Constante.ESTATUS_ES_OBSERVACIONES + " ORDER BY m.fecMov DESC");
                movStudies = queryMovStudies.setMaxResults(1).getResultList();
                if (movStudies.size() > 0) {
                    em.refresh(movStudies.get(0));
//                    System.out.println("fecha a comparar para aviso de es: " + fechaAComparar);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
//                        System.out.println("estudio encontrado: " + movStudies.get(0).getIdmovbco());
                        mail = null;
                        try (QueueConnection connection = connectionFactory.createQueueConnection()) {
                            QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                            QueueSender queueSender = queueSession.createSender(null);
                            mail = new Mail();
                            mail.setIdBco(study.getIdBco() + "");
                            mail.setIdUsuario(em.find(Psolicitud.class, study.getIdSol()).getIdUsu() + "");
                            mail.setIdRolUsu(Constante.ROL_SISTEMA);
                            mail.setSubject("Aviso de la DGI");
                            mail.setDiasParaNoticifacion(new Integer(diasVerificar[i]));
                            //se crea el mensaje
                            ObjectMessage message = queueSession.createObjectMessage(mail);
                            //se envia el mensaje
                            queueSender.send(mailQueue, message);

                            //se cierra la conexion
                            connection.close();
                        } catch (JMSException ex) {
                            System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
                        }
                    }
                    //comparar para cancelar estudio socioeconomico
                    fechaActual = Calendar.getInstance();
                    fechaActual.add(Calendar.DAY_OF_YEAR, ((Constante.VIGENCIA_ES_OBS + 1) * -1));
                    fechaAComparar = fechaActual.getTime();
//                    System.out.println("fecha a comparar para cancelar es: " + fechaAComparar);
                    if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
//                        System.out.println("estudio a cancelar encontrado: " + movStudies.get(0).getIdmovbco());
                        mail = null;
                        try (QueueConnection connection = connectionFactory.createQueueConnection()) {
                            QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                            QueueSender queueSender = queueSession.createSender(null);
                            mail = new Mail();
                            mail.setIdBco(study.getIdBco() + "");
                            mail.setIdUsuario(em.find(Psolicitud.class, study.getIdSol()).getIdUsu() + "");
                            mail.setIdRolUsu(Constante.ROL_SISTEMA);
                            mail.setEstatusBco(Constante.ESTATUS_ES_CANCELADO);
                            mail.setSubject("Aviso de la DGI");
                            study.setStatus(new Integer(Constante.ESTATUS_ES_CANCELADO));
                            em.merge(study);
                            movbcoTemp = new Movbco();
                            movbcoTemp.setFecMov(new Date());
                            movbcoTemp.setIdBco(study.getIdBco());
                            movbcoTemp.setObs("Cancelado autom\u00e1ticamente por inactividad");
                            movbcoTemp.setStatus(Constante.ESTATUS_ES_BLOQUEADO);
                            movbcoTemp.setTipMov(Constante.ESTATUS_ES_CANCELADO);
                            em.persist(movbcoTemp);
                            //se crea el mensaje
                            ObjectMessage message = queueSession.createObjectMessage(mail);
                            //se envia el mensaje
                            queueSender.send(mailQueue, message);
                            //se cierra la conexion
                            connection.close();
                        } catch (JMSException ex) {
                            System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
                        }
                    }
                }
            }
        }
        //se verifican estudios a ser dictaminados
        diasVerificar = Constante.DIAS_TO_SEND_NOTIFICACION_DICT_ES.split(",");
        for (int i = 0; i < diasVerificar.length; i++) {
//            System.out.println("dias para verificar dictaminacion de es: " + diasVerificar[i]);
            queryStudies = em.createQuery("SELECT e FROM Relsolbco e WHERE e.status = " + Constante.ESTATUS_ES_INGRESADO, Relsolbco.class);
            studies = queryStudies.getResultList();
            for (Relsolbco study : studies) {
                movStudies = null;
                queryMovStudies = null;
                fechaActual = Calendar.getInstance();
                fechaActual.add(Calendar.DAY_OF_YEAR, ((Constante.VIGENCIA_ES_DICTAMINACION * -1) + new Integer(diasVerificar[i])));
                fechaAComparar = fechaActual.getTime();
                queryMovStudies = em.createQuery("SELECT m FROM Movbco m WHERE m.idBco = " + study.getIdBco() + " AND m.tipMov = " + Constante.ESTATUS_ES_INGRESADO + " ORDER BY m.fecMov DESC");
                movStudies = queryMovStudies.setMaxResults(1).getResultList();
                if (movStudies.size() > 0) {
                    em.refresh(movStudies.get(0));
//                    System.out.println("fecha a comparar para dic: " + fechaAComparar);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
//                        System.out.println("estudio a dictaminar encontrado: " + movStudies.get(0).getIdmovbco());
                        mail = null;
                        try (QueueConnection connection = connectionFactory.createQueueConnection()) {
                            QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                            QueueSender queueSender = queueSession.createSender(null);
                            mail = new Mail();
                            mail.setIdBco(study.getIdBco() + "");
                            //no lleva usuario
                            mail.setIdRolUsu(Constante.ROL_SISTEMA);
                            mail.setSubject("Aviso de la DGI");
                            mail.setDiasParaNoticifacion(new Integer(diasVerificar[i]));
                            //se crea el mensaje
                            ObjectMessage message = queueSession.createObjectMessage(mail);
                            //se envia el mensaje
                            queueSender.send(mailQueue, message);

                            //se cierra la conexion
                            connection.close();
                        } catch (JMSException ex) {
                            System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
                        }
                    }
                    //comparar para vencimiento de dictaminacion de esudio socioeconomico
                    fechaActual = Calendar.getInstance();
                    fechaActual.add(Calendar.DAY_OF_YEAR, ((Constante.VIGENCIA_ES_DICTAMINACION + 1) * -1));
                    fechaAComparar = fechaActual.getTime();
//                    System.out.println("fecha a comparar para vencimiento de dictaminacion: " + fechaAComparar);
                    if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
//                        System.out.println("estudio vencido encontrado: " + movStudies.get(0).getIdmovbco());
                        mail = null;
                        try (QueueConnection connection = connectionFactory.createQueueConnection()) {
                            QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                            QueueSender queueSender = queueSession.createSender(null);
                            mail = new Mail();
                            mail.setIdBco(study.getIdBco() + "");
                            //no hay usuario
                            mail.setIdRolUsu(Constante.ROL_SISTEMA);
                            mail.setEstatusBco(Constante.ESTATUS_ES_VENCIDO);
                            mail.setSubject("Aviso de la DGI");
                            //se crea el mensaje
                            ObjectMessage message = queueSession.createObjectMessage(mail);
                            //se envia el mensaje
                            queueSender.send(mailQueue, message);
                            //se cierra la conexion
                            connection.close();
                        } catch (JMSException ex) {
                            System.out.println("JMSException al validar periodo a cancelar: " + ex.getMessage());
                        }
                    }
                }
            }
//            System.out.println("Fecha y hora del evento: " + new Date());
        }

    }

    @Schedule(dayOfWeek = Constante.DIA_DE_LA_SEMANA, month = Constante.MES_DEL_ANHO, hour = Constante.HORAS_DEL_DIA, dayOfMonth = Constante.DIAS_DEL_MES, year = Constante.ANHO, minute = Constante.MINUTOS_DE_LA_HORA, second = "0")
    public void checkNotifications() {
        Calendar fechaActual = Calendar.getInstance();
        fechaActual.add(Calendar.DAY_OF_YEAR, (Constante.VIGENCIA_NOTIFICACIONES * -1));
        Date fechaAComparar = fechaActual.getTime();
        Query queryNotificationsDelete = null;
//        System.out.println("fecha a verificar para eliminar notificaciones: "+fechaAComparar);
        queryNotificationsDelete = em.createQuery("DELETE FROM Notificacion n WHERE n.fechaNotificacion < :fechaGenerada");
        queryNotificationsDelete.setParameter("fechaGenerada", fechaAComparar, TemporalType.DATE);
        int numNotifi = queryNotificationsDelete.executeUpdate();
        System.out.println("Se eliminaron "+numNotifi+" notificaciones.");
    }
}
