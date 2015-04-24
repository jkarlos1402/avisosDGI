/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.sgi.ejb;

import gob.sgi.model.Mail;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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
public class MailManager implements MessageListener {

    @EJB
    private EMailSender eMailSender;

    @Resource(lookup = "SGIDB")
    DataSource dsSGIDB;

    @Resource(lookup = "CtrlUsuariosDB")
    DataSource dsUsuariosDB;

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
            conUsu = dsUsuariosDB.getConnection();
            if (mail.getIdRolUsu().equals("1")) {
                // se procede a buscar el correo del área que le corresponde
                if (mail.getIdSolicitud() != null) {
                    statement = conSGI.prepareStatement("select emailUsu from ctrlusuarios.infousuario where idusu = ("
                            + "select idusu from ctrlusuarios.usuarios "
                            + "where idUsu in (select idUsu from sgi2015.rususec where IdSec = "
                            + "(select idSec from rususec where idUsu = ? limit 1)) and idRol = 3 limit 1)");
                    statement.setInt(1, Integer.parseInt(mail.getIdUsuario()));                    
                }else if(mail.getIdBco()!= null){
                    statement = conSGI.prepareStatement("select emailUsu from infousuario where idusu in (select idusu from usuarios where idRol = 2)");                    
                }
                ResultSet rs = statement.executeQuery();
                List recipients = new ArrayList<>();
                while (rs.next()) {
                    recipients.add(rs.getString("emailUsu"));
                }
                System.out.println("recipients: " + recipients.toString());
            }

            //eMailSender.send(mail);
        } catch (JMSException ex) {
            throw new EJBException(ex);
        } catch (SQLException ex) {
            Logger.getLogger(MailManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
