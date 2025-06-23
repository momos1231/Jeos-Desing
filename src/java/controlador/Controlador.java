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
import java.util.Iterator;

@MultipartConfig
@WebServlet(name = "Controlador_1", urlPatterns = {"/Controlador_1"})
public class Controlador extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        // DECLARA LAS INSTANCIAS DE DAO UNA SOLA VEZ AL PRINCIPIO
        ProductoDAO productoDAO = new ProductoDAO(); // Renombrado para evitar conflicto con "dao" si lo tienes en otro lado
        CategoriaDAO categoriaDAO = new CategoriaDAO();
        ProveedorDAO proveedorDAO = new ProveedorDAO();
        ColorDAO colorDAO = new ColorDAO();
        TallaDAO tallaDAO = new TallaDAO();

        if (accion != null) {
            switch (accion) {
                case "listarProductos":
                    List<Producto> lista = productoDAO.listar();
                    request.setAttribute("productos", lista);
                    request.getRequestDispatcher("index.jsp").forward(request, response);
                    break;
                case "nuevoProducto":
                    request.setAttribute("categorias", categoriaDAO.listar());
                    request.setAttribute("proveedores", proveedorDAO.listar());
                    request.setAttribute("colores", colorDAO.listar());
                    request.setAttribute("tallas", tallaDAO.listar());
                    request.getRequestDispatcher("Editar_Agregar_PRO.jsp").forward(request, response);
                    break;
                case "agregarProducto":
                    try {
                        String nombre = request.getParameter("nombre");
                        String precioStr = request.getParameter("precio");
                        String genero = request.getParameter("genero");
                        String descripcion = request.getParameter("descripcion");
                        Part fotoPart = request.getPart("foto");
                        String categoriaIdStr = request.getParameter("categoriaId");
                        String proveedorIdStr = request.getParameter("proveedorId");
                        String colorIdStr = request.getParameter("colorId");
                        String tallaIdStr = request.getParameter("tallaId");

                        int precio = Integer.parseInt(precioStr.trim());
                        int idCategoria = Integer.parseInt(categoriaIdStr.trim());
                        int idProveedor = Integer.parseInt(proveedorIdStr.trim());
                        int idColor = Integer.parseInt(colorIdStr.trim());
                        int idTalla = Integer.parseInt(tallaIdStr.trim());

                        byte[] foto = null;
                        if (fotoPart != null && fotoPart.getSize() > 0) {
                            foto = fotoPart.getInputStream().readAllBytes();
                        }

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

                        boolean insertado = productoDAO.agregar(p); // Usar productoDAO
                        System.out.println("¿Se insertó el producto? " + insertado);

                        response.sendRedirect("Controlador?accion=listarProductos");
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.sendRedirect("Controlador?accion=listarProductos&error=insertError");
                    }
                    break;
                case "editarProducto":
                    int id = Integer.parseInt(request.getParameter("id"));
                    Producto p = productoDAO.buscarPorId(id); // Usar productoDAO

                    request.setAttribute("categorias", categoriaDAO.listar());
                    request.setAttribute("proveedores", proveedorDAO.listar());
                    request.setAttribute("colores", colorDAO.listar());
                    request.setAttribute("tallas", tallaDAO.listar());
                    request.setAttribute("producto", p);
                    request.getRequestDispatcher("Editar_Agregar_PRO.jsp").forward(request, response);
                    break;
                case "actualizarProducto":
                    try {
                        int idProd = Integer.parseInt(request.getParameter("idProducto"));
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

                        Producto productoActualizar = new Producto();
                        productoActualizar.setIdProducto(idProd);
                        productoActualizar.setNombre(nombre);
                        productoActualizar.setPrecio(precio);
                        productoActualizar.setGenero(genero);
                        productoActualizar.setDescripcion(descripcion);
                        if (foto != null) {
                            productoActualizar.setFoto(foto);
                        }

                        Categoria catAct = new Categoria();
                        catAct.setIdCategoria(idCategoria);
                        productoActualizar.setCategoria(catAct);

                        Proveedor provAct = new Proveedor();
                        provAct.setIdProveedor(idProveedor);
                        productoActualizar.setProveedor(provAct);

                        Color colorAct = new Color();
                        colorAct.setIdColor(idColor);
                        productoActualizar.setColor(colorAct);

                        Talla tallaAct = new Talla();
                        tallaAct.setIdTalla(idTalla);
                        productoActualizar.setTalla(tallaAct);

                        productoDAO.editar(productoActualizar); // Usar productoDAO
                        response.sendRedirect("Controlador?accion=listarProductos");
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.sendRedirect("Controlador?accion=listarProductos&error=updateError");
                    }
                    break;
                case "agregarColorDesdeProducto":
                    try {
                        String nombreColor = request.getParameter("nombreColor");
                        if (nombreColor != null && !nombreColor.trim().isEmpty()) {
                            Color color = new Color();
                            color.setNombre(nombreColor);
                            colorDAO.agregar(color); // USAR la instancia ya declarada
                        }
                        response.sendRedirect("Controlador?accion=nuevoProducto");
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.sendRedirect("Controlador?accion=nuevoProducto&error=insertColorError");
                    }
                    break;
                case "agregarTallaDesdeProducto":
                    try {
                        String nombreTalla = request.getParameter("nombreTalla");
                        if (nombreTalla != null && !nombreTalla.trim().isEmpty()) {
                            Talla talla = new Talla();
                            talla.setNombre(nombreTalla);
                            tallaDAO.agregar(talla); // USAR la instancia ya declarada
                        }
                        response.sendRedirect("Controlador?accion=nuevoProducto");
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.sendRedirect("Controlador?accion=nuevoProducto&error=insertTallaError");
                    }
                    break;
                case "agregarProveedorDesdeProducto":
                    try {
                        String nombreProveedor = request.getParameter("nombreProveedor");
                        String correoProveedor = request.getParameter("correoProveedor");
                        if (nombreProveedor != null && !nombreProveedor.trim().isEmpty() &&
                            correoProveedor != null && !correoProveedor.trim().isEmpty()) {
                            Proveedor proveedor = new Proveedor();
                            proveedor.setNombre_proveedor(nombreProveedor);
                            proveedor.setCorreo(correoProveedor);
                            proveedorDAO.agregar(proveedor); // USAR la instancia ya declarada
                        }
                        response.sendRedirect("Controlador?accion=nuevoProducto");
                    } catch (Exception e) {
                        e.printStackTrace();
                        response.sendRedirect("Controlador?accion=nuevoProducto&error=insertProveedorError");
                    }
                    break;
                case "eliminarProducto":
                    int idEliminarDB = Integer.parseInt(request.getParameter("id"));
                    productoDAO.eliminar(idEliminarDB); // Usar productoDAO
                    response.sendRedirect("Controlador?accion=listarProductos");
                    break;

                // --- ACCIONES DEL CARRITO ---
                case "agregarAlCarrito":
                    int idProductoAgregar = Integer.parseInt(request.getParameter("idProducto"));
                    Producto productoAgregar = productoDAO.buscarPorId(idProductoAgregar); // Usar productoDAO
                    List<Producto> carritoAgregar = (List<Producto>) request.getSession().getAttribute("carrito");

                    if (carritoAgregar == null) {
                        carritoAgregar = new ArrayList<>();
                    }
                    carritoAgregar.add(productoAgregar);
                    request.getSession().setAttribute("carrito", carritoAgregar);
                    response.sendRedirect("Controlador?accion=listarProductos");
                    break;
                case "verCarrito":
                    List<Producto> carritoVer = (List<Producto>) request.getSession().getAttribute("carrito");
                    
                    // Ya tenemos las instancias de DAO declaradas al principio
                    List<Talla> tallas = tallaDAO.listar();
                    List<Color> colores = colorDAO.listar();

                    request.setAttribute("tallas", tallas);
                    request.setAttribute("colores", colores);
                    request.setAttribute("carrito", carritoVer);
                    request.getRequestDispatcher("Carrito.jsp").forward(request, response);
                    break;
                case "eliminarDelCarrito":
                    int idProductoEliminarCarrito = Integer.parseInt(request.getParameter("idProducto"));
                    List<Producto> carritoSesion = (List<Producto>) request.getSession().getAttribute("carrito");

                    if (carritoSesion != null) {
                        Iterator<Producto> iterator = carritoSesion.iterator();
                        while (iterator.hasNext()) {
                            Producto pCarrito = iterator.next();
                            if (pCarrito.getIdProducto() == idProductoEliminarCarrito) {
                                iterator.remove();
                                break;
                            }
                        }
                        request.getSession().setAttribute("carrito", carritoSesion);
                    }
                    response.sendRedirect("Controlador?accion=verCarrito");
                    break;
                default:
                    response.getWriter().println("Acción no reconocida: " + accion);
                    break;
            }
        } else {
            response.sendRedirect("Controlador?accion=listarProductos");
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controlador principal para la gestión de productos y carrito.";
    }
}