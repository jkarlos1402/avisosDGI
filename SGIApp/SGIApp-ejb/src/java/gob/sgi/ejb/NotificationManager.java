/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.sgi.ejb;

import gob.sgi.model.Constante;
import gob.sgi.model.Mail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.sql.DataSource;

/**
 * MessageDrivenBean encargado de llamar al ejb que enviar� el correo cuando
 * llegue un mensaje a la cola, de manera as�ncrona
 *
 * @author Juan Carlos Pi�a
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "QueueMail")
})
public class NotificationManager implements MessageListener {

    @EJB
    private EMailSender eMailSender;       

    @Resource(lookup = "SGIDB")
    DataSource dsSGIDB;    
    
    @EJB
    private NotificationSender notificationSender;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = null;
        Mail mail = null;
        Connection conSGI = null;
        Connection conUsu = null;
        if (message instanceof ObjectMessage) {
            objectMessage = (ObjectMessage) message;
        }

        try {
            mail = (Mail) objectMessage.getObject();
            PreparedStatement statement = null;          
            //Se recuperan los destinatarios del correo dependiendo del rol del usuario que realizo la peticion
            conSGI = dsSGIDB.getConnection();
//            conUsu = dsUsuariosDB.getConnection();
            List recipients = new ArrayList<>();
            List userRecipients = new ArrayList<>();
            String unidadEjecutora = "";
            ResultSet rs;
            switch (mail.getIdRolUsu()) {
                case Constante.ROL_DEPENDENCIA: //dependencia es la que genera la notificacion
                    // se procede a buscar el correo del �rea que le corresponde
                    if (!mail.getIdSolicitud().equals("")) {
                        statement = conSGI.prepareStatement("select idusu,emailUsu from ctrlusuarios.infousuario where idusu = ("
                                + "select idusu from ctrlusuarios.usuarios "
                                + "where idUsu in (select idUsu from sgi2015.rususec where IdSec = "
                                + "(select idSec from rususec where idUsu = ? limit 1)) and idRol = 3 limit 1)");
                        statement.setInt(1, Integer.parseInt(mail.getIdUsuario()));
                    } else if (!mail.getIdBco().equals("")) {
                        statement = conUsu.prepareStatement("select idusu,emailUsu from infousuario where idusu in (select idusu from usuarios where idRol = 2)");                        
                    }
                    if (statement != null) {
                        rs = statement.executeQuery();
                        while (rs.next()) {
                            recipients.add(rs.getString("emailUsu"));
                            userRecipients.add(rs.getInt("idusu")+"");
                        }
                    }
                    statement = conSGI.prepareStatement("select NomUE from catue where IdUE = "
                            + "(select idUE from ctrlusuarios.usuarios where idusu = ? limit 1)");
                    statement.setInt(1, Integer.parseInt(mail.getIdUsuario()));
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
                    statement.setInt(1, Integer.parseInt(mail.getIdBco()));
                    rs = statement.executeQuery();
                    while (rs.next()) {
                        recipients.add(rs.getString("emailUsu"));
                        userRecipients.add(rs.getInt("idusu")+"");
                    }
                    //se manda a persistir a base de datos
                    notificationSender.sendNotification(mail, userRecipients);
                    break;
                case Constante.ROL_AREA: // el area es la que genera la notificacion
                    // se busca el correo de la unidad ejecutora a quien se le notificara                    
                    statement = conSGI.prepareStatement("select idusu,emailUsu from ctrlusuarios.infousuario where idusu = ("
                            + "select IdUsu from sgi2015.psolicitud where IdSol = ?)");
                    statement.setInt(1, Integer.parseInt(mail.getIdSolicitud()));
                    rs = statement.executeQuery();
                    while (rs.next()) {
                        recipients.add(rs.getString("emailUsu"));
                        userRecipients.add(rs.getInt("idusu")+"");
                    }
                    //se manda a persistir a base de datos
                    notificationSender.sendNotification(mail, userRecipients);
                    break;
                case Constante.ROL_VENTANILLA: // ventanilla es la que genera la notificacion
                    // se busca el correo de la unidad ejecutora a quien se le notificara                    
                    statement = conSGI.prepareStatement("select idusu,emailUsu from ctrlusuarios.infousuario where idusu = ("
                            + "select IdUsu from sgi2015.psolicitud where IdSol = ?)");
                    statement.setInt(1, Integer.parseInt(mail.getIdSolicitud()));
                    rs = statement.executeQuery();
                    while (rs.next()) {
                        recipients.add(rs.getString("emailUsu"));
                        userRecipients.add(rs.getInt("idusu")+"");
                    }
                    // se busca el correo del area a quien se le notificara
                    statement = conSGI.prepareStatement("select idusu,emailUsu from ctrlusuarios.infousuario where idusu = ("
                            + "select idusu from ctrlusuarios.usuarios "
                            + "where idUsu in (select idUsu from sgi2015.rususec where IdSec = "
                            + "(select idSec from sgi2015.rususec where idUsu = (select IdUsu from sgi2015.psolicitud where IdSol = ?) limit 1)) and idRol = 3 limit 1)");
                    statement.setInt(1, Integer.parseInt(mail.getIdSolicitud()));                    
                    rs = statement.executeQuery();
                    while (rs.next()) {
                        recipients.add(rs.getString("emailUsu"));
                        userRecipients.add(rs.getInt("idusu")+"");
                    }
                    //se manda a persistir a base de datos
                    notificationSender.sendNotification(mail, userRecipients);
                    break;
            }
            mail.setRecipients(recipients);
            System.out.println(mail.getRecipients().toString());
            eMailSender.send(mail);
        } catch (JMSException ex) {
            System.out.println("JMSException: "+ex.getMessage());
        } catch (SQLException ex) {
            System.out.println("SQLException: "+ex.getMessage());
        }
    }

}
