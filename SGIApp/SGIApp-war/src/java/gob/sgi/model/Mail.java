package gob.sgi.model;

import java.io.Serializable;
import java.util.List;

/**
 * Pojo de la estructura de un email
 *
 * @author Juan Carlos Pi�a Moreno
 */
public class Mail implements Serializable {

    private String header;
    private String body;
    private String footer;
    private String subject;
    private List<String> recipients;
    private List<String> cc;
    private List<String> cco;
    private String idSolicitud;
    private String idUsuario;
    private String idRolUsu;
    private String tipoSolicitud;
    private String estatusSolicitud;
    private String idObra;
    private String idBco;

    public static final String HEADER_PATH = "c:/www/sgi/resources/header.html";
    public static final String BODY_PATH = "c:/www/sgi/resources/body.html";
    public static final String FOOTER_PATH = "c:/www/sgi/resources/footer.html";
    public static final String SMTP_HOST = "smtp.office365.com";
    public static final String SMTP_PORT = "587";
    public static final String SMTP_USER = "soporte.sspp@edomex.gob.mx";
    public static final String SMTP_PASSWORD = "Soporte01";

    public String getIdBco() {
        return idBco;
    }

    public void setIdBco(String idBco) {
        this.idBco = idBco;
    }

    public String getIdRolUsu() {
        return idRolUsu;
    }

    public void setIdRolUsu(String idRolUsu) {
        this.idRolUsu = idRolUsu;
    }

    public String getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(String idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTipoSolicitud() {
        return tipoSolicitud;
    }

    public void setTipoSolicitud(String tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public String getEstatusSolicitud() {
        return estatusSolicitud;
    }

    public void setEstatusSolicitud(String estatusSolicitud) {
        this.estatusSolicitud = estatusSolicitud;
    }

    public String getIdObra() {
        return idObra;
    }

    public void setIdObra(String idObra) {
        this.idObra = idObra;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public List<String> getCc() {
        return cc;
    }

    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    public List<String> getCco() {
        return cco;
    }

    public void setCco(List<String> cco) {
        this.cco = cco;
    }

}
