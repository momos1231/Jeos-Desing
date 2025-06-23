package controlador;

import config.Conexion;
import java.io.IOException;
// import java.io.OutputStream; // Ya no necesario para PDF
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// Importar tus modelos y DAOs
import modelo.Producto;
import modelo.ProductoDAO;
import modelo.Categoria;
import modelo.CategoriaDAO;
import modelo.Color;
import modelo.ColorDAO;
import modelo.Talla;
import modelo.TallaDAO;
import modelo.Proveedor;
import modelo.ProveedorDAO;
import modelo.Cliente;
import modelo.ClienteDAO;
import modelo.Pedido;
import modelo.PedidoDAO;
import modelo.DetallePedido;
import modelo.DetallePedidoDAO;
import modelo.Factura;
import modelo.FacturaDAO;
import modelo.MetodoPago;
import modelo.Pago;
import modelo.PagoDAO;
import modelo.Empleado;
import modelo.ItemCarrito;

// Las importaciones de iText ya no son necesarias si se elimina la funcionalidad de PDF
// import com.itextpdf.text.DocumentException;
// import utilidades.PdfGenerator; // Ya no necesario si no se genera PDF


public class Controlador extends HttpServlet {

    ProductoDAO pdao = new ProductoDAO();
    CategoriaDAO categoriaDAO = new CategoriaDAO();
    ColorDAO colorDAO = new ColorDAO();
    TallaDAO tallaDAO = new TallaDAO();
    ProveedorDAO proveedorDAO = new ProveedorDAO();
    PedidoDAO pedidoDAO = new PedidoDAO();
    DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO();
    PagoDAO pagoDAO = new PagoDAO();
    ClienteDAO clienteDAO = new ClienteDAO(); // Instancia de ClienteDAO

    Conexion cn = new Conexion();

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String accion = request.getParameter("accion");
        if (accion == null || accion.isEmpty()) {
            accion = "listarProductos";
        }

        HttpSession session = request.getSession();

        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }

        switch (accion) {
            case "listarProductos":
                List<Producto> productos = pdao.listar();
                request.setAttribute("productos", productos);
                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;

            case "agregarAlCarrito":
                int idProductoAgregar = Integer.parseInt(request.getParameter("idProducto"));
                int cantidadAgregar = 1;
                String tallaAgregar = request.getParameter("talla");
                if (tallaAgregar == null || tallaAgregar.isEmpty()) {
                    tallaAgregar = "Única";
                }

                Producto productoBase = pdao.listarId(idProductoAgregar);

                if (productoBase != null) {
                    boolean itemFound = false;
                    for (ItemCarrito item : carrito) {
                        if (item.getProducto().getIdProducto() == idProductoAgregar && item.getTalla().equals(tallaAgregar)) {
                            item.setCantidad(item.getCantidad() + cantidadAgregar);
                            itemFound = true;
                            break;
                        }
                    }
                    if (!itemFound) {
                        ItemCarrito newItem = new ItemCarrito(productoBase, cantidadAgregar, tallaAgregar);
                        carrito.add(newItem);
                    }
                    session.setAttribute("carrito", carrito);
                }
                response.sendRedirect("Controlador?accion=listarProductos");
                break;

            case "verCarrito":
                request.getRequestDispatcher("Carrito.jsp").forward(request, response);
                break;

            case "quitar":
                int idProductoQuitar = Integer.parseInt(request.getParameter("idProducto"));
                String tallaQuitar = request.getParameter("talla");

                if (carrito != null) {
                    Iterator<ItemCarrito> iterator = carrito.iterator();
                    while (iterator.hasNext()) {
                        ItemCarrito item = iterator.next();
                        if (item.getProducto().getIdProducto() == idProductoQuitar && item.getTalla().equals(tallaQuitar)) {
                            iterator.remove();
                            break;
                        }
                    }
                    session.setAttribute("carrito", carrito);
                }
                response.sendRedirect("Controlador?accion=verCarrito");
                break;

            case "vaciar":
                session.removeAttribute("carrito");
                response.sendRedirect("Controlador?accion=verCarrito");
                break;

            case "actualizarCantidadTalla":
                for (int i = 0; ; i++) {
                    String idProductoParam = request.getParameter("idProducto_" + i);
                    String tallaOriginalParam = request.getParameter("tallaOriginal_" + i);
                    String nuevaTallaParam = request.getParameter("talla_" + i);
                    String nuevaCantidadParam = request.getParameter("cantidad_" + i);

                    if (idProductoParam == null) {
                        break;
                    }

                    try {
                        int idProducto = Integer.parseInt(idProductoParam);
                        String tallaOriginal = tallaOriginalParam;
                        String nuevaTalla = nuevaTallaParam;
                        int nuevaCantidad = Integer.parseInt(nuevaCantidadParam);

                        boolean itemUpdated = false;
                        Iterator<ItemCarrito> it = carrito.iterator();
                        while(it.hasNext()){
                            ItemCarrito item = it.next();
                            if (item.getProducto().getIdProducto() == idProducto && item.getTalla().equals(tallaOriginal)) {
                                item.setTalla(nuevaTalla);
                                item.setCantidad(nuevaCantidad);
                                itemUpdated = true;
                                break;
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato al actualizar cantidad/talla para índice " + i + ": " + e.getMessage());
                    }
                }
                session.setAttribute("carrito", carrito);
                response.sendRedirect("Controlador?accion=verCarrito");
                break;

            case "procesarCompra":
                String nombreCliente = request.getParameter("nombre");
                String emailCliente = request.getParameter("email");
                String direccionCliente = request.getParameter("direccion");
                String edadClienteStr = request.getParameter("edad");

                if (nombreCliente == null || nombreCliente.isEmpty() ||
                    emailCliente == null || emailCliente.isEmpty() ||
                    direccionCliente == null || direccionCliente == null ||
                    edadClienteStr == null || edadClienteStr.isEmpty()) {
                    request.setAttribute("mensajeError", "Por favor, complete todos los campos de información del cliente.");
                    request.getRequestDispatcher("Carrito.jsp").forward(request, response);
                    return;
                }

                int edadCliente;
                try {
                    edadCliente = Integer.parseInt(edadClienteStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("mensajeError", "La edad debe ser un número válido.");
                    request.getRequestDispatcher("Carrito.jsp").forward(request, response);
                    return;
                }

                if (carrito.isEmpty()) {
                    request.setAttribute("mensajeError", "El carrito está vacío. No se puede procesar la compra.");
                    request.getRequestDispatcher("Carrito.jsp").forward(request, response);
                    return;
                }

                Connection transactionCon = null;

                try {
                    transactionCon = cn.getConnection();
                    transactionCon.setAutoCommit(false);

                    Cliente clienteSel = clienteDAO.buscarClientePorEmail(emailCliente, transactionCon);

                    if (clienteSel == null) {
                        clienteSel = new Cliente();
                        clienteSel.setNombre(nombreCliente);
                        clienteSel.setDireccion(direccionCliente);
                        clienteSel.setEmail(emailCliente);
                        clienteSel.setEdad(edadCliente);

                        int idClienteGenerado = clienteDAO.insertarCliente(clienteSel, transactionCon);
                        if (idClienteGenerado == 0) {
                            throw new SQLException("Error al insertar un nuevo cliente en la base de datos.");
                        }
                    }

                    double totalPedido = 0;
                    int cantidadTotalItems = 0;
                    for (ItemCarrito item : carrito) {
                        totalPedido += item.getSubtotal();
                        cantidadTotalItems += item.getCantidad();
                    }

                    Pedido nuevoPedido = new Pedido();
                    nuevoPedido.setCliente(clienteSel);
                    nuevoPedido.setCantidadItems(cantidadTotalItems);
                    nuevoPedido.setTotal(totalPedido);
                    // No se setea fechaPedido en Pedido, ya que tu tabla 'pedido' no tiene esa columna.

                    Empleado empleadoDefault = new Empleado();
                    empleadoDefault.setIdEmpleado(1);
                    nuevoPedido.setEmpleado(empleadoDefault);

                    int idPedidoGenerado = pedidoDAO.generarPedido(nuevoPedido, transactionCon);

                    if (idPedidoGenerado > 0) {
                        nuevoPedido.setIdPedido(idPedidoGenerado);

                        for (ItemCarrito item : carrito) {
                            DetallePedido detalle = new DetallePedido();
                            detalle.setPedido(nuevoPedido);
                            detalle.setProducto(item.getProducto());
                            // No se setea cantidad y precioUnitario en DetallePedido,
                            // ya que tu tabla 'detalle_pedido' no tiene esas columnas.

                            boolean detalleGuardado = detallePedidoDAO.guardarDetallePedido(detalle, transactionCon);
                            if (!detalleGuardado) {
                                throw new SQLException("Error al guardar el detalle del pedido para producto ID: " + item.getProducto().getIdProducto());
                            }
                        }

                        Factura nuevaFactura = new Factura();
                        nuevaFactura.setFechaFactura(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                        nuevaFactura.setMonto(totalPedido);
                        nuevaFactura.setPedido(nuevoPedido);

                        int idFacturaGenerado = new FacturaDAO().generarFactura(nuevaFactura, transactionCon);

                        if (idFacturaGenerado > 0) {
                            nuevaFactura.setIdFactura(idFacturaGenerado);

                            MetodoPago metodoPagoDefault = new MetodoPago();
                            metodoPagoDefault.setIdMetodoPago(1);

                            Pago nuevoPago = new Pago();
                            nuevoPago.setFechaPago(LocalDate.now());
                            nuevoPago.setMontoPago(totalPedido);
                            nuevoPago.setEstado("Pagado");
                            nuevoPago.setFactura(nuevaFactura);
                            nuevoPago.setMetodoPago(metodoPagoDefault);

                            int idPagoGenerado = pagoDAO.generarPago(nuevoPago, transactionCon);

                            if (idPagoGenerado > 0) {
                                transactionCon.commit(); // Confirmar la transacción
                                session.removeAttribute("carrito"); // Limpiar carrito

                                // Redirigir a una página de confirmación simple, sin PDF
                                request.setAttribute("mensajeExito", "¡Compra realizada con éxito! Pedido Nro: " + idPedidoGenerado + ", Factura Nro: " + idFacturaGenerado + ", Pago Nro: " + idPagoGenerado);
                                request.getRequestDispatcher("confirmacionCompra.jsp").forward(request, response);
                                return; // Terminar el proceso de la solicitud
                            } else {
                                
                                
                            }
                        } else {
                            throw new SQLException("Error al generar la factura.");
                        }
                    } else {
                        throw new SQLException("No se pudo generar el pedido principal.");
                    }

                } catch (Exception e) {
                    try {
                        if (transactionCon != null) {
                            transactionCon.rollback();
                        }
                    } catch (SQLException ex) {
                        System.err.println("Error al hacer rollback: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                    System.err.println("Excepción al procesar la compra: " + e.getMessage());
                    e.printStackTrace();
                    request.setAttribute("mensajeError", "Ocurrió un error inesperado al procesar su compra: " + e.getMessage());
                    request.getRequestDispatcher("Carrito.jsp").forward(request, response);
                } finally {
                    try {
                        if (transactionCon != null && !transactionCon.isClosed()) {
                            transactionCon.setAutoCommit(true);
                            transactionCon.close();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;

            case "nuevoProducto":
                request.setAttribute("categorias", categoriaDAO.listar());
                request.setAttribute("colores", colorDAO.listar());
                request.setAttribute("tallas", tallaDAO.listar());
                request.setAttribute("proveedores", proveedorDAO.listar());
                request.getRequestDispatcher("addProducto.jsp").forward(request, response);
                break;

            case "agregarProducto":
                try {
                    String nombre = request.getParameter("nombre");
                    int precio = Integer.parseInt(request.getParameter("precio"));
                    String genero = request.getParameter("genero");
                    String descripcion = request.getParameter("descripcion");
                    int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
                    int idProveedor = Integer.parseInt(request.getParameter("idProveedor"));
                    int idColor = Integer.parseInt(request.getParameter("idColor"));
                    int idTalla = Integer.parseInt(request.getParameter("idTalla"));

                    Producto nuevoP = new Producto();
                    nuevoP.setNombre(nombre);
                    nuevoP.setPrecio(precio);
                    nuevoP.setGenero(genero);
                    nuevoP.setDescripcion(descripcion);

                    nuevoP.setCategoria(categoriaDAO.listarId(idCategoria));
                    nuevoP.setProveedor(proveedorDAO.listarId(idProveedor));
                    nuevoP.setColor(colorDAO.listarId(idColor));
                    nuevoP.setTalla(tallaDAO.listarId(idTalla));

                    pdao.agregar(nuevoP);
                    response.sendRedirect("Controlador?accion=listarProductos");
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("mensajeError", "Error al agregar el producto: " + e.getMessage());
                    request.getRequestDispatcher("addProducto.jsp").forward(request, response);
                }
                break;

            case "eliminarProducto":
                int idEliminar = Integer.parseInt(request.getParameter("id"));
                pdao.eliminar(idEliminar);
                response.sendRedirect("Controlador?accion=listarProductos");
                break;

            case "editarProducto":
                int idEditar = Integer.parseInt(request.getParameter("id"));
                Producto pEditar = pdao.listarId(idEditar);
                request.setAttribute("producto", pEditar);
                request.setAttribute("categorias", categoriaDAO.listar());
                request.setAttribute("colores", colorDAO.listar());
                request.setAttribute("tallas", tallaDAO.listar());
                request.setAttribute("proveedores", proveedorDAO.listar());
                request.getRequestDispatcher("editarProducto.jsp").forward(request, response);
                break;

            case "actualizarProducto":
                try {
                    int idProducto = Integer.parseInt(request.getParameter("idProducto"));
                    String nombre = request.getParameter("nombre");
                    int precio = Integer.parseInt(request.getParameter("precio"));
                    String genero = request.getParameter("genero");
                    String descripcion = request.getParameter("descripcion");
                    int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
                    int idProveedor = Integer.parseInt(request.getParameter("idProveedor"));
                    int idColor = Integer.parseInt(request.getParameter("idColor"));
                    int idTalla = Integer.parseInt(request.getParameter("idTalla"));

                    Producto pActualizar = new Producto();
                    pActualizar.setIdProducto(idProducto);
                    pActualizar.setNombre(nombre);
                    pActualizar.setPrecio(precio);
                    pActualizar.setGenero(genero);
                    pActualizar.setDescripcion(descripcion);

                    pActualizar.setCategoria(categoriaDAO.listarId(idCategoria));
                    pActualizar.setProveedor(proveedorDAO.listarId(idProveedor));
                    pActualizar.setColor(colorDAO.listarId(idColor));
                    pActualizar.setTalla(tallaDAO.listarId(idTalla));

                    pdao.actualizar(pActualizar);
                    response.sendRedirect("Controlador?accion=listarProductos");
                } catch (Exception e) {
                    e.printStackTrace();
                    request.setAttribute("mensajeError", "Error al actualizar el producto: " + e.getMessage());
                    request.getRequestDispatcher("editarProducto.jsp").forward(request, response);
                }
                break;

            case "agregarTallaDesdeProducto":
                try {
                    String nombreTalla = request.getParameter("nombreTalla");
                    if (nombreTalla != null && !nombreTalla.trim().isEmpty()) {
                        Talla talla = new Talla();
                        talla.setNombre(nombreTalla);
                        tallaDAO.agregar(talla);
                    }
                    response.sendRedirect("Controlador?accion=nuevoProducto");
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("Controlador?accion=nuevoProducto&insertTallaError");
                }
                break;

            case "agregarColorDesdeProducto":
                try {
                    String nombreColor = request.getParameter("nombreColor");
                    if (nombreColor != null && !nombreColor.trim().isEmpty()) {
                        Color color = new Color();
                        color.setNombre(nombreColor);
                        colorDAO.agregar(color);
                    }
                    response.sendRedirect("Controlador?accion=nuevoProducto");
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("Controlador?accion=nuevoProducto&insertColorError");
                }
                break;

            case "agregarCategoriaDesdeProducto":
                try {
                    String nombreCategoria = request.getParameter("nombreCategoria");
                    if (nombreCategoria != null && !nombreCategoria.trim().isEmpty()) {
                        Categoria categoria = new Categoria();
                        categoria.setNombre(nombreCategoria);
                        categoriaDAO.agregar(categoria);
                    }
                    response.sendRedirect("Controlador?accion=nuevoProducto");
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("Controlador?accion=nuevoProducto&insertCategoriaError");
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
                        proveedorDAO.agregar(proveedor);
                    }
                    response.sendRedirect("Controlador?accion=nuevoProducto");
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("Controlador?accion=nuevoProducto&insertProveedorError");
                }
                break;

            case "ControladorImg":
                // Este caso es para mostrar imágenes. Se asume que tienes un método para obtener la foto del producto.
                // Es mejor tener un servlet dedicado para imágenes para un manejo más limpio y caché,
                // pero se mantiene aquí si lo estás manejando así.
                int idImg = Integer.parseInt(request.getParameter("id"));
                Producto imgProd = pdao.listarId(idImg);
                if (imgProd != null && imgProd.getFoto() != null) {
                    response.setContentType("image/jpeg");
                    try (java.io.OutputStream out = response.getOutputStream()) { // Asegurar importación de java.io.OutputStream
                        out.write(imgProd.getFoto());
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
                break;

            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Acción no reconocida: " + accion);
                break;
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
        return "Controlador principal para la aplicación de E-commerce";
    }
}