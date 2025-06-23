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

public class DetallePedidoDAO {

    Conexion cn = new Conexion();

    /**
     * Guarda un detalle de pedido en la base de datos.
     * Este método solo inserta idproducto e idpedido, según la estructura de tu tabla actual.
     *
     * @param detalle El objeto DetallePedido a guardar.
     * @param transactionCon La conexión a la base de datos para la transacción.
     * @return true si el detalle se guardó correctamente, false en caso contrario.
     */
    public boolean guardarDetallePedido(DetallePedido detalle, Connection transactionCon) {
        // SQL ajustado: Solo idproducto e idpedido
        String sql = "INSERT INTO detalle_pedido (idProducto, idpedido) VALUES (?, ?)";
        PreparedStatement ps = null;
        try {
            ps = transactionCon.prepareStatement(sql);
            ps.setInt(1, detalle.getProducto().getIdProducto());
            ps.setInt(2, detalle.getPedido().getIdPedido());

            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar detalle de pedido: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (ps != null) ps.close();
                // IMPORTANTE: NO CERRAR la conexión 'transactionCon' aquí.
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
