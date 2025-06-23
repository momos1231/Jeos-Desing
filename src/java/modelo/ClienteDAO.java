/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;



import config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    Conexion cn = new Conexion(); // Instancia de tu clase Conexion
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // Método para listar todos los clientes
    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente"; // Asume que tu tabla se llama 'Cliente'
        try {
            con = cn.getConnection(); // Obtener la conexión
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getInt("idCliente"));
                c.setNombre(rs.getString("nombre"));
                c.setDireccion(rs.getString("direccion"));
                c.setEmail(rs.getString("email"));
                c.setEdad(rs.getInt("edad"));
                clientes.add(c);
            }
        } catch (Exception e) { // Cambiado a Exception para ser consistente con tu CategoriaDAO
            System.err.println("Error al listar clientes: " + e.getMessage());
            e.printStackTrace(); // Imprime el stack trace para depuración
        } finally {
            // Cerrar recursos en el finally para asegurar que se cierren incluso si hay una excepción
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close(); // Cierra la conexión si no es un pool manejado externamente
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return clientes;
    }

    // Método para buscar un cliente por ID (útil para asociar un pedido)
    public Cliente buscarPorId(int id) {
        Cliente cliente = null;
        String sql = "SELECT * FROM Cliente WHERE idCliente = ?";
        try {
            con = cn.getConnection(); // Obtener la conexión
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                cliente = new Cliente();
                cliente.setIdCliente(rs.getInt("idCliente"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setEmail(rs.getString("email"));
                cliente.setEdad(rs.getInt("edad"));
            }
        } catch (Exception e) {
            System.err.println("Error al buscar cliente por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cliente;
    }
    
    // Método para agregar un nuevo cliente (opcional)
    public boolean agregar(Cliente cliente) {
        String sql = "INSERT INTO Cliente (nombre, direccion, email, edad) VALUES (?, ?, ?, ?)";
        try {
            con = cn.getConnection(); // Obtener la conexión
            ps = con.prepareStatement(sql);
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDireccion());
            ps.setString(3, cliente.getEmail());
            ps.setInt(4, cliente.getEdad());
            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error al agregar cliente: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}