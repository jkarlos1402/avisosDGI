package gob.sgi.listener;

import gob.sgi.constante.Constante;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class StartUpListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Date now = new Date();
        DateFormat df = DateFormat.getDateInstance();
        System.out.println("Se levanto la aplicacion: " + df.format(now));
        //se crea y copian los archivos iniciales
        File directorio = new File(Constante.FILES_HTML_PATH);
        if (!directorio.isDirectory()) {
            directorio.mkdirs();
            ServletContext sc = sce.getServletContext();
            String contextPathResources = sc.getRealPath(File.separator + "WEB-INF" + File.separator + "resources");
            File directorioBase = new File(contextPathResources);
            String[] ficheros = directorioBase.list();
            for (int i = 0; i < ficheros.length; i++) {
                copiarFicheros(new File(directorioBase, ficheros[i]), new File(directorio, ficheros[i]));
            }
            System.out.println("Se copiaron los archivos iniciales");
        }
        File directorioConf = new File(Constante.FILE_CONF_PATH);
        if (!directorioConf.isDirectory()) {
            directorioConf.mkdirs();
            ServletContext sc = sce.getServletContext();
            String contextPathResources = sc.getRealPath(File.separator + "WEB-INF" + File.separator + "resources" + File.separator + "conf");
            File directorioBase = new File(contextPathResources);
            String[] ficheros = directorioBase.list();
            for (int i = 0; i < ficheros.length; i++) {
                copiarFicheros(new File(directorioBase, ficheros[i]), new File(directorioConf, ficheros[i]));
            }
            System.out.println("Se copiaron los archivos de configuracion");
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        Date now = new Date();
        DateFormat df = DateFormat.getDateInstance();
        System.out.println("Se cerro la aplicacion: " + df.format(now));
    }

    private void copiarFicheros(File f1, File f2) {
        try {
            OutputStream out = null;
            if (!f1.isDirectory()) {
                try (InputStream in = new FileInputStream(f1)) {
                    out = new FileOutputStream(f2);
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }
            }
            if (out != null) {
                out.close();
            }
        } catch (IOException ioe) {
            System.out.println("IOException: " + ioe.getMessage());
        }
    }
}
