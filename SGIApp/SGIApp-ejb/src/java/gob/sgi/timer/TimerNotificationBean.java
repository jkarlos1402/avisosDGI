/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.sgi.timer;

import gob.sgi.constante.Constante;
import gob.sgi.dto.Mail;
import gob.sgi.model.Psolicitud;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 *
 * @author intel core i 7
 */
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

    @Schedule(dayOfWeek = "Mon-Fri", month = "*", hour = "*", dayOfMonth = "*", year = "*", minute = "*", second = "0")
    public void checkPetitionsAndStudies() {
        List<Psolicitud> petitions = null;
        Calendar fechaActual = null;
        //se verifica el periodo para reenviar una solicitud despues de ser revisada por el area
        fechaActual = Calendar.getInstance();
        fechaActual.add(Calendar.DAY_OF_YEAR, ((Constante.VIGENCIA_SOL_OBS * -1) + Constante.DIAS_TO_SEND_NOTIFICACION_SOL));
        Date fechaAComparar = fechaActual.getTime();
        Query queryPetitions = em.createQuery("SELECT p FROM Psolicitud p WHERE p.idEdoSol = 5 AND p.fecEval = :fechaGenerada", Psolicitud.class);
        queryPetitions.setParameter("fechaGenerada", fechaAComparar, TemporalType.DATE);
        System.out.println("fechaGenerada: " + fechaAComparar);
        petitions = queryPetitions.getResultList();
        Mail mail = null;
        try (QueueConnection connection = connectionFactory.createQueueConnection()) {
            QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            QueueSender queueSender = queueSession.createSender(null);
            for (Psolicitud petition : petitions) {
                mail = new Mail();
                mail.setIdSolicitud(petition.getIdSol() + "");
                mail.setIdUsuario(petition.getIdUsu() + "");
                mail.setIdRolUsu("0");
                mail.setSubject("Aviso de la DGI");
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

        System.out.println("Timer event: " + new Date());
    }

}
