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
public class ColorDAO {
     Conexion cn = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    public List<Color> listar() {
        List<Color> lista = new ArrayList<>();
        String sql = "SELECT * FROM color";  // ajusta el nombre de tu tabla
        try {
            con = cn.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Color c = new Color();
                c.setIdColor(rs.getInt("idColor"));    // ajusta campos
                c.setNombre(rs.getString("nombre"));   // ajusta campos
                lista.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public boolean agregar(Color color) {
    String sql = "INSERT INTO color(nombre) VALUES(?)";
    try (Connection con = cn.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, color.getNombre());
        return ps.executeUpdate() > 0;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}
}
