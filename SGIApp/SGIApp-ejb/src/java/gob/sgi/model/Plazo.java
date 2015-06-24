package gob.sgi.model;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Plazo {

    public static Date obtieneFechaPlazo(int diasPlazo) {
        GregorianCalendar calendar = new GregorianCalendar();
        int diasNaturales = 1;
        Date diaFinal = null;
        int aumento = 0;
        Calendar diaFinalCalendar = Calendar.getInstance();
        while (diasNaturales <= (diasPlazo + 1)) {
            diaFinalCalendar.add(Calendar.DAY_OF_YEAR, -1);
            diaFinal = diaFinalCalendar.getTime();
            calendar.setTime(diaFinal);            
            if (calendar.get(Calendar.DAY_OF_WEEK) != 1 && calendar.get(Calendar.DAY_OF_WEEK) != 7) {
                diasNaturales++;
            }
            aumento++;
        }        
        return diaFinal;
    }
}
