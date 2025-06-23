package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    Connection con;
    String url = "jdbc:mysql://localhost:3306/bding?useSSL=false&serverTimezone=UTC";
    String user = "root";   
    String password = "";

    public Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
          
           
            con = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión a la base de datos establecida.");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: Driver JDBC no encontrado. Asegúrate de tener el JAR del driver en tu classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error de SQL al conectar a la base de datos.");
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return con;
    }
public static Connection getConexion() throws SQLException {
        return new Conexion().getConnection();
    }
    
}