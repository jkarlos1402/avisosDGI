package gob.sgi.ejb;

import gob.sgi.model.Constante;
import gob.sgi.model.Mail;
import gob.sgi.model.Notificacion;
import java.util.List;
import java.util.Calendar;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class NotificationSender {

    @PersistenceContext
    private EntityManager em;

    public Boolean sendNotification(Mail mail, List<String> idUsuarioDestino) {
        switch (mail.getIdRolUsu()) {
            case Constante.ROL_DEPENDENCIA://dependencia es la que genera la notificacion
                Notificacion notificacion;
                if (mail.getEstatusSolicitud().equals(Constante.ESTATUS_SOL_ENVIADA)) {
                    for (String idUsuario : idUsuarioDestino) {
                        notificacion = new Notificacion();
                        notificacion.setFechaNotificacion(Calendar.getInstance().getTime());
                        notificacion.setIdUsu(Integer.parseInt(idUsuario));
                        notificacion.setLeido(false);
                        notificacion.setMensaje("La solicitud con n\u00famero de identificaci\u00f3n: " + mail.getIdSolicitud() + " ah sido ENVIADA para su revisi\u00f3n; procedencia: " + mail.getUnidadEjecutora());
                        em.persist(notificacion);
                    }
                } else if (mail.getEstatusBco().equals(Constante.ESTATUS_ES_ENVIADO)) {
                    for (String idUsuario : idUsuarioDestino) {
                        notificacion = new Notificacion();
                        notificacion.setFechaNotificacion(Calendar.getInstance().getTime());
                        notificacion.setIdUsu(Integer.parseInt(idUsuario));
                        notificacion.setLeido(false);
                        notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: " + mail.getIdBco() + " ah sido ENVIADA para su revisi\u00f3n; procedencia: " + mail.getUnidadEjecutora());
                        em.persist(notificacion);
                    }
                }
                break;
            case Constante.ROL_BANCO: // banco es la que genera la notificacion
                switch (mail.getEstatusBco()) {
                    case Constante.ESTATUS_ES_INGRESADO:
                        for (String idUsuario : idUsuarioDestino) {
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(Calendar.getInstance().getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: " + mail.getIdBco() + " ah sido INGRESADO f\u00edsicamente a la DGI para su DICTAMINACI\u00d3N");
                            em.persist(notificacion);
                        }
                        break;
                    case Constante.ESTATUS_ES_OBSERVACIONES:
                        for (String idUsuario : idUsuarioDestino) {
                            Calendar fechaEnvio = Calendar.getInstance();
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(fechaEnvio.getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: " + mail.getIdBco() + " ah sido regresado con OBSERVACIONES, favor de corregirlas y env\u00edar nuevamente el estudio");
                            fechaEnvio.add(Calendar.DAY_OF_YEAR, 180);// cambiar 180 por una constante (regla de negocio)
                            notificacion.setVigencia(fechaEnvio.getTime());
                            em.persist(notificacion);
                        }
                        break;
                    case Constante.ESTATUS_ES_DICTAMINADO:
                        for (String idUsuario : idUsuarioDestino) {
                            Calendar fechaEnvio = Calendar.getInstance();
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(fechaEnvio.getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: " + mail.getIdBco() + " ah sido DICTAMINADO");
                            em.persist(notificacion);
                        }
                        break;
                }
                break;
            case Constante.ROL_AREA:// el area es la que genera la notificacion
                if (mail.getEstatusSolicitud().equals(Constante.ESTATUS_SOL_REVISADA)) {
                    for (String idUsuario : idUsuarioDestino) {
                        Calendar fechaEnvio = Calendar.getInstance();
                        notificacion = new Notificacion();
                        notificacion.setFechaNotificacion(fechaEnvio.getTime());
                        notificacion.setIdUsu(Integer.parseInt(idUsuario));
                        notificacion.setLeido(false);
                        notificacion.setMensaje("La solicitud con n\u00famero de identificaci\u00f3n: " + mail.getIdSolicitud() + " ah sido regresada con OBSERVACIONES, favor de corregirlas y env\u00edar nuevamente la solicitud");
                        fechaEnvio.add(Calendar.DAY_OF_YEAR, 180);// cambiar 180 por una constante (regla de negocio)
                        notificacion.setVigencia(fechaEnvio.getTime());
                        em.persist(notificacion);
                    }
                } else if (mail.getEstatusSolicitud().equals(Constante.ESTATUS_SOL_ACEPTADA)) {
                    for (String idUsuario : idUsuarioDestino) {
                        Calendar fechaEnvio = Calendar.getInstance();
                        notificacion = new Notificacion();
                        notificacion.setFechaNotificacion(fechaEnvio.getTime());
                        notificacion.setIdUsu(Integer.parseInt(idUsuario));
                        notificacion.setLeido(false);
                        notificacion.setMensaje("La solicitud con n\u00famero de identificaci\u00f3n: " + mail.getIdSolicitud() + " ah sido ACEPTADA");
                        em.persist(notificacion);
                    }
                } else if (!mail.getIdObra().equals("")) {
                    for (String idUsuario : idUsuarioDestino) {
                        Calendar fechaEnvio = Calendar.getInstance();
                        notificacion = new Notificacion();
                        notificacion.setFechaNotificacion(fechaEnvio.getTime());
                        notificacion.setIdUsu(Integer.parseInt(idUsuario));
                        notificacion.setLeido(false);
                        notificacion.setMensaje("Se ha CREADO la OBRA con n\u00famero de identificaci\u00f3n: " + mail.getIdObra() + ", correspondiente a la solicitud con n\u00famero de identificaci\u00f3n: " + mail.getIdSolicitud());
                        em.persist(notificacion);
                    }
                }
                break;
            case Constante.ROL_VENTANILLA:// ventanilla es la que genera la notificacion
                if (!mail.getIdSolicitud().equals("")) {
                    for (String idUsuario : idUsuarioDestino) {
                        Calendar fechaEnvio = Calendar.getInstance();
                        notificacion = new Notificacion();
                        notificacion.setFechaNotificacion(fechaEnvio.getTime());
                        notificacion.setIdUsu(Integer.parseInt(idUsuario));
                        notificacion.setLeido(false);
                        notificacion.setMensaje("Se ha INGRESADO en ventanilla la solicitud con n\u00famero de identificaci\u00f3n: " + mail.getIdSolicitud());
                        em.persist(notificacion);
                    }
                }
                break;
        }
        return null;
    }

}
