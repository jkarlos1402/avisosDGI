package gob.sgi.ejb;

import gob.sgi.model.Notificacion;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class NotificationEditor {

    @PersistenceContext
    private EntityManager em;

    public void setNotificacion(String idNotificacion, String estatus) {
        Notificacion notificacion;
        notificacion = em.find(Notificacion.class, Integer.parseInt(idNotificacion));                
        notificacion.setLeido(new Boolean(estatus));        
        em.merge(notificacion);
    }
}
