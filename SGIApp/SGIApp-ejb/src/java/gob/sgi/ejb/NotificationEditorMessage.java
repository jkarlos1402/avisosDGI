package gob.sgi.ejb;

import java.util.List;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.EJB;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

/**
 *
 * @author intel core i 7
 */
@MessageDriven(activationConfig = {
    @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "QueueSetNotification")
})
public class NotificationEditorMessage implements MessageListener {

    @EJB
    private NotificationEditor editor;

    @Override
    public void onMessage(Message message) {
        ObjectMessage objectMessage = null;
        List info;
        if (message instanceof ObjectMessage) {
            objectMessage = (ObjectMessage) message;
        }

        try {
            info = (List<String>) objectMessage.getObject();            
            editor.setNotificacion(info.get(0).toString(), info.get(1).toString());

        } catch (JMSException ex) {
            System.out.println("JMSException: " + ex.getMessage());
        }
    }

}
