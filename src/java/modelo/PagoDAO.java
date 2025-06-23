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

public class PagoDAO {
    PreparedStatement ps;
    ResultSet rs;

    public int generarPago(Pago pago, Connection transactionCon) throws SQLException {
        int idPagoGenerado = 0;
        String sql = "INSERT INTO pagos (fecha_pago, monto_pago, estado, idFactura, idMetodo_pago) VALUES (?, ?, ?, ?, ?)";
        try {
            ps = transactionCon.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, java.sql.Date.valueOf(pago.getFechaPago()));
            ps.setDouble(2, pago.getMontoPago());
            ps.setString(3, pago.getEstado());
            ps.setInt(4, pago.getFactura().getIdFactura());
            ps.setInt(5, pago.getMetodoPago().getIdMetodoPago());

            int filasAfectadas = ps.executeUpdate();
            if (filasAfectadas > 0) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    idPagoGenerado = rs.getInt(1);
                }
            }
        } finally {
            // Importante: No cerrar la conexión aquí, ya que es una conexión de transacción.
            if (rs != null) try { rs.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (ps != null) try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
        return idPagoGenerado;
    }
}