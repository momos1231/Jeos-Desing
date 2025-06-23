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
import java.sql.Statement;

public class PedidoDAO {
    // No necesitamos una instancia de Conexion aquí, ya que se pasa la conexión de transacción
    PreparedStatement ps;
    ResultSet rs;

    // Este método toma una conexión ya existente para ser parte de una transacción
    public int generarPedido(Pedido pedido, Connection transactionCon) throws SQLException {
        int idPedidoGenerado = 0;
        String sql = "INSERT INTO pedido (cantidad, precio, idCliente, idEmpleado) VALUES (?, ?, ?, ?)";
        try {
            
            ps = transactionCon.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, pedido.getCantidadItems());
            ps.setDouble(2, pedido.getTotal());
            ps.setLong(3, pedido.getCliente().getIdCliente());
            ps.setInt(4, pedido.getEmpleado().getIdEmpleado());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    idPedidoGenerado = rs.getInt(1);
                }
            }
        } finally {
            // Importante: No cerrar la conexión aquí, ya que es una conexión de transacción.
            // Se cerrará en el Controlador o donde se inició la transacción.
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return idPedidoGenerado;
    }
}