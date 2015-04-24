package gob.sgi.ws;

import gob.sgi.model.Mail;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;

/**
 * Servicio web encargado de enviar un mensaje a una cola llamada QueueMail se
 * envia la información necesaria para enviar un e-mail
 *
 * @author Juan Carlos Piña Moreno
 */
@Path("/servicio")
public class MailWS {

    @Resource(mappedName = "jms/QueueConnectionFactory")
    private QueueConnectionFactory connectionFactory;

    @Resource(mappedName = "QueueMail")
    private Queue mailQueue;

    @POST
    public Response placeMailToQueue(@FormParam("idSol") String idSol, @FormParam("idUsu") String idUsu,
            @FormParam("estatusSol") String estatusSol, @FormParam("idObr") String idObr,
            @FormParam("idRolUsu") String idRolUsu, @FormParam("idBco") String idBco) {
        try {
            //requerimientos para poder enviar un mensage
            try (QueueConnection connection = connectionFactory.createQueueConnection()) {
                QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                QueueSender queueSender = queueSession.createSender(null);

                // se crea el objeto que sera enviado como mensaje
                Mail mail = new Mail();
                mail.setIdSolicitud(idSol);
                mail.setIdUsuario(idUsu);
                mail.setEstatusSolicitud(estatusSol);
                mail.setIdObra(idObr);
                mail.setIdRolUsu(idRolUsu);
                //se creo el mensaje
                ObjectMessage message = queueSession.createObjectMessage(mail);
                //se envia el mensaje
                queueSender.send(mailQueue, message);
                //se cierra la conexion
                connection.close();
            }
        } catch (JMSException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "POST, GET, PUT, UPDATE, OPTIONS")
                .header("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With").build();
    }

}
