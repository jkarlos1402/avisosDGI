/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.sgi.ejb;

import gob.sgi.model.Mail;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author intel core i 7
 */
@Stateless
@LocalBean
public class EMailSender {

    public boolean send(Mail mail) {
        String linea;
        StringBuilder header = new StringBuilder();
        StringBuilder body = new StringBuilder();
        StringBuilder footer = new StringBuilder();
        try {
            FileReader fileReader = new FileReader(Mail.HEADER_PATH);
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                while ((linea = bufferedReader.readLine()) != null) {
                    header.append(linea);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EMailSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EMailSender.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            FileReader fileReader = new FileReader(Mail.BODY_PATH);
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                while ((linea = bufferedReader.readLine()) != null) {
                    body.append(linea);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EMailSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EMailSender.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            FileReader fileReader = new FileReader(Mail.FOOTER_PATH);
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                while ((linea = bufferedReader.readLine()) != null) {
                    footer.append(linea);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(EMailSender.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EMailSender.class.getName()).log(Level.SEVERE, null, ex);
        }
        mail.setHeader(header.toString().replace("$idSolicitud", mail.getIdSolicitud() != null ? mail.getIdSolicitud() : ""));
        mail.setBody(body.toString().replace("$idSolicitud", mail.getIdSolicitud() != null ? mail.getIdSolicitud() : ""));
        mail.setFooter(footer.toString().replace("$idSolicitud", mail.getIdSolicitud() != null ? mail.getIdSolicitud() : ""));
        Properties props = new Properties();
        props.put("mail.smtp.user", Mail.SMTP_USER);
        props.put("mail.smtp.host", Mail.SMTP_HOST);
        props.put("mail.smtp.port", Mail.SMTP_PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", Mail.SMTP_PORT);
        props.put("mail.smtp.socketFactory.fallback", "false");
        try {
            Session session = Session.getDefaultInstance(props,
                    new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(Mail.SMTP_USER, Mail.SMTP_PASSWORD);
                        }
                    });
            session.setDebug(true);

            MimeMessage msg = new MimeMessage(session);
            msg.setText(mail.getHeader().concat(mail.getBody()).concat(mail.getFooter()), "utf-8", "html");            
            msg.setSubject(mail.getSubject());
            msg.setFrom(new InternetAddress(Mail.SMTP_USER));
            for (String recipient : mail.getRecipients()) {
                msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            }            
            Transport.send(msg);
            System.out.println("Mensaje enviado");
        } catch (Exception mex) {
            mex.printStackTrace();
        }
        return true;
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
