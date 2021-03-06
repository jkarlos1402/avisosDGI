package gob.sgi.ejb;

import gob.sgi.constante.Constante;
import gob.sgi.model.EstatusES;
import gob.sgi.model.EstatusSolicitud;
import gob.sgi.dto.Mail;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
@LocalBean
public class EMailSender {

    public boolean send(Mail mail) {
        String linea;
        StringBuilder header = new StringBuilder();
        StringBuilder body = new StringBuilder();
        StringBuilder footer = new StringBuilder();
        String tipoBody = "";
        try {// encabezado del correo            
            FileReader fileReader = new FileReader(Constante.HEADER_PATH);
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                while ((linea = bufferedReader.readLine()) != null) {
                    header.append(linea);
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException header: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException header: " + ex.getMessage());
        }
        switch (mail.getIdRolUsu()) {
            case Constante.ROL_DEPENDENCIA://dependencia es la que genera la notificacion
                if (mail.getEstatusSolicitud().equals(Constante.ESTATUS_SOL_ENVIADA)) {
                    tipoBody = Constante.SOL_ENV_BODY_PATH;
                } else if (mail.getEstatusSolicitud().equals(Constante.ESTATUS_SOL_INGRESADA)) {
                    tipoBody = Constante.SOL_ENV_BODY_PATH;
                } else if (mail.getEstatusBco().equals(Constante.ESTATUS_ES_ENVIADO)) {
                    tipoBody = Constante.ES_ENV_BODY_PATH;
                }
                break;
            case Constante.ROL_BANCO:// banco es la que genera la notificacion
                switch (mail.getEstatusBco()) {
                    case Constante.ESTATUS_ES_INGRESADO:// ingreso de estudio socioeconomico
                        tipoBody = Constante.ES_INGRESO_BODY_PATH;
                        break;
                    case Constante.ESTATUS_ES_OBSERVACIONES://regreso de estudio socioeconomico con observaciones 
                        tipoBody = Constante.ES_OBS_BODY_PATH;
                        break;
                    case Constante.ESTATUS_ES_DICTAMINADO:// estudio socioeconomico dictaminado
                        tipoBody = Constante.ES_DICT_BODY_PATH;
                        break;
                }
                break;
            case Constante.ROL_AREA:// el area es la que genera la notificacion
                if (mail.getEstatusSolicitud().equals(Constante.ESTATUS_SOL_REVISADA)) {// se regresa solicitud con observaciones
                    tipoBody = Constante.SOL_OBS_BODY_PATH;
                } else if (mail.getEstatusSolicitud().equals(Constante.ESTATUS_SOL_ACEPTADA)) {// solicitud aceptada
                    tipoBody = Constante.SOL_ACEPT_BODY_PATH;
                } else if (!mail.getIdObra().equals("")) {//creacion de obra
                    tipoBody = Constante.OBR_CREADA_BODY_PATH;
                }
                break;
            case Constante.ROL_VENTANILLA:
                if (!mail.getIdSolicitud().trim().equals("")) {// ingreso de solicitud
                    tipoBody = Constante.SOL_INGRESO_BODY_PATH;
                }
                break;
            case Constante.ROL_SISTEMA:
                if (mail.getIdSolicitud() != null && !mail.getIdSolicitud().trim().equals("") && mail.getEstatusSolicitud() == null) {
                    tipoBody = Constante.SOL_NO_ACTIVIDAD_BODY_PATH;
                } else if (mail.getIdSolicitud() != null && !mail.getIdSolicitud().trim().equals("") && mail.getEstatusSolicitud().equals(Constante.ESTATUS_SOL_CANCELADA)) {
                    tipoBody = Constante.SOL_CANCELADA_BODY_PATH;
                } else if (mail.getIdBco() != null && !mail.getIdBco().equals("") && mail.getEstatusBco() == null) {
                    tipoBody = Constante.ES_NO_ACTIVIDAD_BODY_PATH;
                } else if (mail.getIdBco() != null && !mail.getIdBco().equals("") && mail.getEstatusBco().equals(Constante.ESTATUS_ES_CANCELADO)) {
                    tipoBody = Constante.ES_CANCELADO_BODY_PATH;
                } else if (mail.getIdBco() != null && !mail.getIdBco().equals("") && mail.getEstatusBco() == null && mail.getIdUsuario() == null) {
                    tipoBody = Constante.ES_NO_ACTIVIDAD_DICT_BODY_PATH;
                } else if (mail.getIdBco() != null && !mail.getIdBco().equals("") && mail.getEstatusBco().equals(Constante.ESTATUS_ES_VENCIDO)) {
                    tipoBody = Constante.ES_CANCELADO_BODY_PATH;
                }
                break;
        }
        try {// cuerpo del correo dependiendo del tipo de notificacion
            FileReader fileReader = new FileReader(tipoBody);
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                while ((linea = bufferedReader.readLine()) != null) {
                    body.append(linea);
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException body email: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException body email: " + ex.getMessage());
        }
        try {//pie del correo
            FileReader fileReader = new FileReader(Constante.FOOTER_PATH);
            try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                while ((linea = bufferedReader.readLine()) != null) {
                    footer.append(linea);
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException footer email: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException footer email: " + ex.getMessage());
        }
        // se sustituyen palabras reservadas dentro del texto del correo
        mail.setHeader(header.toString().replace(Constante.STR_ID_SOLICITUD, mail.getIdSolicitud() != null ? mail.getIdSolicitud().trim() : "")
                .replace(Constante.STR_ID_BANCO, mail.getIdBco() != null ? mail.getIdBco() : "")
                .replace(Constante.STR_ESTATUS_SOL, mail.getEstatusSolicitud() != null ? EstatusSolicitud.val(mail.getEstatusSolicitud()) : "")
                .replace(Constante.STR_ESTATUS_ES, mail.getEstatusBco() != null ? EstatusES.val(mail.getEstatusBco()) : "")
                .replace(Constante.STR_NOM_UE, mail.getUnidadEjecutora() != null ? mail.getUnidadEjecutora() : "")
                .replace(Constante.STR_ID_OBRA, mail.getIdObra() != null ? mail.getIdObra() : "")
                .replace(Constante.STR_DIAS_CANCELACION, mail.getDiasParaNoticifacion() + "")
                .replace(Constante.STR_NUM_DICTAMEN, mail.getNumDictamen()+ "")
                .replace(Constante.STR_DIR_SGI, Constante.DIR_SGI));
        mail.setBody(body.toString().replace(Constante.STR_ID_SOLICITUD, mail.getIdSolicitud() != null ? mail.getIdSolicitud().trim() : "")
                .replace(Constante.STR_ID_BANCO, mail.getIdBco() != null ? mail.getIdBco() : "")
                .replace(Constante.STR_ESTATUS_SOL, mail.getEstatusSolicitud() != null ? EstatusSolicitud.val(mail.getEstatusSolicitud()) : "")
                .replace(Constante.STR_ESTATUS_ES, mail.getEstatusBco() != null ? EstatusES.val(mail.getEstatusBco()) : "")
                .replace(Constante.STR_NOM_UE, mail.getUnidadEjecutora() != null ? mail.getUnidadEjecutora() : "")
                .replace(Constante.STR_ID_OBRA, mail.getIdObra() != null ? mail.getIdObra() : "")
                .replace(Constante.STR_DIAS_CANCELACION, mail.getDiasParaNoticifacion() + "")
                .replace(Constante.STR_NUM_DICTAMEN, mail.getNumDictamen()+ "")
                .replace(Constante.STR_DIR_SGI, Constante.DIR_SGI));
        mail.setFooter(footer.toString().replace(Constante.STR_ID_SOLICITUD, mail.getIdSolicitud() != null ? mail.getIdSolicitud().trim() : "")
                .replace(Constante.STR_ID_BANCO, mail.getIdBco() != null ? mail.getIdBco() : "")
                .replace(Constante.STR_ESTATUS_SOL, mail.getEstatusSolicitud() != null ? EstatusSolicitud.val(mail.getEstatusSolicitud()) : "")
                .replace(Constante.STR_ESTATUS_ES, mail.getEstatusBco() != null ? EstatusES.val(mail.getEstatusBco()) : "")
                .replace(Constante.STR_NOM_UE, mail.getUnidadEjecutora() != null ? mail.getUnidadEjecutora() : "")
                .replace(Constante.STR_ID_OBRA, mail.getIdObra() != null ? mail.getIdObra() : "")
                .replace(Constante.STR_DIAS_CANCELACION, mail.getDiasParaNoticifacion() + "")
                .replace(Constante.STR_NUM_DICTAMEN, mail.getNumDictamen()+ "")
                .replace(Constante.STR_DIR_SGI, Constante.DIR_SGI));
        Properties props = new Properties();
        props.put("mail.smtp.user", Constante.SMTP_USER);
        props.put("mail.smtp.host", Constante.SMTP_HOST);
        props.put("mail.smtp.port", Constante.SMTP_PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", Constante.SMTP_PORT);
        props.put("mail.smtp.socketFactory.fallback", "false");
        try {
            if (!mail.getRecipients().isEmpty()) {
                Session session = Session.getInstance(props,
                        new javax.mail.Authenticator() {
                            @Override
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(Constante.SMTP_USER, Constante.SMTP_PASSWORD);
                            }
                        });
                session.setDebug(false);

                MimeMessage msg = new MimeMessage(session);
                msg.setText(mail.getHeader().concat(mail.getBody()).concat(mail.getFooter()), "utf-8", "html");
                msg.setSubject(mail.getSubject());
                msg.setFrom(new InternetAddress(Constante.SMTP_USER));
                for (String recipient : mail.getRecipients()) {
                    msg.addRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
                }
                Transport.send(msg);
            }
        } catch (Exception mex) {
            System.out.println("Exception: " + mex.getMessage());
        }
        return true;
    }
}
