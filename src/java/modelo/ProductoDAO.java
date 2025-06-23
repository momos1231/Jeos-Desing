/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modelo;

import config.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author FAMILIA RUSSI
 */
public class ProductoDAO {

    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    int r;

    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT " +
                 " p.idProducto, p.nombre, p.precio, p.genero, p.descripcion, p.foto, " +
                 " c.idCategoria, c.nombre AS nombreCategoria, " +
                 " pr.idProveedor, pr.nombre_proveedor AS nombreProveedor, " +
                 " co.idColor, co.nombre AS nombreColor, " +
                 " t.idTalla, t.nombre AS nombreTalla " +
                 "FROM producto p " +
                 " LEFT JOIN categoria c   ON p.idCategoria = c.idCategoria " +
                 " LEFT JOIN proveedor pr  ON p.Proveedor_idProveedor = pr.idProveedor " +
                 " LEFT JOIN color co      ON p.Color_idColor = co.idColor " +
                 " LEFT JOIN talla t       ON p.Talla_idTalla = t.idTalla";
            
        try {
            con = cn.getConnection();
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

            // Crear objetos relacionados con solo el ID
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

                lista.add(p);
            }
        } catch (Exception e) {
        } 

        return lista;
    }

    public boolean agregar(Producto p) {
    String sql = "INSERT INTO producto (nombre, precio, genero, descripcion, foto, idCategoria, Proveedor_idProveedor, Color_idColor, Talla_idTalla) "
               + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, p.getNombre());
        ps.setInt(2, p.getPrecio());
        ps.setString(3, p.getGenero());
        ps.setString(4, p.getDescripcion());
        ps.setBytes(5, p.getFoto());
        ps.setInt(6, p.getCategoria().getIdCategoria());
        ps.setInt(7, p.getProveedor().getIdProveedor());
        ps.setInt(8, p.getColor().getIdColor());
        ps.setInt(9, p.getTalla().getIdTalla());

        int rowsInserted = ps.executeUpdate();
        return (rowsInserted > 0); // true si insertó
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (Exception e) {
            e.printStackTrace();}
    }
    return false;
}
    
    public int eliminar(int idProducto) {
    String sql = "DELETE FROM producto WHERE idProducto = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, idProducto);
        r = ps.executeUpdate();
    } catch (Exception e) {
        System.out.println("Error al eliminar producto: " + e.getMessage());
    }
    return r;
}
   // método editar mejorado
public boolean editar(Producto p) {
    String sql;
    if (p.getFoto() != null) {
        sql = "UPDATE producto SET nombre=?, precio=?, genero=?, descripcion=?, foto=?, idCategoria=?, Proveedor_idProveedor=?, Color_idColor=?, Talla_idTalla=? WHERE idProducto=?";
    } else {
        sql = "UPDATE producto SET nombre=?, precio=?, genero=?, descripcion=?, idCategoria=?, Proveedor_idProveedor=?, Color_idColor=?, Talla_idTalla=? WHERE idProducto=?";
    }


    try {
        con = cn.getConnection();
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
        ps.setInt(index, p.getIdProducto());

        int updated = ps.executeUpdate();
        return updated > 0;
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        try {
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    
    }
    return false;
}
    
    public byte[] obtenerFotoPorId(int idProducto) {
    byte[] foto = null;
    String sql = "SELECT foto FROM producto WHERE idProducto = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, idProducto);
        rs = ps.executeQuery();
        if (rs.next()) {
            foto = rs.getBytes("foto");
        }
    } catch (Exception e) {
        System.out.println("Error al obtener foto: " + e.getMessage());
    }
    return foto;
}
    
    public Producto buscarPorId(int idProducto) {
    Producto pd = new Producto();
    String sql = "SELECT * FROM producto WHERE idProducto = ?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, idProducto);
        rs = ps.executeQuery();
        if (rs.next()) {
            pd.setIdProducto(rs.getInt("idProducto"));
            pd.setNombre(rs.getString("nombre"));
            pd.setPrecio(rs.getInt("precio"));
            pd.setGenero(rs.getString("genero"));
            pd.setDescripcion(rs.getString("descripcion"));
            pd.setFoto(rs.getBytes("foto"));

            // Relaciones
            Categoria cat = new Categoria();
            cat.setIdCategoria(rs.getInt("idCategoria"));
            pd.setCategoria(cat);

            Proveedor prov = new Proveedor();
            prov.setIdProveedor(rs.getInt("Proveedor_idProveedor"));
            pd.setProveedor(prov);

            Color color = new Color();
            color.setIdColor(rs.getInt("Color_idColor"));
            pd.setColor(color);

            Talla talla = new Talla();
            talla.setIdTalla(rs.getInt("Talla_idTalla"));
            pd.setTalla(talla);
        }
    } catch (Exception e) {
        System.out.println("Error al buscar producto por ID: " + e.getMessage());
    }
    return pd;
}
    
}
