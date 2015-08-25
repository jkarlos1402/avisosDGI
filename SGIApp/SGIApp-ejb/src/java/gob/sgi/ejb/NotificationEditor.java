package gob.sgi.ejb;

import gob.sgi.model.ConnectionManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

@Stateless
@LocalBean
public class NotificationEditor {

    public void setNotificacion(String idNotificacion, String estatus) {        
        ConnectionManager cm = new ConnectionManager();
        Connection connection = cm.conectar();
        PreparedStatement statement = null;
        int rs = 0;
        try {
            statement = connection.prepareStatement("UPDATE notificacion SET leido = ? WHERE idnotificacion = ?");
            statement.setBoolean(1, new Boolean(estatus != null && estatus != "" ? estatus : "false"));
            statement.setInt(2, new Integer(idNotificacion != null && idNotificacion != "" ? idNotificacion : "0"));
            rs = statement.executeUpdate();
            cm.desconectar(connection);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        } finally {            
            cm.desconectar(connection);
        }
    }
}
