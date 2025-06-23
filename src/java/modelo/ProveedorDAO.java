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
public class ProveedorDAO {
    
    Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    public List<Proveedor> listar() {
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedor";  // ajusta el nombre de tu tabla
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Proveedor pro = new Proveedor();
                pro.setIdProveedor(rs.getInt("idProveedor"));   
                pro.setNombre_proveedor(rs.getString("nombre_proveedor"));// ajusta campos
                pro.setCorreo(rs.getString("correo"));
                lista.add(pro);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public boolean agregar(Proveedor proveedor) {
    String sql = "INSERT INTO proveedor(nombre_proveedor, correo) VALUES (?, ?)";
    try (Connection con = cn.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, proveedor.getNombre_proveedor());
        ps.setString(2, proveedor.getCorreo());

        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
    
}
