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

    public List listar() {
        String sql = "select * from producto";
        List<Producto> lista = new ArrayList<>();

        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Producto pd = new Producto();
                pd.setIdProducto(rs.getInt(1));
                pd.setNombre(rs.getString(2));
                pd.setPrecio(rs.getInt(3));
                pd.setGenero(rs.getString(4));
                pd.setDescripcion(rs.getString(5));
                pd.setFoto(rs.getBytes("foto"));
                // Leer las llaves foráneas
            int idCategoria = rs.getInt("idCategoria");
            int idProveedor = rs.getInt("Proveedor_idProveedor");
            int idColor = rs.getInt("Color_idColor");
            int idTalla = rs.getInt("Talla_idTalla");

            // Crear objetos relacionados con solo el ID
            Categoria cat = new Categoria();
            cat.setIdCategoria(idCategoria);
            pd.setCategoria(cat);

            Proveedor prov = new Proveedor();
            prov.setIdProveedor(idProveedor);
            pd.setProveedor(prov);

            Color color = new Color();
            color.setIdColor(idColor);
            pd.setColor(color);

            Talla talla = new Talla();
            talla.setIdTalla(idTalla);
            pd.setTalla(talla);


                //System.out.println("Producto cargado: " + pd.getNombre());

                lista.add(pd);
            }
        } catch (Exception e) {
        }

        return lista;
    }

    public int agregar(Producto pd) {
        String sql = "INSERT INTO producto(nombre, precio, genero, descripcion, foto, idCategoria, Proveedor_idProveedor, Color_idColor, Talla_idTalla) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, pd.getNombre());
            ps.setInt(2, pd.getPrecio());
            ps.setString(3, pd.getGenero());
            ps.setString(4, pd.getDescripcion());
            ps.setBytes(5, pd.getFoto()); // Suponiendo que estás manejando la imagen como arreglo de bytes
            ps.setInt(6, pd.getCategoria().getIdCategoria());
            ps.setInt(7, pd.getProveedor().getIdProveedor());
            ps.setInt(8, pd.getColor().getIdColor());
            ps.setInt(9, pd.getTalla().getIdTalla());

            r = ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error al insertar producto: " + e.getMessage());
        }
        return r;
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
    
    public int actualizar(Producto pd) {
    String sql = "UPDATE producto SET nombre=?, precio=?, genero=?, descripcion=?, foto=?, idCategoria=?, Proveedor_idProveedor=?, Color_idColor=?, Talla_idTalla=? WHERE idProducto=?";
    try {
        con = cn.getConnection();
        ps = con.prepareStatement(sql);
        ps.setString(1, pd.getNombre());
        ps.setInt(2, pd.getPrecio());
        ps.setString(3, pd.getGenero());
        ps.setString(4, pd.getDescripcion());
        ps.setBytes(5, pd.getFoto()); // Si no actualizas la imagen, asegúrate de que esta no sea null
        ps.setInt(6, pd.getCategoria().getIdCategoria());
        ps.setInt(7, pd.getProveedor().getIdProveedor());
        ps.setInt(8, pd.getColor().getIdColor());
        ps.setInt(9, pd.getTalla().getIdTalla());
        ps.setInt(10, pd.getIdProducto());

        r = ps.executeUpdate();
    } catch (Exception e) {
        System.out.println("Error al actualizar producto: " + e.getMessage());
    }
    return r;
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
