package config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    // La conexión 'con' no debe ser de instancia si el objetivo es obtener una nueva cada vez.
    // O si se mantiene de instancia, debe ser para un pool.
    // Para esta solución directa, vamos a modificar el getConnection() para que siempre devuelva una conexión fresca si la anterior está cerrada/nula.
    // private Connection con; // Ya no la necesitamos como variable de instancia aquí si getConnection la crea

    String url = "jdbc:mysql://localhost:3306/bding?useSSL=false&serverTimezone=UTC";
    String user = "root";    
    String password = "";

    // Constructor: Ahora solo carga el driver, no establece la conexión directa
    public Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
            System.out.println("Driver JDBC cargado.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver JDBC no encontrado. Asegúrate de tener el JAR del driver en tu classpath.");
            e.printStackTrace();
        }
    }

    /**
     * Este método devuelve una nueva conexión a la base de datos cada vez que es llamado.
     * Es crucial que cada método DAO que la obtenga, la cierre en su bloque finally
     * A MENOS que sea una conexión de transacción que se gestiona externamente.
     * @return Una nueva instancia de Connection.
     * @throws SQLException Si ocurre un error al establecer la conexión.
     */
    public Connection getConnection() throws SQLException {
        Connection con = null; // Declara la conexión localmente
        try {
            con = DriverManager.getConnection(url, user, password);
            // System.out.println("Nueva Conexión a la base de datos establecida."); // Quitar para evitar muchos logs
        } catch (SQLException e) {
            System.err.println("Error de SQL al establecer una nueva conexión a la base de datos: " + e.getMessage());
            throw e; // Relanzar la excepción para que sea manejada por el que llamó
        }
        return con;
    }

    /**
     * Método estático para obtener una conexión (similar a tu getConexion original).
     * También devuelve una nueva conexión cada vez.
     * @return Una nueva instancia de Connection.
     * @throws SQLException Si ocurre un error al establecer la conexión.
     */
    public static Connection getConexion() throws SQLException {
        // Crea una nueva instancia de Conexion solo para llamar a getConnection()
        return new Conexion().getConnection(); 
    }
}