package gob.sgi.model;

public class Constante {
    //palabras reservadas para el texto de los emails
    public static final String STR_ID_BANCO = "$idBanco";
    public static final String STR_ID_SOLICITUD = "$idSolicitud";
    public static final String STR_ESTATUS_SOL = "$estatusSolicitud";
    public static final String STR_ESTATUS_ES = "$estatusES";
    public static final String STR_NOM_UE = "$nombreUE";    

    //serie de rutas para los email
    public static final String HEADER_PATH = "c:/www/sgi/resources/header.html";
    public static final String SOL_ENV_BODY_PATH = "c:/www/sgi/resources/sol_env_body.html";
    public static final String SOL_OBS_BODY_PATH = "c:/www/sgi/resources/sol_obs_body.html";
    public static final String SOL_ACEPT_BODY_PATH = "c:/www/sgi/resources/sol_acept_body.html";
    public static final String SOL_INGRESO_BODY_PATH = "c:/www/sgi/resources/sol_ingreso_body.html";
    public static final String ES_ENV_BODY_PATH = "c:/www/sgi/resources/es_env_body.html";
    public static final String ES_INGRESO_BODY_PATH = "c:/www/sgi/resources/es_ingreso_body.html";
    public static final String ES_OBS_BODY_PATH = "c:/www/sgi/resources/es_obs_body.html";
    public static final String ES_DICT_BODY_PATH = "c:/www/sgi/resources/es_dict_body.html";
    public static final String OBR_CREADA_BODY_PATH = "c:/www/sgi/resources/obr_creada_body.html";
    public static final String FOOTER_PATH = "c:/www/sgi/resources/footer.html";
       
    //configuracion del servidor smtp
    public static final String SMTP_HOST = "smtp.office365.com";
    public static final String SMTP_PORT = "587";
    public static final String SMTP_USER = "soporte.sspp@edomex.gob.mx";
    public static final String SMTP_PASSWORD = "Soporte01";
    
    //tipos de rol de usuario
    public static final String ROL_DEPENDENCIA = "1";
    public static final String ROL_BANCO = "2";
    public static final String ROL_AREA = "3";
    public static final String ROL_VENTANILLA = "4";
    
    //estatus de solicitud    
    public static final String ESTATUS_SOL_ENVIADA = "3";
    public static final String ESTATUS_SOL_INGRESADA = "4";
    public static final String ESTATUS_SOL_REVISADA = "5";
    public static final String ESTATUS_SOL_ACEPTADA = "6";
    
    // estaus de estudio socioeconomico
    public static final String ESTATUS_ES_CREADO = "1";
    public static final String ESTATUS_ES_ENVIADO = "3";
    public static final String ESTATUS_ES_INGRESADO = "4";
    public static final String ESTATUS_ES_REVISADO = "5";
    public static final String ESTATUS_ES_OBSERVACIONES = "2";
    public static final String ESTATUS_ES_DICTAMINADO = "6";
}
