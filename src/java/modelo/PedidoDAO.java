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
import java.time.LocalDateTime;
import java.sql.Timestamp;

public class PedidoDAO {
    Conexion cn = new Conexion(); // Instancia de tu clase Conexion
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // Método para insertar un nuevo pedido y devolver su ID generado
    public int insertarPedido(Pedido pedido) {
        int idPedidoGenerado = -1;
        String sql = "INSERT INTO Pedido (idCliente, fechaPedido, total, estado) VALUES (?, ?, ?, ?)";
        try {
            con = cn.getConnection(); // Obtener la conexión
            // Usa RETURN_GENERATED_KEYS para obtener el ID autoincremental
            ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, pedido.getCliente().getIdCliente());
            ps.setTimestamp(2, Timestamp.valueOf(pedido.getFechaPedido())); // Convierte LocalDateTime a Timestamp
            ps.setDouble(3, pedido.getTotal());
            ps.setString(4, pedido.getEstado());

            int filasAfectadas = ps.executeUpdate();

            if (filasAfectadas > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    idPedidoGenerado = rs.getInt(1); // Obtiene el primer ID generado
                }
            }
        } catch (Exception e) {
            System.err.println("Error al insertar pedido: " + e.getMessage());
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
        return idPedidoGenerado;
    }

    // Método para insertar un detalle de pedido
    public boolean insertarDetallePedido(DetallePedido detallePedido) {
        String sql = "INSERT INTO DetallePedido (idPedido, idProducto, idTalla, idColor, cantidad, precioUnitario) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            con = cn.getConnection(); // Obtener la conexión
            ps = con.prepareStatement(sql);
            ps.setInt(1, detallePedido.getPedido().getIdPedido());
            ps.setInt(2, detallePedido.getProducto().getIdProducto());
            // Manejar si talla o color son null (si permites que no se seleccionen)
            if (detallePedido.getTalla() != null) {
                ps.setInt(3, detallePedido.getTalla().getIdTalla());
            } else {
                ps.setNull(3, java.sql.Types.INTEGER); // O el tipo SQL apropiado
            }
            if (detallePedido.getColor() != null) {
                ps.setInt(4, detallePedido.getColor().getIdColor());
            } else {
                ps.setNull(4, java.sql.Types.INTEGER); // O el tipo SQL apropiado
            }
            
            ps.setInt(5, detallePedido.getCantidad());
            ps.setDouble(6, detallePedido.getPrecioUnitario());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;
        } catch (Exception e) {
            System.err.println("Error al insertar detalle de pedido: " + e.getMessage());
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