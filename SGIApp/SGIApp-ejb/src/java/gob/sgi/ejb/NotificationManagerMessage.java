package gob.sgi.ejb;

import gob.sgi.constante.Constante;
import gob.sgi.dto.Mail;
import gob.sgi.model.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 * MessageDrivenBean encargado de llamar al ejb que enviará el correo cuando
 * llegue un mensaje a la cola, de manera asíncrona
 *
 * @author Juan Carlos Piña
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "QueueMail")
})
public class NotificationManagerMessage implements MessageListener {

    @EJB
    private EMailSender eMailSender;

    @EJB
    private NotificationSender notificationSender;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = null;
        Mail mail = null;
        Connection conSGI = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        ConnectionManager cm = new ConnectionManager();
        if (message instanceof ObjectMessage) {
            objectMessage = (ObjectMessage) message;
        }
        try {
            mail = (Mail) objectMessage.getObject();
            System.out.println(mail.toString());
            //Se recuperan los destinatarios del correo dependiendo del rol del usuario que realizo la peticion            
            conSGI = cm.conectar();
            List recipients = new ArrayList<>();
            List userRecipients = new ArrayList<>();
            String unidadEjecutora = "";
            switch (mail.getIdRolUsu()) {
                case Constante.ROL_DEPENDENCIA: //dependencia es la que genera la notificacion
                    // se procede a buscar el correo del área que le corresponde
                    if (!mail.getIdSolicitud().equals("")) {
                        statement = conSGI.prepareStatement("select idusu,emailUsu from ctrlusuarios.infousuario where idusu in ("
                                + "select idusu from ctrlusuarios.usuarios "
                                + "where idUsu in (select idUsu from sgi2015.rususec where IdSec = "
                                + "(select idSec from rususec where idUsu = ? limit 1)) and idRol = " + Constante.ROL_AREA + ")");
                        statement.setInt(1, Integer.parseInt(mail.getIdUsuario() != null ? mail.getIdUsuario() : "0"));
                    } else if (!mail.getIdBco().equals("")) {
                        statement = conSGI.prepareStatement("select idusu,emailUsu from ctrlusuarios.infousuario join ctrlusuarios.usuarios using (idusu) where IdUSu in (select idusu from sgi2015.rususec where IdSec = (select idSec from sgi2015.rususec where idUsu = ? limit 1)) and idRol = '" + Constante.ROL_BANCO + "'");
                        statement.setInt(1, Integer.parseInt(mail.getIdUsuario() != null ? mail.getIdUsuario() : "0"));
                    }
                    if (statement != null) {
                        rs = statement.executeQuery();
                        while (rs.next()) {
                            recipients.add(rs.getString("emailUsu") != null ? rs.getString("emailUsu") : "");
                            userRecipients.add(rs.getInt("idusu") + "");
                        }
                    }
                    statement = conSGI.prepareStatement("select NomUE from catue where IdUE = "
                            + "(select idUE from ctrlusuarios.usuarios where idusu = ? limit 1)");
                    statement.setInt(1, Integer.parseInt(mail.getIdUsuario() != null ? mail.getIdUsuario() : "0"));
                    rs = statement.executeQuery();
                    while (rs.next()) {
                        unidadEjecutora = rs.getString("NomUE");
                    }
                    mail.setUnidadEjecutora(unidadEjecutora);
                    //se manda a persistir a base de datos
                    notificationSender.sendNotification(mail, userRecipients);
                    break;
                case Constante.ROL_BANCO: // banco es la que genera la notificacion
                    // se busca el correo de la unidad ejecutora a quien se le notificara                    
                    statement = conSGI.prepareStatement("select idusu,emailUsu from ctrlusuarios.infousuario where idusu = ("
                            + "select IdUsu from sgi2015.psolicitud where IdSol = ("
                            + "select IdSol from sgi2015.relsolbco where IdBco = ?))");
                    statement.setInt(1, Integer.parseInt(mail.getIdBco() != null ? mail.getIdBco() : "0"));
                    rs = statement.executeQuery();
                    while (rs.next()) {
                        recipients.add(rs.getString("emailUsu") != null ? rs.getString("emailUsu") : "");
                        userRecipients.add(rs.getInt("idusu") + "");
                    }
                    //se manda a persistir a base de datos
                    notificationSender.sendNotification(mail, userRecipients);
                    break;
                case Constante.ROL_AREA: // el area es la que genera la notificacion
                    // se busca el correo de la unidad ejecutora a quien se le notificara                    
                    statement = conSGI.prepareStatement("select idusu,emailUsu from ctrlusuarios.infousuario where idusu = ("
                            + "select IdUsu from sgi2015.psolicitud where IdSol = ?)");
                    statement.setInt(1, Integer.parseInt(mail.getIdSolicitud() != null ? mail.getIdSolicitud() : "0"));
                    rs = statement.executeQuery();
                    while (rs.next()) {
                        recipients.add(rs.getString("emailUsu") != null ? rs.getString("emailUsu") : "");
                        userRecipients.add(rs.getInt("idusu") + "");
                    }
                    //se manda a persistir a base de datos
                    notificationSender.sendNotification(mail, userRecipients);
                    break;
                case Constante.ROL_VENTANILLA: // ventanilla es la que genera la notificacion
                    // se busca el correo de la unidad ejecutora a quien se le notificara                    
                    statement = conSGI.prepareStatement("select idusu,emailUsu from ctrlusuarios.infousuario where idusu = ("
                            + "select IdUsu from sgi2015.psolicitud where IdSol = ?)");
                    statement.setInt(1, Integer.parseInt(mail.getIdSolicitud() != null ? mail.getIdSolicitud() : "0"));
                    rs = statement.executeQuery();
                    while (rs.next()) {
                        recipients.add(rs.getString("emailUsu") != null ? rs.getString("emailUsu") : "");
                        userRecipients.add(rs.getInt("idusu") + "");
                    }
                    // se busca el correo del area a quien se le notificara
                    statement = conSGI.prepareStatement("select idusu,emailUsu from ctrlusuarios.infousuario where idusu = ("
                            + "select idusu from ctrlusuarios.usuarios "
                            + "where idUsu in (select idUsu from sgi2015.rususec where IdSec = "
                            + "(select idSec from sgi2015.rususec where idUsu = (select IdUsu from sgi2015.psolicitud where IdSol = ?) limit 1)) and idRol = " + Constante.ROL_AREA + " limit 1)");
                    statement.setInt(1, Integer.parseInt(mail.getIdSolicitud() != null ? mail.getIdSolicitud() : "0"));
                    rs = statement.executeQuery();
                    while (rs.next()) {
                        recipients.add(rs.getString("emailUsu") != null ? rs.getString("emailUsu") : "");
                        userRecipients.add(rs.getInt("idusu") + "");
                    }
                    //se manda a persistir a base de datos
                    notificationSender.sendNotification(mail, userRecipients);
                    break;
                case Constante.ROL_SISTEMA:
                    if (mail.getIdBco() != null && mail.getIdUsuario() == null) {
                        statement = conSGI.prepareStatement("select idusu,emailUsu from ctrlusuarios.infousuario where idusu in (select idusu from ctrlusuarios.usuarios where idRol = " + Constante.ROL_BANCO + ")");
                        rs = statement.executeQuery();
                        while (rs.next()) {
                            recipients.add(rs.getString("emailUsu") != null ? rs.getString("emailUsu") : "");
                            userRecipients.add(rs.getInt("idusu") + "");
                        }
                    } else if (mail.getIdBco() != null && mail.getEstatusBco() != null && mail.getEstatusBco().equals(Constante.ESTATUS_ES_VENCIDO)) {
                        statement = conSGI.prepareStatement("select idusu,emailUsu from ctrlusuarios.infousuario where idusu in (select idusu from ctrlusuarios.usuarios where idRol = " + Constante.ROL_BANCO + ")");
                        rs = statement.executeQuery();
                        while (rs.next()) {
                            recipients.add(rs.getString("emailUsu") != null ? rs.getString("emailUsu") : "");
                            userRecipients.add(rs.getInt("idusu") + "");
                        }
                        statement = conSGI.prepareStatement("select idusu,emailUsu from ctrlusuarios.infousuario where idusu = ?");
                        statement.setInt(1, Integer.parseInt(mail.getIdUsuario() != null ? mail.getIdUsuario() : "0"));
                        rs = statement.executeQuery();
                        while (rs.next()) {
                            recipients.add(rs.getString("emailUsu") != null ? rs.getString("emailUsu") : "");
                            userRecipients.add(rs.getInt("idusu") + "");
                        }
                    } else {
                        statement = conSGI.prepareStatement("select idusu,emailUsu from ctrlusuarios.infousuario where idusu = ?");
                        statement.setInt(1, Integer.parseInt(mail.getIdUsuario() != null ? mail.getIdUsuario() : "0"));
                        rs = statement.executeQuery();
                        while (rs.next()) {
                            recipients.add(rs.getString("emailUsu") != null ? rs.getString("emailUsu") : "");
                            userRecipients.add(rs.getInt("idusu") + "");
                        }
                    }
                    //se manda a persistir a base de datos
                    notificationSender.sendNotification(mail, userRecipients);
                    break;
            }
            mail.setRecipients(recipients);
            // se envia correo a los correspondientes
            eMailSender.send(mail);
        } catch (JMSException ex) {
            System.out.println("JMSException: " + ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException ex) {
                    System.out.println("SQLException al cerrar resultSet: " + ex.getMessage());
                }
            }
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    System.out.println("SQLException al cerrar statement: " + ex.getMessage());
                }
            }
            if (conSGI != null) {
                cm.desconectar(conSGI);
            }
        }
    }

}
