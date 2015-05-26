package gob.sgi.constante;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Constante {

    //palabras reservadas para el texto de los emails
    public static final String STR_ID_BANCO;
    public static final String STR_ID_SOLICITUD;
    public static final String STR_ID_OBRA;
    public static final String STR_ESTATUS_SOL;
    public static final String STR_ESTATUS_ES;
    public static final String STR_NOM_UE;
    public static final String STR_DIR_SGI;
    public static final String STR_DIAS_CANCELACION;

    //ruta de la carpeta de recursos
    public static final String FILES_HTML_PATH = "C:/DGI/resources";
    public static final String FILE_CONF_PATH = "C:/DGI/conf";

    //serie de rutas para los email
    public static final String HEADER_PATH;
    public static final String SOL_ENV_BODY_PATH;
    public static final String SOL_OBS_BODY_PATH;
    public static final String SOL_ACEPT_BODY_PATH;
    public static final String SOL_INGRESO_BODY_PATH;
    public static final String ES_ENV_BODY_PATH;
    public static final String ES_INGRESO_BODY_PATH;
    public static final String ES_OBS_BODY_PATH;
    public static final String ES_DICT_BODY_PATH;
    public static final String OBR_CREADA_BODY_PATH;
    public static final String FOOTER_PATH;
    public static final String SOL_NO_ACTIVIDAD_BODY_PATH;
    public static final String SOL_CANCELADA_BODY_PATH;
    public static final String ES_NO_ACTIVIDAD_BODY_PATH;
    public static final String ES_CANCELADO_BODY_PATH;
    public static final String ES_NO_ACTIVIDAD_DICT_BODY_PATH;
    public static final String ES_VENCIDO_BODY_PATH;

    //configuracion del servidor smtp
    public static final String SMTP_HOST;
    public static final String SMTP_PORT;
    public static final String SMTP_USER;
    public static final String SMTP_PASSWORD;

    //roles de usuario
    public static final String ROL_SISTEMA = "0";
    public static final String ROL_DEPENDENCIA = "1";
    public static final String ROL_BANCO = "2";
    public static final String ROL_AREA = "3";
    public static final String ROL_VENTANILLA = "4";

    //estatus de solicitud    
    public static final String ESTATUS_SOL_ENVIADA = "3";
    public static final String ESTATUS_SOL_INGRESADA = "4";
    public static final String ESTATUS_SOL_REVISADA = "5";
    public static final String ESTATUS_SOL_ACEPTADA = "6";
    public static final String ESTATUS_SOL_CANCELADA = "7";

    // estaus de estudio socioeconomico
    public static final String ESTATUS_ES_CREADO = "1";
    public static final String ESTATUS_ES_ENVIADO = "3";
    public static final String ESTATUS_ES_INGRESADO = "4";
    public static final String ESTATUS_ES_REVISADO = "5";
    public static final String ESTATUS_ES_OBSERVACIONES = "2";
    public static final String ESTATUS_ES_DICTAMINADO = "6";
    public static final String ESTATUS_ES_CANCELADO = "7";
    public static final String ESTATUS_ES_VENCIDO = "0";
    public static final String ESTATUS_ES_BLOQUEADO;

    //direccion web del sgi
    public static final String DIR_SGI;

    // periodo de fechas para las solicitudes
    public static final Integer VIGENCIA_SOL_OBS;
    public static final String DIAS_TO_SEND_NOTIFICACION_SOL;

    // periodo de fechas para los estudios
    public static final Integer VIGENCIA_ES_OBS;
    public static final String DIAS_TO_SEND_NOTIFICACION_ES;
    public static final Integer VIGENCIA_ES_DICTAMINACION;
    public static final String DIAS_TO_SEND_NOTIFICACION_DICT_ES;

    //vigencia de las notificaciones
    public static final Integer VIGENCIA_NOTIFICACIONES;
    
    //horario para verificar solicitudes, estudios y notificaciones
    public static final String ANHO="*";
    public static final String MES_DEL_ANHO="*"; 
    public static final String DIAS_DEL_MES="*";
    public static final String DIA_DE_LA_SEMANA="*";
    public static final String HORAS_DEL_DIA="*"; 
    public static final String MINUTOS_DE_LA_HORA="*"; 
    
    static {
        Properties propiedades = new Properties();

        try {
            propiedades.load(new FileInputStream(FILE_CONF_PATH + "/constantes.properties"));
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException: " + ex.getMessage());
        }
        // Se obtienen los valores del archivo constantes.properites
        STR_ID_BANCO = propiedades.getProperty("STR_ID_BANCO") != null ? propiedades.getProperty("STR_ID_BANCO") : "";
        STR_ID_SOLICITUD = propiedades.getProperty("STR_ID_SOLICITUD") != null ? propiedades.getProperty("STR_ID_SOLICITUD") : "";
        STR_ID_OBRA = propiedades.getProperty("STR_ID_OBRA") != null ? propiedades.getProperty("STR_ID_OBRA") : "";
        STR_ESTATUS_SOL = propiedades.getProperty("STR_ESTATUS_SOL") != null ? propiedades.getProperty("STR_ESTATUS_SOL") : "";
        STR_ESTATUS_ES = propiedades.getProperty("STR_ESTATUS_ES") != null ? propiedades.getProperty("STR_ESTATUS_ES") : "";
        STR_NOM_UE = propiedades.getProperty("STR_NOM_UE") != null ? propiedades.getProperty("STR_NOM_UE") : "";
        STR_DIR_SGI = propiedades.getProperty("STR_DIR_SGI") != null ? propiedades.getProperty("STR_DIR_SGI") : "";
        STR_DIAS_CANCELACION = propiedades.getProperty("STR_DIAS_CANCELACION") != null ? propiedades.getProperty("STR_DIAS_CANCELACION") : "";

        HEADER_PATH = propiedades.getProperty("HEADER_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("HEADER_PATH") : "";
        SOL_ENV_BODY_PATH = propiedades.getProperty("SOL_ENV_BODY_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("SOL_ENV_BODY_PATH") : "";
        SOL_OBS_BODY_PATH = propiedades.getProperty("SOL_OBS_BODY_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("SOL_OBS_BODY_PATH") : "";
        SOL_ACEPT_BODY_PATH = propiedades.getProperty("SOL_ACEPT_BODY_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("SOL_ACEPT_BODY_PATH") : "";
        SOL_INGRESO_BODY_PATH = propiedades.getProperty("SOL_INGRESO_BODY_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("SOL_INGRESO_BODY_PATH") : "";
        SOL_CANCELADA_BODY_PATH = propiedades.getProperty("SOL_CANCELADA_BODY_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("SOL_CANCELADA_BODY_PATH") : "";
        SOL_NO_ACTIVIDAD_BODY_PATH = propiedades.getProperty("SOL_NO_ACTIVIDAD_BODY_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("SOL_NO_ACTIVIDAD_BODY_PATH") : "";
        ES_ENV_BODY_PATH = propiedades.getProperty("ES_ENV_BODY_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("ES_ENV_BODY_PATH") : "";
        ES_INGRESO_BODY_PATH = propiedades.getProperty("ES_INGRESO_BODY_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("ES_INGRESO_BODY_PATH") : "";
        ES_OBS_BODY_PATH = propiedades.getProperty("ES_OBS_BODY_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("ES_OBS_BODY_PATH") : "";
        ES_DICT_BODY_PATH = propiedades.getProperty("ES_DICT_BODY_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("ES_DICT_BODY_PATH") : "";
        ES_NO_ACTIVIDAD_BODY_PATH = propiedades.getProperty("ES_NO_ACTIVIDAD_BODY_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("ES_NO_ACTIVIDAD_BODY_PATH") : "";
        ES_CANCELADO_BODY_PATH = propiedades.getProperty("ES_CANCELADO_BODY_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("ES_CANCELADO_BODY_PATH") : "";
        ES_NO_ACTIVIDAD_DICT_BODY_PATH = propiedades.getProperty("ES_NO_ACTIVIDAD_DICT_BODY_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("ES_NO_ACTIVIDAD_DICT_BODY_PATH") : "";
        ES_VENCIDO_BODY_PATH = propiedades.getProperty("ES_VENCIDO_BODY_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("ES_VENCIDO_BODY_PATH") : "";
        OBR_CREADA_BODY_PATH = propiedades.getProperty("OBR_CREADA_BODY_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("OBR_CREADA_BODY_PATH") : "";
        FOOTER_PATH = propiedades.getProperty("FOOTER_PATH") != null ? FILES_HTML_PATH + propiedades.getProperty("FOOTER_PATH") : "";

        SMTP_HOST = propiedades.getProperty("SMTP_HOST") != null ? propiedades.getProperty("SMTP_HOST") : "";
        SMTP_PORT = propiedades.getProperty("SMTP_PORT") != null ? propiedades.getProperty("SMTP_PORT") : "";
        SMTP_USER = propiedades.getProperty("SMTP_USER") != null ? propiedades.getProperty("SMTP_USER") : "";
        SMTP_PASSWORD = propiedades.getProperty("SMTP_PASSWORD") != null ? propiedades.getProperty("SMTP_PASSWORD") : "";

        DIR_SGI = propiedades.getProperty("DIR_SGI") != null ? propiedades.getProperty("DIR_SGI") : "";

        VIGENCIA_SOL_OBS = propiedades.getProperty("VIGENCIA_SOL_OBS") != null ? Integer.parseInt(propiedades.getProperty("VIGENCIA_SOL_OBS")) : 0;
        DIAS_TO_SEND_NOTIFICACION_SOL = propiedades.getProperty("DIAS_TO_SEND_NOTIFICACION_SOL") != null ? propiedades.getProperty("DIAS_TO_SEND_NOTIFICACION_SOL") : "0";

        VIGENCIA_ES_OBS = propiedades.getProperty("VIGENCIA_ES_OBS") != null ? Integer.parseInt(propiedades.getProperty("VIGENCIA_ES_OBS")) : 0;
        DIAS_TO_SEND_NOTIFICACION_ES = propiedades.getProperty("DIAS_TO_SEND_NOTIFICACION_ES") != null ? propiedades.getProperty("DIAS_TO_SEND_NOTIFICACION_ES") : "0";

        VIGENCIA_ES_DICTAMINACION = propiedades.getProperty("VIGENCIA_ES_DICTAMINACION") != null ? Integer.parseInt(propiedades.getProperty("VIGENCIA_ES_DICTAMINACION")) : 0;
        DIAS_TO_SEND_NOTIFICACION_DICT_ES = propiedades.getProperty("DIAS_TO_SEND_NOTIFICACION_DICT_ES") != null ? propiedades.getProperty("DIAS_TO_SEND_NOTIFICACION_DICT_ES") : "0";

        ESTATUS_ES_BLOQUEADO = propiedades.getProperty("ESTATUS_ES_BLOQUEADO") != null ? propiedades.getProperty("ESTATUS_ES_BLOQUEADO") : "";
        
        VIGENCIA_NOTIFICACIONES = propiedades.getProperty("VIGENCIA_NOTIFICACIONES") != null ? Integer.parseInt(propiedades.getProperty("VIGENCIA_NOTIFICACIONES")) : 0;
                
    }
}
