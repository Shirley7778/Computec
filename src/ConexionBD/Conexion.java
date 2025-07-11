package ConexionBD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author JENS07
 */
public class Conexion {

    // Nombre de la base de datos ajustado al sistema de ventas/inventarios
    public static String cadena = "jdbc:mysql://localhost:3306/compu_tec";
    public static String usuario = "root";
    public static String clave = "root";
    public static String driver = "com.mysql.cj.jdbc.Driver";

    //conexion local
    public static Connection conectar() {
        try {
            Connection cn = DriverManager.getConnection(cadena, usuario, clave);
            return cn;
        } catch (SQLException e) {
            System.out.println("Error en la conexion local " + e);
        }
        return null;
    }
    
}
