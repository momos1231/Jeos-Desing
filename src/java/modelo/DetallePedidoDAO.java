/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DetallePedidoDAO {
    PreparedStatement ps;

    // Este método toma una conexión ya existente para ser parte de una transacción
    public boolean guardarDetallePedido(DetallePedido detalle, Connection transactionCon) throws SQLException {
        String sql = "INSERT INTO detalle_pedido (idProducto, idpedido) VALUES (?, ?)";
        try {
            ps = transactionCon.prepareStatement(sql);
            ps.setInt(1, detalle.getProducto().getIdProducto());
            ps.setInt(2, detalle.getPedido().getIdPedido());

            ps.executeUpdate();
            return true;
        } finally {
            // Importante: No cerrar la conexión aquí, ya que es una conexión de transacción.
            if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}