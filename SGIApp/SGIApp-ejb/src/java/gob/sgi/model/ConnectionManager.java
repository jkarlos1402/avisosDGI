package gob.sgi.model;

import gob.sgi.constante.Constante;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private String servidor;
    private String puerto;
    private String baseDatos;
    private String usuario;
    private String password;
    
    public ConnectionManager(){
        Properties propiedades = new Properties();
        InputStream stream = null;
        try {
            stream = new FileInputStream(Constante.FILE_CONF_PATH + "/constantes.properties");
            propiedades.load(stream);

            this.baseDatos = propiedades.getProperty("baseDatos");
            this.password = propiedades.getProperty("passwordDB");
            this.puerto = propiedades.getProperty("puertoDB");
            this.servidor = propiedades.getProperty("servidorDB");
            this.usuario = propiedades.getProperty("usuarioDB");
            
        } catch (FileNotFoundException ex) {
            System.out.println("FileNotFoundException properties: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("IOException properties: " + ex.getMessage());
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    System.out.println("IOException properties: " + e.getMessage());
                }
            }
        }
    
    }
    public Connection conectar() {
        Connection conexion = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String servidor = "jdbc:mysql://"+this.servidor+":"+this.puerto+"/"+this.baseDatos;
            String usuarioDB = this.usuario;
            String passwordDB = this.password;
            conexion = DriverManager.getConnection(servidor, usuarioDB, passwordDB);
        } catch (ClassNotFoundException ex) {
            conexion = null;
        } catch (SQLException ex) {
            System.out.println("SQLException: "+ex.getMessage());
            conexion = null;
        }
        return conexion;
        
    }
    
    public boolean desconectar(Connection connection){
        try {
            connection.close();
            return true;
        } catch (SQLException ex) {
            return false;
        }
    }
}
