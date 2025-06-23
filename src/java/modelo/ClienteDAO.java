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
import java.sql.Statement; // Necesario para RETURN_GENERATED_KEYS
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    Conexion cn = new Conexion(); // Instancia de tu clase Conexion

    /**
     * Lista todos los clientes de la base de datos.
     * Este método NO es parte de la transacción de compra, por lo que gestiona su propia conexión.
     * @return Una lista de objetos Cliente.
     */
    public List<Cliente> listar() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Cliente";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = cn.getConnection(); // Obtener la conexión
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cliente c = new Cliente();
                c.setIdCliente(rs.getLong("idCliente"));
                c.setNombre(rs.getString("nombre"));
                c.setDireccion(rs.getString("direccion"));
                c.setEmail(rs.getString("email"));
                c.setEdad(rs.getInt("edad"));
                clientes.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close(); // Cerrar la conexión aquí
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return clientes;
    }

    /**
     * Busca un cliente por su ID.
     * Este método NO es parte de la transacción de compra, por lo que gestiona su propia conexión.
     * @param id El ID del cliente a buscar.
     * @return El objeto Cliente si se encuentra, null en caso contrario.
     */
    public Cliente buscarPorId(long id) {
        Cliente cliente = null;
        String sql = "SELECT * FROM Cliente WHERE idCliente = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setLong(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                cliente = new Cliente();
                cliente.setIdCliente(rs.getLong("idCliente"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setEmail(rs.getString("email"));
                cliente.setEdad(rs.getInt("edad"));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close(); // Cerrar la conexión aquí
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cliente;
    }

    /**
     * Busca un cliente por su correo electrónico usando una conexión específica.
     * Es ideal para usar dentro de una transacción.
     * @param email El correo electrónico del cliente a buscar.
     * @param transactionCon La conexión a la base de datos para la transacción.
     * @return El objeto Cliente si se encuentra, null en caso contrario.
     */
    public Cliente buscarClientePorEmail(String email, Connection transactionCon) { // Acepta la conexión
        Cliente cliente = null;
        String sql = "SELECT * FROM cliente WHERE email = ?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // Usa la conexión pasada por parámetro, NO obtiene una nueva
            ps = transactionCon.prepareStatement(sql); 
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) {
                cliente = new Cliente();
                cliente.setIdCliente(rs.getLong("idCliente"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setEmail(rs.getString("email"));
                cliente.setEdad(rs.getInt("edad"));
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar cliente por email (en transacción): " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                // IMPORTANTE: NO CERRAR 'transactionCon' aquí. Se cierra en el Controlador.
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cliente;
    }

    /**
     * Inserta un nuevo cliente en la base de datos usando una conexión específica.
     * Es ideal para usar dentro de una transacción.
     * @param cliente El objeto Cliente a insertar.
     * @param transactionCon La conexión a la base de datos para la transacción.
     * @return El ID del cliente generado, o 0 si falla.
     */
    public int insertarCliente(Cliente cliente, Connection transactionCon) { // Acepta la conexión
        int idGenerado = 0;
        String sql = "INSERT INTO cliente (nombre, direccion, email, edad) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            // Usa la conexión pasada por parámetro, NO obtiene una nueva
            ps = transactionCon.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getDireccion());
            ps.setString(3, cliente.getEmail());
            ps.setInt(4, cliente.getEdad());
            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    idGenerado = rs.getInt(1);
                    cliente.setIdCliente((long) idGenerado);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar cliente (en transacción): " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                // IMPORTANTE: NO CERRAR 'transactionCon' aquí. Se cierra en el Controlador.
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return idGenerado;
    }
}