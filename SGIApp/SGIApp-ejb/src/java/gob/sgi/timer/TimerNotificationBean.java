/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gob.sgi.timer;

import com.sun.xml.wss.util.DateUtils;
import gob.sgi.constante.Constante;
import gob.sgi.dto.Mail;
import gob.sgi.model.Movbco;
import gob.sgi.model.Psolicitud;
import gob.sgi.model.Relsolbco;
import java.text.SimpleDateFormat;
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
        List<Relsolbco> studies = null;
        List<Movbco> movStudies = null;
        Calendar fechaActual = null;
        Date fechaAComparar = null;
        Query queryPetitions = null;
        Query queryStudies = null;
        Query queryMovStudies = null;
        Mail mail = null;
        //se verifica el periodo para reenviar una solicitud despues de ser revisada por el area
        fechaActual = Calendar.getInstance();
        fechaActual.add(Calendar.DAY_OF_YEAR, ((Constante.VIGENCIA_SOL_OBS * -1) + Constante.DIAS_TO_SEND_NOTIFICACION_SOL));
        fechaAComparar = fechaActual.getTime();
        queryPetitions = em.createQuery("SELECT p FROM Psolicitud p WHERE p.idEdoSol = " + Constante.ESTATUS_SOL_REVISADA + " AND p.fecEval = :fechaGenerada", Psolicitud.class);
        queryPetitions.setParameter("fechaGenerada", fechaAComparar, TemporalType.DATE);
        System.out.println("fechaGenerada para envio de alerta: " + fechaAComparar);
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
        queryPetitions = em.createQuery("SELECT p FROM Psolicitud p WHERE p.idEdoSol = " + Constante.ESTATUS_SOL_REVISADA + " AND p.fecEval = :fechaGenerada", Psolicitud.class);
        queryPetitions.setParameter("fechaGenerada", fechaAComparar, TemporalType.DATE);
        System.out.println("fechaGenerada para cancelar: " + fechaAComparar);
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

        queryStudies = em.createQuery("SELECT e FROM Relsolbco e WHERE e.status = " + Constante.ESTATUS_ES_OBSERVACIONES, Relsolbco.class);
        studies = queryStudies.getResultList();
        for (Relsolbco study : studies) {
            movStudies = null;
            queryMovStudies = null;
            fechaActual = Calendar.getInstance();
            fechaActual.add(Calendar.DAY_OF_YEAR, ((Constante.VIGENCIA_ES_OBS * -1) + Constante.DIAS_TO_SEND_NOTIFICACION_ES));
            fechaAComparar = fechaActual.getTime();
            queryMovStudies = em.createQuery("SELECT m FROM Movbco m WHERE m.idBco = " + study.getIdBco() + " AND m.tipMov = " + Constante.ESTATUS_ES_OBSERVACIONES + " ORDER BY m.fecMov DESC");
            movStudies = queryMovStudies.setMaxResults(1).getResultList();
            if (movStudies.size() > 0) {
                em.refresh(movStudies.get(0));
                System.out.println("fecha del es: " + movStudies.get(0).getFecMov() + " del: " + movStudies.get(0).getIdmovbco());
                System.out.println("fecha a comparar: " + fechaAComparar);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
                    System.out.println("estudio encontrado: " + movStudies.get(0).getIdmovbco());
                    mail = null;
                    try (QueueConnection connection = connectionFactory.createQueueConnection()) {
                        QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                        QueueSender queueSender = queueSession.createSender(null);
                        mail = new Mail();
                        mail.setIdBco(study.getIdBco() + "");
                        mail.setIdUsuario(em.find(Psolicitud.class, study.getIdSol()).getIdUsu() + "");
                        mail.setIdRolUsu(Constante.ROL_SISTEMA);
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
                //comparar para cancelar estudio socioeconomico
                fechaActual = Calendar.getInstance();
                fechaActual.add(Calendar.DAY_OF_YEAR, ((Constante.VIGENCIA_ES_OBS+1) * -1));
                fechaAComparar = fechaActual.getTime();
                System.out.println("fecha a comparar para cancelar: " + fechaAComparar);
                if (sdf.format(movStudies.get(0).getFecMov()).equals(sdf.format(fechaAComparar))) {
                    System.out.println("estudio a cancelar encontrado: " + movStudies.get(0).getIdmovbco());
                    mail = null;
                    try (QueueConnection connection = connectionFactory.createQueueConnection()) {
                        QueueSession queueSession = connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
                        QueueSender queueSender = queueSession.createSender(null);
                        mail = new Mail();
                        mail.setIdBco(study.getIdBco() + "");
                        mail.setIdUsuario(em.find(Psolicitud.class, study.getIdSol()).getIdUsu() + "");
                        mail.setIdRolUsu(Constante.ROL_SISTEMA);
                        mail.setEstatusBco(Constante.ESTATUS_SOL_CANCELADA);
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

        System.out.println("Timer event: " + new Date());
    }

}
