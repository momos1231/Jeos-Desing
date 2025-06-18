/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import com.sun.jdi.connect.spi.Connection;
import config.Conexion;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.util.ArrayList;
import java.util.List;
import modelo.Categoria;
import modelo.Color;
import modelo.Producto;
import modelo.ProductoDAO;
import modelo.Proveedor;
import modelo.Talla;

/**
 *
 * @author nicolas
 */
@WebServlet(name = "Controlador_1", urlPatterns = {"/Controlador_1"})
public class Controlador extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

    String accion = request.getParameter("accion");
    ProductoDAO dao = new ProductoDAO();

    // LISTAR PRODUCTOS
    if ("listarProductos".equals(accion)) {
    List<Producto> lista = new ArrayList<>();

    /*Connection con = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    String sql = "SELECT p.idProducto, p.nombre, p.precio, p.genero, p.descripcion, p.foto, " +
                 "c.idCategoria, c.nombre AS nombreCategoria, " +
                 "pr.idProveedor, pr.nombre AS nombreProveedor, " +
                 "co.idColor, co.nombre AS nombreColor, " +
                 "t.idTalla, t.nombre AS nombreTalla " +
                 "FROM producto p " +
                 "LEFT JOIN categoria c ON p.idCategoria = c.idCategoria " +
                 "LEFT JOIN proveedor pr ON p.idProveedor = pr.idProveedor " +
                 "LEFT JOIN color co ON p.idColor = co.idColor " +
                 "LEFT JOIN talla t ON p.idTalla = t.idTalla";

    try {
            // Aquí usa tu clase Conexion.java
           Connection = con.ge(); 

            ps = con.prepareStatement("SELECT foto FROM empleado WHERE idEmpleado = ?");
            ps.setInt(1, idEmpleado);
            rs = ps.executeQuery();

        while (rs.next()) {
            Producto p = new Producto();
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
            prov.setNombre(rs.getString("nombreProveedor"));
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

    } catch (SQLException e) 
    {
        e.printStackTrace();
    } finally {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch  (){
            
        }
    }

    request.setAttribute("productos", lista);
    request.getRequestDispatcher("index.jsp").forward(request, response);*/
}

    // MOSTRAR FORMULARIO PARA CREAR
    else if ("nuevoProducto".equals(accion)) {
        // No hay producto en request => JSP mostrará formulario en modo "Agregar"
        request.getRequestDispatcher("Editar_Agregar_PRO.jsp").forward(request, response);
    }

    // CREAR PRODUCTO
    else if ("agregarProducto".equals(accion)) {
        String nombre = request.getParameter("nombre");
        int precio = Integer.parseInt(request.getParameter("precio"));
        String genero = request.getParameter("genero");
        String descripcion = request.getParameter("descripcion");

        Part fotoPart = request.getPart("foto");
        byte[] foto = fotoPart.getInputStream().readAllBytes();

        int idCategoria = Integer.parseInt(request.getParameter("categoriaId"));
        int idProveedor = Integer.parseInt(request.getParameter("proveedorId"));
        int idColor = Integer.parseInt(request.getParameter("colorId"));
        int idTalla = Integer.parseInt(request.getParameter("tallaId"));

        Producto p = new Producto();
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setGenero(genero);
        p.setDescripcion(descripcion);
        p.setFoto(foto);

        Categoria cat = new Categoria(); cat.setIdCategoria(idCategoria); p.setCategoria(cat);
        Proveedor prov = new Proveedor(); prov.setIdProveedor(idProveedor); p.setProveedor(prov);
        Color color = new Color(); color.setIdColor(idColor); p.setColor(color);
        Talla talla = new Talla(); talla.setIdTalla(idTalla); p.setTalla(talla);

        dao.agregar(p);
        response.sendRedirect("Controlador?accion=listarProductos");
    }

    // MOSTRAR FORMULARIO PARA EDITAR
    else if ("editarProducto".equals(accion)) {
        int id = Integer.parseInt(request.getParameter("id"));
        Producto p = dao.buscarPorId(id);
        request.setAttribute("producto", p);
        request.getRequestDispatcher("Editar_Agregar_PRO.jsp").forward(request, response);
    }

    // ACTUALIZAR PRODUCTO
    else if ("actualizarProducto".equals(accion)) {
        int id = Integer.parseInt(request.getParameter("idProducto"));
        String nombre = request.getParameter("nombre");
        int precio = Integer.parseInt(request.getParameter("precio"));
        String genero = request.getParameter("genero");
        String descripcion = request.getParameter("descripcion");

        Part fotoPart = request.getPart("foto");
        byte[] foto = fotoPart.getSize() > 0 ? fotoPart.getInputStream().readAllBytes() : null;

        int idCategoria = Integer.parseInt(request.getParameter("categoriaId"));
        int idProveedor = Integer.parseInt(request.getParameter("proveedorId"));
        int idColor = Integer.parseInt(request.getParameter("colorId"));
        int idTalla = Integer.parseInt(request.getParameter("tallaId"));

        Producto p = new Producto();
        p.setIdProducto(id);
        p.setNombre(nombre);
        p.setPrecio(precio);
        p.setGenero(genero);
        p.setDescripcion(descripcion);
        if (foto != null) p.setFoto(foto);

        Categoria cat = new Categoria(); cat.setIdCategoria(idCategoria); p.setCategoria(cat);
        Proveedor prov = new Proveedor(); prov.setIdProveedor(idProveedor); p.setProveedor(prov);
        Color color = new Color(); color.setIdColor(idColor); p.setColor(color);
        Talla talla = new Talla(); talla.setIdTalla(idTalla); p.setTalla(talla);

        dao.actualizar(p);
        response.sendRedirect("Controlador?accion=listarProductos");
    }

    // ELIMINAR PRODUCTO
    else if ("eliminarProducto".equals(accion)) {
        int id = Integer.parseInt(request.getParameter("id"));
        dao.eliminar(id);
        response.sendRedirect("Controlador?accion=listarProductos");
    }

    // ACCIÓN DESCONOCIDA
    else {
        response.getWriter().println("Acción no reconocida: " + accion);
    }
    
    
}

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

 
    
}
