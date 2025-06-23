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

public class FacturaDAO {
    PreparedStatement ps;
    ResultSet rs;

    public int generarFactura(Factura factura, Connection transactionCon) throws SQLException {
        int idFacturaGenerado = 0;
        String sql = "INSERT INTO factura (fecha_factura, monto, Pedido_idpedido) VALUES (?, ?, ?)";
        try {
            ps = transactionCon.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, factura.getFechaFactura());
            ps.setDouble(2, factura.getMonto());
            ps.setInt(3, factura.getPedido().getIdPedido());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    idFacturaGenerado = rs.getInt(1);
                }
            }
        } finally {
            // Importante: No cerrar la conexión aquí, ya que es una conexión de transacción.
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return idFacturaGenerado;
    }
}