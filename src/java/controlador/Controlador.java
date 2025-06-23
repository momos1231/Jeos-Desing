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
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.util.ArrayList;
import java.util.List;
import modelo.Categoria;
import modelo.CategoriaDAO;
import modelo.Color;
import modelo.ColorDAO;
import modelo.Producto;
import modelo.ProductoDAO;
import modelo.Proveedor;
import modelo.ProveedorDAO;
import modelo.Talla;
import modelo.TallaDAO;

/**
 *
 * @author nicolas
 */
@MultipartConfig
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
            List<Producto> lista = dao.listar();    // traemos la lista de productos reales
            request.setAttribute("productos", lista);
            request.getRequestDispatcher("index.jsp").forward(request, response);
        } // MOSTRAR FORMULARIO PARA CREAR
        else if ("nuevoProducto".equals(accion)) {
            CategoriaDAO categoriaDAO = new CategoriaDAO();
            ProveedorDAO proveedorDAO = new ProveedorDAO();
            ColorDAO colorDAO = new ColorDAO();
            TallaDAO tallaDAO = new TallaDAO();

            request.setAttribute("categorias", categoriaDAO.listar());
            request.setAttribute("proveedores", proveedorDAO.listar());
            request.setAttribute("colores", colorDAO.listar());
            request.setAttribute("tallas", tallaDAO.listar());
            request.getRequestDispatcher("Editar_Agregar_PRO.jsp").forward(request, response);
        } // CREAR PRODUCTO
        else if ("agregarProducto".equals(accion)) {
            try {
                // Recogemos los campos
                String nombre = request.getParameter("nombre");
                String precioStr = request.getParameter("precio");
                String genero = request.getParameter("genero");
                String descripcion = request.getParameter("descripcion");

                Part fotoPart = request.getPart("foto");
                String categoriaIdStr = request.getParameter("categoriaId");
                String proveedorIdStr = request.getParameter("proveedorId");
                String colorIdStr = request.getParameter("colorId");
                String tallaIdStr = request.getParameter("tallaId");

                

                // Parseamos campos numéricos solo si no son nulos o vacíos
                int precio = Integer.parseInt(precioStr.trim());
                int idCategoria = Integer.parseInt(categoriaIdStr.trim());
                int idProveedor = Integer.parseInt(proveedorIdStr.trim());
                int idColor = Integer.parseInt(colorIdStr.trim());
                int idTalla = Integer.parseInt(tallaIdStr.trim());

                // Procesamos la imagen
                byte[] foto = null;
                if (fotoPart != null && fotoPart.getSize() > 0) {
                    foto = fotoPart.getInputStream().readAllBytes();
                }

                // Construimos el objeto Producto
                Producto p = new Producto();
                p.setNombre(nombre);
                p.setPrecio(precio);
                p.setGenero(genero);
                p.setDescripcion(descripcion);
                if (foto != null) {
                    p.setFoto(foto);
                }

                Categoria cat = new Categoria();
                cat.setIdCategoria(idCategoria);
                p.setCategoria(cat);
                Proveedor prov = new Proveedor();
                prov.setIdProveedor(idProveedor);
                p.setProveedor(prov);
                Color color = new Color();
                color.setIdColor(idColor);
                p.setColor(color);
                Talla talla = new Talla();
                talla.setIdTalla(idTalla);
                p.setTalla(talla);

                // Intentamos insertar
                boolean insertado = dao.agregar(p);
                System.out.println("¿Se insertó el producto? " + insertado);

                // Redirigimos al listado
                response.sendRedirect("Controlador?accion=listarProductos");
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("Controlador?accion=listarProductos&error=insertError");
            }
        } // MOSTRAR FORMULARIO PARA EDITAR
        else if ("editarProducto".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));
            Producto p = dao.buscarPorId(id);  // Este método debe devolver un Producto con todos sus datos
            CategoriaDAO categoriaDAO = new CategoriaDAO();
            ProveedorDAO proveedorDAO = new ProveedorDAO();
            ColorDAO colorDAO = new ColorDAO();
            TallaDAO tallaDAO = new TallaDAO();

            

            request.setAttribute("categorias", categoriaDAO.listar());
            request.setAttribute("proveedores", proveedorDAO.listar());
            request.setAttribute("colores", colorDAO.listar());
            request.setAttribute("tallas", tallaDAO.listar());

            request.setAttribute("producto", p);
            request.getRequestDispatcher("Editar_Agregar_PRO.jsp").forward(request, response);
        } // ACTUALIZAR PRODUCTO
        else if ("actualizarProducto".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("idProducto"));
            String nombre = request.getParameter("nombre");
            int precio = Integer.parseInt(request.getParameter("precio"));
            String genero = request.getParameter("genero");
            String descripcion = request.getParameter("descripcion");

            Part fotoPart = request.getPart("foto");
            byte[] foto = null;
            if (fotoPart != null && fotoPart.getSize() > 0) {
                foto = fotoPart.getInputStream().readAllBytes();
            }

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
            if (foto != null) {
                p.setFoto(foto); // solo si llega nueva imagen
            }
            Categoria cat = new Categoria();
            cat.setIdCategoria(idCategoria);
            p.setCategoria(cat);

            Proveedor prov = new Proveedor();
            prov.setIdProveedor(idProveedor);
            p.setProveedor(prov);

            Color color = new Color();
            color.setIdColor(idColor);
            p.setColor(color);

            Talla talla = new Talla();
            talla.setIdTalla(idTalla);
            p.setTalla(talla);

            // Usar dao.editar en lugar de dao.actualizar
            dao.editar(p);
            response.sendRedirect("Controlador?accion=listarProductos");
        }
        else if ("agregarColorDesdeProducto".equals(accion)) {
    try {
        String nombreColor = request.getParameter("nombreColor");

        if (nombreColor != null && !nombreColor.trim().isEmpty()) {
            Color color = new Color();
            color.setNombre(nombreColor);

            ColorDAO colorDAO = new ColorDAO();
            colorDAO.agregar(color);
        }

        response.sendRedirect("Controlador?accion=nuevoProducto");
    } catch (Exception e) {
        e.printStackTrace();
        response.sendRedirect("Controlador?accion=nuevoProducto&error=insertColorError");
    }
}
        
        else if ("agregarTallaDesdeProducto".equals(accion)) {
    try {
        String nombreTalla = request.getParameter("nombreTalla");

        if (nombreTalla != null && !nombreTalla.trim().isEmpty()) {
            Talla talla = new Talla();
            talla.setNombre(nombreTalla);

            TallaDAO tallaDAO = new TallaDAO();
            tallaDAO.agregar(talla);
        }

        response.sendRedirect("Controlador?accion=nuevoProducto");
    } catch (Exception e) {
        e.printStackTrace();
        response.sendRedirect("Controlador?accion=nuevoProducto&error=insertTallaError");
    }
}
else if ("agregarProveedorDesdeProducto".equals(accion)) {
    try {
        String nombreProveedor = request.getParameter("nombreProveedor");
        String correoProveedor = request.getParameter("correoProveedor");

        if (nombreProveedor != null && !nombreProveedor.trim().isEmpty() &&
            correoProveedor != null && !correoProveedor.trim().isEmpty()) {

            Proveedor proveedor = new Proveedor();
            proveedor.setNombre_proveedor(nombreProveedor);
            proveedor.setCorreo(correoProveedor);

            ProveedorDAO proveedorDAO = new ProveedorDAO();
            proveedorDAO.agregar(proveedor);
        }

        response.sendRedirect("Controlador?accion=nuevoProducto");
    } catch (Exception e) {
        e.printStackTrace();
        response.sendRedirect("Controlador?accion=nuevoProducto&error=insertProveedorError");
    }
}
        

// ELIMINAR PRODUCTO
        else if ("eliminarProducto".equals(accion)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.eliminar(id);
            response.sendRedirect("Controlador?accion=listarProductos");
        } // ACCIÓN DESCONOCIDA
        else {
            response.getWriter().println("Acción no reconocida: " + accion);
        }

        //acciones Carrito:
        if ("agregarAlCarrito".equals(accion)) {
            int idProducto = Integer.parseInt(request.getParameter("idProducto"));

            // Traemos el producto
            Producto producto = dao.buscarPorId(idProducto);

            // Obtenemos el carrito de la sesión
            List<Producto> carrito = (List<Producto>) request.getSession().getAttribute("carrito");

            if (carrito == null) {
                carrito = new ArrayList<>();
            }

            carrito.add(producto);

            // Lo guardamos en sesión
            request.getSession().setAttribute("carrito", carrito);

            // Redirigimos de vuelta a la lista de productos
            response.sendRedirect("Controlador?accion=listarProductos");
        } else if ("verCarrito".equals(accion)) {
            List<Producto> carrito = (List<Producto>) request.getSession().getAttribute("carrito");
            TallaDAO tallaDao = new TallaDAO();
            ColorDAO colorDao = new ColorDAO();

            List<Talla> tallas = tallaDao.listar();
            List<Color> colores = colorDao.listar();

            request.setAttribute("tallas", tallas);
            request.setAttribute("colores", colores);

            request.setAttribute("carrito", carrito);
            request.getRequestDispatcher("Carrito.jsp").forward(request, response);
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
