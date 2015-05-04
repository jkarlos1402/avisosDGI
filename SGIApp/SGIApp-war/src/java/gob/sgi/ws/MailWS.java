package gob.sgi.ws;

import gob.sgi.constante.Constante;
import gob.sgi.model.Mail;
import java.util.ArrayList;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Servicio web encargado de enviar un mensaje a una cola llamada QueueMail se
 * envia la información necesaria para enviar un e-mail
 *
 * @author Juan Carlos Piña Moreno
 */
@Path("/notificacion")
public class MailWS {

    @Resource(mappedName = "jms/QueueConnectionFactory")
    private QueueConnectionFactory connectionFactory;

    @Resource(mappedName = "QueueMail")
    private Queue mailQueue;

    @Resource(mappedName = "QueueSetNotification")
    private Queue notificationQueue;

    @POST
    public Response placeMailToQueue(@FormParam("idSol") final String idSol, @FormParam("idUsu") final String idUsu,
            @FormParam("estatusSol") final String estatusSol, @FormParam("idObr") final String idObr,
            @FormParam("idRolUsu") final String idRolUsu, @FormParam("idBco") final String idBco, @FormParam("estatusBco") final String estatusBco) {
        try {
            //requerimientos para poder enviar un mensage a la cola
            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                QueueSender queueSender = queueSession.createSender(null);

                // se crea el objeto que sera enviado como mensaje
                Mail mail = new Mail();
                mail.setIdSolicitud(idSol != null ? idSol : "");
                mail.setIdUsuario(idUsu != null ? idUsu : "");
                mail.setEstatusSolicitud(estatusSol != null ? estatusSol : "");
                mail.setIdObra(idObr != null ? idObr : "");
                mail.setIdRolUsu(idRolUsu != null ? idRolUsu : "");
                mail.setEstatusBco(estatusBco != null ? estatusBco : "");
                mail.setSubject("Aviso de la DGI");
                //se crea el mensaje
                ObjectMessage message = queueSession.createObjectMessage(mail);
                //se envia el mensaje
                queueSender.send(mailQueue, message);
                //se cierra la conexion
                connection.close();
            }
        } catch (JMSException ex) {
            System.out.println("JMSException: " + ex.getMessage());
        }
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With").build();
    }

    @Path("/setNotification")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    public Response setNotificacion(@FormParam("idNotificacion") String idNotificacion, @FormParam("estatus") String estatus) {                
        JsonObject jsonObject = null;
        try (QueueConnection connection = connectionFactory.createQueueConnection()) {
            QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueSender queueSender = queueSession.createSender(null);
            ArrayList valores = new ArrayList<>();
            valores.add(idNotificacion);
            valores.add(estatus);            
            //se crea el mensaje
            ObjectMessage message = queueSession.createObjectMessage(valores);
            //se envia el mensaje
            queueSender.send(notificationQueue, message);
                       
            jsonObject = Json.createObjectBuilder().add("respuesta", "Notificaci\u00f3n actualizada").build();
            //se cierra la conexion
            connection.close();
        } catch (JMSException ex) {
            System.out.println("JMSException: " + ex.getMessage());
        } catch (NumberFormatException ne) {
            System.out.println("NumberFormatException: " + ne.getMessage());
        }      
        
        return Response.ok(jsonObject.toString())
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With").build();        
    }

}
