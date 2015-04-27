package gob.sgi.model;

public class EstatusSolicitud {

    private static final String CREADA = "CREADA";
    private static final String EDITADA = "EDITADA";
    private static final String ENVIADA = "ENVIADA";
    private static final String INGRESADA = "INGRESADA";
    private static final String REVISADA = "REVISADA";
    private static final String ACEPTADA = "ACEPTADA";

    public static String val(String val) {
        switch (val) {
            case "1":
                return CREADA;
            case "2":
                return EDITADA;
            case "3":
                return ENVIADA;
            case "4":
                return INGRESADA;
            case "5":
                return REVISADA;
            case "6":
                return ACEPTADA;
            default:
                return "";
        }
    }
}
