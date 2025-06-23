/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import config.Conexion; // Importa tu clase Conexion
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException; // Importa SQLException para un manejo específico
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    // No es necesario mantener una instancia de Conexion o Connection aquí
    // Cada método obtendrá una conexión fresca usando Conexion.getConexion()
    // y la cerrará en su propio bloque finally.

    // Método para listar todos los productos
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT " +
                     " p.idProducto, p.nombre, p.precio, p.genero, p.descripcion, p.foto, " +
                     " c.idCategoria, c.nombre AS nombreCategoria, " +
                     " pr.idProveedor, pr.nombre_proveedor AS nombreProveedor, " +
                     " co.idColor, co.nombre AS nombreColor, " +
                     " t.idTalla, t.nombre AS nombreTalla " +
                     "FROM producto p " +
                     " LEFT JOIN categoria c    ON p.idCategoria = c.idCategoria " +
                     " LEFT JOIN proveedor pr   ON p.idProveedor = pr.idProveedor " +
                     " LEFT JOIN color co       ON p.idColor = co.idColor " +
                     " LEFT JOIN talla t        ON p.idTalla = t.idTalla";
                     
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConexion(); // Obtener una nueva conexión
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("idProducto"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getInt("precio"));
                p.setGenero(rs.getString("genero"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setFoto(rs.getBytes("foto"));

                // Crear y settear objetos relacionados (asumiendo que siempre hay un ID, incluso si el nombre es null)
                Categoria cat = new Categoria();
                cat.setIdCategoria(rs.getInt("idCategoria"));
                cat.setNombre(rs.getString("nombreCategoria")); // Puede ser null si el JOIN es null
                p.setCategoria(cat);

                Proveedor prov = new Proveedor();
                prov.setIdProveedor(rs.getInt("idProveedor"));
                prov.setNombre_proveedor(rs.getString("nombreProveedor")); // Puede ser null
                p.setProveedor(prov);

                Color color = new Color();
                color.setIdColor(rs.getInt("idColor"));
                color.setNombre(rs.getString("nombreColor")); // Puede ser null
                p.setColor(color);

                Talla talla = new Talla();
                talla.setIdTalla(rs.getInt("idTalla"));
                talla.setNombre(rs.getString("nombreTalla")); // Puede ser null
                p.setTalla(talla);

                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al listar productos: " + e.getMessage());
            e.printStackTrace(); // Imprimir el stack trace completo para depuración
        } finally {
            // Asegurarse de cerrar todos los recursos en el orden inverso
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos en listar(): " + e.getMessage());
                e.printStackTrace();
            }
        }
        return lista;
    }

    // Método para buscar un producto por ID (renombrado de listarId para mayor claridad)
    // Este es el que el Controlador usa para 'productoAgregado' y 'pEditar'
    public Producto listarId(int idProducto) { // Mantengo el nombre original para no cambiar el Controlador
        Producto p = null; // Inicializar a null, devolver null si no se encuentra
        String sql = "SELECT " +
                     " p.idProducto, p.nombre, p.precio, p.genero, p.descripcion, p.foto, " +
                     " c.idCategoria, c.nombre AS nombreCategoria, " +
                     " pr.idProveedor, pr.nombre_proveedor AS nombreProveedor, " +
                     " co.idColor, co.nombre AS nombreColor, " +
                     " t.idTalla, t.nombre AS nombreTalla " +
                     "FROM producto p " +
                     " LEFT JOIN categoria c    ON p.idCategoria = c.idCategoria " +
                     " LEFT JOIN proveedor pr   ON p.idProveedor = pr.idProveedor " +
                     " LEFT JOIN color co       ON p.idColor = co.idColor " +
                     " LEFT JOIN talla t        ON p.idTalla = t.idTalla " +
                     "WHERE p.idProducto = ?";
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idProducto);
            rs = ps.executeQuery();

            if (rs.next()) { // Solo si hay resultados
                p = new Producto(); // Inicializar el objeto solo si se encuentra
                p.setIdProducto(rs.getInt("idProducto"));
                p.setNombre(rs.getString("nombre"));
                p.setPrecio(rs.getInt("precio"));
                p.setGenero(rs.getString("genero"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setFoto(rs.getBytes("foto"));

                Categoria cat = new Categoria();
                cat.setIdCategoria(rs.getInt("idCategoria"));
                cat.setNombre(rs.getString("nombreCategoria"));
                p.setCategoria(cat);

                Proveedor prov = new Proveedor();
                prov.setIdProveedor(rs.getInt("idProveedor"));
                prov.setNombre_proveedor(rs.getString("nombreProveedor"));
                p.setProveedor(prov);

                Color color = new Color();
                color.setIdColor(rs.getInt("idColor"));
                color.setNombre(rs.getString("nombreColor"));
                p.setColor(color);

                Talla talla = new Talla();
                talla.setIdTalla(rs.getInt("idTalla"));
                talla.setNombre(rs.getString("nombreTalla"));
                p.setTalla(talla);
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al buscar producto por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos en listarId(): " + e.getMessage());
                e.printStackTrace();
            }
        }
        return p; // Devolverá null si no se encuentra
    }

    // Método para agregar un producto
    public boolean agregar(Producto p) {
        String sql = "INSERT INTO producto (nombre, precio, genero, descripcion, foto, idCategoria, idProveedor, idColor, idTalla) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getPrecio());
            ps.setString(3, p.getGenero());
            ps.setString(4, p.getDescripcion());
            ps.setBytes(5, p.getFoto()); // Asegúrate de que p.getFoto() no sea nulo si la columna no acepta null
            ps.setInt(6, p.getCategoria().getIdCategoria());
            ps.setInt(7, p.getProveedor().getIdProveedor());
            ps.setInt(8, p.getColor().getIdColor());
            ps.setInt(9, p.getTalla().getIdTalla());

            int rowsInserted = ps.executeUpdate();
            return (rowsInserted > 0);
        } catch (SQLException e) {
            System.err.println("Error SQL al agregar producto: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos en agregar(): " + e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

    // Método para eliminar un producto
    public boolean eliminar(int idProducto) { // Cambiado a boolean para indicar éxito/fracaso
        String sql = "DELETE FROM producto WHERE idProducto = ?";
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idProducto);
            int rowsDeleted = ps.executeUpdate();
            return (rowsDeleted > 0);
        } catch (SQLException e) {
            System.err.println("Error SQL al eliminar producto: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos en eliminar(): " + e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }

    // Método para actualizar un producto (ahora llamado 'actualizar' para coincidir con tu Controlador)
    // El nombre del método en tu Controlador es 'pdao.actualizar(pActualizar);'
    public boolean actualizar(Producto p) { // Renombrado de 'editar' a 'actualizar'
        String sql;
        // CORRECCIÓN: "Proveedor=?" cambiado a "idProveedor=?"
        if (p.getFoto() != null) {
            sql = "UPDATE producto SET nombre=?, precio=?, genero=?, descripcion=?, foto=?, idCategoria=?, idProveedor=?, idColor=?, idTalla=? WHERE idProducto=?";
        } else {
            sql = "UPDATE producto SET nombre=?, precio=?, genero=?, descripcion=?, idCategoria=?, idProveedor=?, idColor=?, idTalla=? WHERE idProducto=?";
        }
        
        Connection con = null;
        PreparedStatement ps = null;

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setInt(2, p.getPrecio());
            ps.setString(3, p.getGenero());
            ps.setString(4, p.getDescripcion());

            int index = 5;
            if (p.getFoto() != null) {
                ps.setBytes(index++, p.getFoto());
            }

            ps.setInt(index++, p.getCategoria().getIdCategoria());
            ps.setInt(index++, p.getProveedor().getIdProveedor());
            ps.setInt(index++, p.getColor().getIdColor());
            ps.setInt(index++, p.getTalla().getIdTalla());
            ps.setInt(index, p.getIdProducto()); // El último parámetro es el idProducto para el WHERE

            int updated = ps.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            System.err.println("Error SQL al actualizar producto: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos en actualizar(): " + e.getMessage());
                e.printStackTrace();
            }
        }
        return false;
    }
    
    // Método para obtener solo la foto por ID
    public byte[] obtenerFotoPorId(int idProducto) {
        byte[] foto = null;
        String sql = "SELECT foto FROM producto WHERE idProducto = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = Conexion.getConexion();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idProducto);
            rs = ps.executeQuery();
            if (rs.next()) {
                foto = rs.getBytes("foto");
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al obtener foto: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos en obtenerFotoPorId(): " + e.getMessage());
                e.printStackTrace();
            }
        }
        return foto;
    }
}