package gob.sgi.ejb;

import gob.sgi.constante.Constante;
import gob.sgi.dto.Mail;
import gob.sgi.model.ConnectionManager;
import gob.sgi.model.Notificacion;
import gob.sgi.model.Plazo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

@Stateless
@LocalBean
public class NotificationSender {

    public Boolean sendNotification(Mail mail, List<String> idUsuarioDestino) {
        ConnectionManager cm = new ConnectionManager();
        Connection connection = null;
        PreparedStatement statement = null;
        int rs;
        List<Notificacion> notificaciones = new ArrayList<>();
        if (!idUsuarioDestino.isEmpty()) {
            switch (mail.getIdRolUsu()) {
                case Constante.ROL_DEPENDENCIA://dependencia es la que genera la notificacion
                    Notificacion notificacion;
                    if (mail.getEstatusSolicitud().equals(Constante.ESTATUS_SOL_ENVIADA)) {
                        for (String idUsuario : idUsuarioDestino) {
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(Calendar.getInstance().getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("La solicitud con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdSolicitud().trim() + "</b> ha sido <b>ENVIADA</b> para su revisi\u00f3n; procedencia: <b>" + mail.getUnidadEjecutora() + "</b>, cuenta con <b>" + Constante.VIGENCIA_SOL_OBS + " d\u00edas h\u00e1biles</b> para su an\u00e1lisis");
                            notificacion.setVigencia(Plazo.calculaFuturaFechaPlazo(Constante.VIGENCIA_SOL_OBS));
                            notificaciones.add(notificacion);
                        }
                    } else if (mail.getEstatusSolicitud().equals(Constante.ESTATUS_SOL_INGRESADA)) {
                        for (String idUsuario : idUsuarioDestino) {
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(Calendar.getInstance().getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("La solicitud con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdSolicitud().trim() + "</b> presentaba observaciones menores y ha sido <b>ENVIADA</b> para su revisi\u00f3n; procedencia: <b>" + mail.getUnidadEjecutora() + "</b>, cuenta con <b>" + Constante.VIGENCIA_SOL_OBS + " d\u00edas h\u00e1biles</b> para su an\u00e1lisis");
                            notificacion.setVigencia(Plazo.calculaFuturaFechaPlazo(Constante.VIGENCIA_SOL_OBS));
                            notificaciones.add(notificacion);
                        }
                    } else if (mail.getEstatusBco().equals(Constante.ESTATUS_ES_ENVIADO)) {
                        for (String idUsuario : idUsuarioDestino) {
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(Calendar.getInstance().getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdBco() + "</b> ha sido <b>ENVIADA</b> para su revisi\u00f3n; procedencia: <b>" + mail.getUnidadEjecutora() + "</b>");
                            notificaciones.add(notificacion);
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
                                if (mail.getMontoBco() < 50000000.00f) {
                                    notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdBco() + "</b> ha sido <b>INGRESADO</b> f\u00edsicamente a la DGI para su <b>DICTAMINACI\u00d3N</b>, se cuenta con <b>" + Constante.VIGENCIA_ES_OBS_1 + " d\u00edas h\u00e1biles</b> para su an\u00e1lisis");
                                    notificacion.setVigencia(Plazo.calculaFuturaFechaPlazo(Constante.VIGENCIA_ES_OBS_1));
                                } else if (mail.getMontoBco() >= 50000000.00f && mail.getMontoBco() <= 500000000.00f) {
                                    notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdBco() + "</b> ha sido <b>INGRESADO</b> f\u00edsicamente a la DGI para su <b>DICTAMINACI\u00d3N</b>, se cuenta con <b>" + Constante.VIGENCIA_ES_OBS_2 + " d\u00edas h\u00e1biles</b> para su an\u00e1lisis");
                                    notificacion.setVigencia(Plazo.calculaFuturaFechaPlazo(Constante.VIGENCIA_ES_OBS_2));
                                } else if (mail.getMontoBco() > 500000000.00f) {
                                    notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdBco() + "</b> ha sido <b>INGRESADO</b> f\u00edsicamente a la DGI para su <b>DICTAMINACI\u00d3N</b>, se cuenta con <b>" + Constante.VIGENCIA_ES_OBS_3 + " d\u00edas h\u00e1biles</b> para su an\u00e1lisis");
                                    notificacion.setVigencia(Plazo.calculaFuturaFechaPlazo(Constante.VIGENCIA_ES_OBS_3));
                                }
                                notificaciones.add(notificacion);
                            }
                            break;
                        case Constante.ESTATUS_ES_OBSERVACIONES:
                            for (String idUsuario : idUsuarioDestino) {
                                notificacion = new Notificacion();
                                notificacion.setFechaNotificacion(Calendar.getInstance().getTime());
                                notificacion.setIdUsu(Integer.parseInt(idUsuario));
                                notificacion.setLeido(false);
                                if (mail.getMontoBco() < 50000000.00f) {
                                    notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdBco() + "</b> ha sido regresado con <b>OBSERVACIONES</b>, tiene <b>" + Constante.VIGENCIA_ES_OBS_1 + " d\u00edas h\u00e1biles</b> para corregirlas y env\u00edar nuevamente el estudio");
                                    notificacion.setVigencia(Plazo.calculaFuturaFechaPlazo(Constante.VIGENCIA_ES_OBS_1));
                                } else if (mail.getMontoBco() >= 50000000.00f && mail.getMontoBco() <= 500000000.00f) {
                                    notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdBco() + "</b> ha sido regresado con <b>OBSERVACIONES</b>, tiene <b>" + Constante.VIGENCIA_ES_OBS_2 + " d\u00edas h\u00e1biles</b> para corregirlas y env\u00edar nuevamente el estudio");
                                    notificacion.setVigencia(Plazo.calculaFuturaFechaPlazo(Constante.VIGENCIA_ES_OBS_2));
                                } else if (mail.getMontoBco() > 500000000.00f) {
                                    notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdBco() + "</b> ha sido regresado con <b>OBSERVACIONES</b>, tiene <b>" + Constante.VIGENCIA_ES_OBS_3 + " d\u00edas h\u00e1biles</b> para corregirlas y env\u00edar nuevamente el estudio");
                                    notificacion.setVigencia(Plazo.calculaFuturaFechaPlazo(Constante.VIGENCIA_ES_OBS_3));
                                }
                                notificaciones.add(notificacion);
                            }
                            break;
                        case Constante.ESTATUS_ES_DICTAMINADO:
                            for (String idUsuario : idUsuarioDestino) {
                                Calendar fechaEnvio = Calendar.getInstance();
                                notificacion = new Notificacion();
                                notificacion.setFechaNotificacion(fechaEnvio.getTime());
                                notificacion.setIdUsu(Integer.parseInt(idUsuario));
                                notificacion.setLeido(false);
                                notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdBco() + "</b> ha sido <b>DICTAMINADO</b> y ha obtenido el n\u00famero de dictamen: <b>" + mail.getNumDictamen() + "</b>");
                                notificaciones.add(notificacion);
                            }
                            break;
                    }
                    break;
                case Constante.ROL_AREA:// el area es la que genera la notificacion
                    if (mail.getEstatusSolicitud().equals(Constante.ESTATUS_SOL_REVISADA)) {
                        for (String idUsuario : idUsuarioDestino) {
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(Calendar.getInstance().getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("La solicitud con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdSolicitud().trim() + "</b> ha sido regresada con <b>OBSERVACIONES</b>, cuenta con <b>" + Constante.VIGENCIA_SOL_OBS + " d\u00edas h\u00e1biles</b> para corregirlas y env\u00edar nuevamente la solicitud");
                            notificacion.setVigencia(Plazo.calculaFuturaFechaPlazo(Constante.VIGENCIA_SOL_OBS));
                            notificaciones.add(notificacion);
                        }
                    } else if (mail.getEstatusSolicitud().equals(Constante.ESTATUS_SOL_ACEPTADA)) {
                        for (String idUsuario : idUsuarioDestino) {
                            Calendar fechaEnvio = Calendar.getInstance();
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(fechaEnvio.getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("La solicitud con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdSolicitud().trim() + "</b> ha sido <b>ACEPTADA</b>");
                            notificaciones.add(notificacion);
                        }
                    } else if (!mail.getIdObra().equals("")) {
                        for (String idUsuario : idUsuarioDestino) {
                            Calendar fechaEnvio = Calendar.getInstance();
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(fechaEnvio.getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("Se ha <b>CREADO</b> la <b>OBRA</b> con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdObra() + "</b>, correspondiente a la solicitud con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdSolicitud().trim() + "</b>");
                            notificaciones.add(notificacion);
                        }
                    }
                    break;
                case Constante.ROL_VENTANILLA:// ventanilla es la que genera la notificacion
                    if (!mail.getIdSolicitud().trim().equals("")) {
                        for (String idUsuario : idUsuarioDestino) {
                            Calendar fechaEnvio = Calendar.getInstance();
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(fechaEnvio.getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("Se ha <b>INGRESADO</b> en ventanilla la solicitud con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdSolicitud().trim() + "</b>");
                            notificaciones.add(notificacion);
                        }
                    }
                    break;
                case Constante.ROL_SISTEMA:
                    if (mail.getEstatusSolicitud() == null && mail.getIdSolicitud() != null && !mail.getIdSolicitud().trim().equals("")) {
                        for (String idUsuario : idUsuarioDestino) {
                            Calendar fechaEnvio = Calendar.getInstance();
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(fechaEnvio.getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("La solicitud con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdSolicitud().trim() + "</b>, no presenta actividad, si en los pr\u00f3ximos <b>" + mail.getDiasParaNoticifacion() + "</b> d\u00edas no es enviada a revisi\u00f3n ser\u00e1 <b>CANCELADA</b>.");
                            notificaciones.add(notificacion);
                        }
                    } else if (mail.getIdSolicitud() != null && !mail.getIdSolicitud().trim().equals("") && mail.getEstatusSolicitud().equals(Constante.ESTATUS_SOL_CANCELADA)) {
                        for (String idUsuario : idUsuarioDestino) {
                            Calendar fechaEnvio = Calendar.getInstance();
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(fechaEnvio.getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("La solicitud con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdSolicitud().trim() + "</b>, ha sido <b>CANCELADA</b> por falta de actividad");
                            notificaciones.add(notificacion);
                        }
                    } else if (mail.getEstatusBco() == null && mail.getIdBco() != null && !mail.getIdBco().equals("")) {
                        for (String idUsuario : idUsuarioDestino) {
                            Calendar fechaEnvio = Calendar.getInstance();
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(fechaEnvio.getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdBco() + "</b>, no presenta actividad, si en los pr\u00f3ximos <b>" + mail.getDiasParaNoticifacion() + "</b> d\u00edas no es enviado a revisi\u00f3n ser\u00e1 <b>CANCELADO</b>.");
                            notificaciones.add(notificacion);
                        }
                    } else if (mail.getIdBco() != null && !mail.getIdBco().equals("") && mail.getEstatusBco().equals(Constante.ESTATUS_ES_CANCELADO)) {
                        for (String idUsuario : idUsuarioDestino) {
                            Calendar fechaEnvio = Calendar.getInstance();
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(fechaEnvio.getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdBco() + "</b>, ha sido <b>CANCELADO</b> por falta de actividad");
                            notificaciones.add(notificacion);
                        }
                    } else if (mail.getIdBco() != null && !mail.getIdBco().equals("") && mail.getEstatusBco() == null && mail.getIdUsuario() == null) {
                        for (String idUsuario : idUsuarioDestino) {
                            Calendar fechaEnvio = Calendar.getInstance();
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(fechaEnvio.getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdBco() + "</b>, a\u00fan <b>NO</b> se ha <b>DICTAMINADO</b>, cuenta con <b>" + mail.getDiasParaNoticifacion() + "</b> d\u00edas para revisarlo.");
                            notificaciones.add(notificacion);
                        }
                    } else if (mail.getIdBco() != null && !mail.getIdBco().equals("") && mail.getEstatusBco().equals(Constante.ESTATUS_ES_VENCIDO)) {
                        for (String idUsuario : idUsuarioDestino) {
                            Calendar fechaEnvio = Calendar.getInstance();
                            notificacion = new Notificacion();
                            notificacion.setFechaNotificacion(fechaEnvio.getTime());
                            notificacion.setIdUsu(Integer.parseInt(idUsuario));
                            notificacion.setLeido(false);
                            notificacion.setMensaje("El estudio socioecon\u00f3mico con n\u00famero de identificaci\u00f3n: <b>" + mail.getIdBco() + "</b>, se ha <b>CANCELADO</b> ya que no se ha dictaminado o regresado con observaciones");
                            notificaciones.add(notificacion);
                        }
                    }
                    break;
            }
            for (Notificacion notificacion : notificaciones) {
                try {
                    connection = cm.conectar();
                    statement = connection.prepareStatement("INSERT INTO notificacion(fechaNotificacion,mensaje,vigencia,leido,idUsu) values(?,?,?,?,?)");
                    statement.setDate(1, new java.sql.Date(notificacion.getFechaNotificacion().getTime()));
                    statement.setString(2, notificacion.getMensaje());
                    statement.setDate(3, notificacion.getVigencia() != null ? new java.sql.Date(notificacion.getVigencia().getTime()) : null);
                    statement.setBoolean(4, notificacion.getLeido());
                    statement.setInt(5, notificacion.getIdUsu());
                    rs = statement.executeUpdate();
                } catch (SQLException ex) {
                    System.out.println("SQLException notificationSender.java: " + ex.getMessage());
                } finally {
                    if (statement != null) {
                        try {
                            statement.close();
                        } catch (SQLException ex) {
                            System.out.println("SQLException al cerrar statement: " + ex.getMessage());
                        }
                    }
                    if (connection != null) {
                        cm.desconectar(connection);
                    }
                }
            }
        }
        return true;
    }
}
