
package gob.sgi.model;


public class EstatusES {
    private static final String CREADO = "CREADO";
    private static final String EDITADO = "OBSERVACIONES";
    private static final String ENVIADO = "ENVIADO";
    private static final String INGRESADO = "INGRESADO";
    private static final String REVISADO = "REVISADO";
    private static final String DICTAMINADO = "DICTAMINADO";

    public static String val(String val) {
        switch (val) {
            case "1":
                return CREADO;            
            case "2":
                return EDITADO;            
            case "3":
                return ENVIADO;
            case "4":
                return INGRESADO;
            case "5":
                return REVISADO;
            case "6":
                return DICTAMINADO;
            default:
                return "";
        }
    }
}
