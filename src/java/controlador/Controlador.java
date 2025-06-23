/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controlador;

import config.Conexion;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator; // Importa Iterator
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession; // Importar HttpSession

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
import modelo.Cliente; // Importar Cliente
import modelo.Pedido; // Importar Pedido
import modelo.PedidoDAO; // Importar PedidoDAO
import modelo.DetallePedido; // Importar DetallePedido
import modelo.DetallePedidoDAO; // Importar DetallePedidoDAO
import modelo.Factura; // Importar Factura
import modelo.FacturaDAO; // Importar FacturaDAO
import modelo.MetodoPago; // Importar MetodoPago
import modelo.Pago; // Importar Pago
import modelo.PagoDAO; // Importar PagoDAO
import modelo.Empleado; // Importar Empleado (si se usa en Pedido)


public class Controlador extends HttpServlet {

    ProductoDAO pdao = new ProductoDAO();
    // Instancias DAO necesarias
    CategoriaDAO categoriaDAO = new CategoriaDAO();
    ColorDAO colorDAO = new ColorDAO();
    TallaDAO tallaDAO = new TallaDAO();
    ProveedorDAO proveedorDAO = new ProveedorDAO();
    PedidoDAO pedidoDAO = new PedidoDAO();
    DetallePedidoDAO detallePedidoDAO = new DetallePedidoDAO();
    PagoDAO pagoDAO = new PagoDAO();

    List<Producto> productos = new ArrayList<>();
    List<Producto> carrito = new ArrayList<>(); // Inicializa la lista del carrito

    Conexion cn = new Conexion(); // Instancia de la clase de conexión

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String accion = request.getParameter("accion");
        if (accion == null) {
            accion = "listarProductos"; // Acción por defecto si no se especifica
        }

        switch (accion) {
            case "listarProductos":
                productos = pdao.listar();
                request.setAttribute("productos", productos);
                request.getRequestDispatcher("index.jsp").forward(request, response);
                break;

            case "agregarAlCarrito":
                int idProductoCarrito = Integer.parseInt(request.getParameter("idProducto"));
                Producto productoAgregado = pdao.listarId(idProductoCarrito); // Obtener el producto de la DB
                if (productoAgregado != null) {
                    // Obtener el carrito de la sesión o crear uno nuevo
                    HttpSession session = request.getSession();
                    carrito = (List<Producto>) session.getAttribute("carrito");
                    if (carrito == null) {
                        carrito = new ArrayList<>();
                    }
                    carrito.add(productoAgregado);
                    session.setAttribute("carrito", carrito);
                }
                response.sendRedirect("Controlador?accion=listarProductos"); // Redirigir a la vista de productos
                break;

            case "verCarrito":
                // El carrito ya está en la sesión, solo necesitamos enviarlo al JSP
                request.getRequestDispatcher("Carrito.jsp").forward(request, response);
                break;

            case "quitarDelCarrito": // <-- ¡NUEVO CASE PARA QUITAR PRODUCTOS DEL CARRITO!
                int idProductoQuitar = Integer.parseInt(request.getParameter("idProducto"));
                HttpSession session = request.getSession();
                carrito = (List<Producto>) session.getAttribute("carrito");

                if (carrito != null) {
                    // Usar Iterator para eliminar de forma segura mientras se itera
                    Iterator<Producto> iterator = carrito.iterator();
                    while (iterator.hasNext()) {
                        Producto p = iterator.next();
                        if (p.getIdProducto() == idProductoQuitar) {
                            iterator.remove(); // Elimina el producto de la lista
                            break; // Salir después de encontrar y eliminar el primer producto
                        }
                    }
                    session.setAttribute("carrito", carrito); // Actualizar el carrito en la sesión
                }
                response.sendRedirect("Controlador?accion=verCarrito"); // Redirigir de vuelta al carrito
                break;

            case "vaciarCarrito":
                request.getSession().removeAttribute("carrito"); // Eliminar el carrito de la sesión
                response.sendRedirect("Controlador?accion=verCarrito"); // Redirigir de vuelta al carrito
                break;

            case "procesarCompra":
                // Asumiendo que el cliente ya está logueado o tienes un cliente por defecto
                // Si el cliente no está logueado, deberías redirigirlo al login o a un formulario de datos del cliente
                // Por ahora, usaremos un cliente predeterminado (ID 1001) para la demostración.
                Cliente clienteSel = new Cliente();
                clienteSel.setIdCliente(1001L); // Asegúrate de que este cliente exista en tu DB

                HttpSession currentSession = request.getSession();
                List<Producto> carritoCompra = (List<Producto>) currentSession.getAttribute("carrito");

                if (carritoCompra == null || carritoCompra.isEmpty()) {
                    request.setAttribute("mensajeError", "El carrito está vacío. No se puede procesar la compra.");
                    request.getRequestDispatcher("confirmacionCompra.jsp").forward(request, response);
                    return;
                }

                double totalPedido = 0;
                int cantidadTotalItems = 0;
                for (Producto p : carritoCompra) {
                    totalPedido += p.getPrecio(); // p.getPrecio() devuelve int, se suma a double
                    cantidadTotalItems++; // Incrementa la cantidad de ítems
                }

                // 5. Crear el objeto Pedido principal
                Pedido nuevoPedido = new Pedido();
                nuevoPedido.setCliente(clienteSel);
                nuevoPedido.setCantidadItems(cantidadTotalItems); // Asignar la cantidad total de ítems
                nuevoPedido.setTotal(totalPedido); // Asignar el total calculado

                Empleado empleadoDefault = new Empleado();
                empleadoDefault.setIdEmpleado(1); // Empleado por defecto, ID 1
                nuevoPedido.setEmpleado(empleadoDefault);

                boolean transaccionExitosa = true;
                Connection transactionCon = null;

                try {
                    transactionCon = cn.getConnection();
                    transactionCon.setAutoCommit(false); // Iniciar transacción

                    // 6. Insertar el Pedido y obtener el ID generado
                    int idPedidoGenerado = pedidoDAO.generarPedido(nuevoPedido, transactionCon);

                    if (idPedidoGenerado > 0) {
                        nuevoPedido.setIdPedido(idPedidoGenerado); // Establecer el ID generado

                        // 7. Insertar los detalles del pedido
                        for (Producto prodEnCarrito : carritoCompra) { // Usar carritoCompra para evitar conflictos
                            DetallePedido detalle = new DetallePedido();
                            detalle.setPedido(nuevoPedido); // Asociar al pedido recién creado
                            detalle.setProducto(prodEnCarrito); // Asociar el producto
                            // No hay cantidad ni precio unitario en detalle_pedido según tu DB
                            // Si en el futuro agregas esas columnas a detalle_pedido, deberás setearlas aquí.

                            boolean detalleGuardado = detallePedidoDAO.guardarDetallePedido(detalle, transactionCon);
                            if (!detalleGuardado) {
                                transaccionExitosa = false;
                                break;
                            }
                        }

                        // 8. Insertar la Factura
                        if (transaccionExitosa) {
                            Factura nuevaFactura = new Factura();
                            // Formatear la fecha actual a String en formato 'YYYY-MM-DD' para tu DB
                            nuevaFactura.setFechaFactura(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
                            nuevaFactura.setMonto(totalPedido);
                            nuevaFactura.setPedido(nuevoPedido); // Relacionar la factura con el pedido

                            int idFacturaGenerado = new FacturaDAO().generarFactura(nuevaFactura, transactionCon);
                            if (idFacturaGenerado > 0) {
                                nuevaFactura.setIdFactura(idFacturaGenerado); // Establecer el ID generado

                                // 9. Insertar el Pago
                                MetodoPago metodoPagoDefault = new MetodoPago();
                                metodoPagoDefault.setIdMetodoPago(1); // Método de pago por defecto (ej: Tarjeta de Crédito)

                                Pago nuevoPago = new Pago();
                                nuevoPago.setFechaPago(LocalDate.now()); // fecha_pago es DATE en la DB
                                nuevoPago.setMontoPago(totalPedido); // monto_pago es DECIMAL(10,0)
                                nuevoPago.setEstado("Pagado");
                                nuevoPago.setFactura(nuevaFactura); // Relacionar el pago con la factura
                                nuevoPago.setMetodoPago(metodoPagoDefault);

                                int idPagoGenerado = pagoDAO.generarPago(nuevoPago, transactionCon);

                                if (idPagoGenerado > 0) {
                                    transactionCon.commit(); // Confirmar la transacción si todo fue bien
                                    currentSession.removeAttribute("carrito"); // Vaciar el carrito después de la compra exitosa
                                    request.setAttribute("mensajeExito", "¡Compra realizada con éxito! Pedido Nro: " + idPedidoGenerado + ", Factura Nro: " + idFacturaGenerado + ", Pago Nro: " + idPagoGenerado);
                                    request.getRequestDispatcher("confirmacionCompra.jsp").forward(request, response);
                                } else {
                                    transaccionExitosa = false;
                                    transactionCon.rollback(); // Deshacer si falla el pago
                                    request.setAttribute("mensajeError", "Error al generar el pago. La compra ha sido cancelada.");
                                    request.getRequestDispatcher("confirmacionCompra.jsp").forward(request, response);
                                }

                            } else {
                                transaccionExitosa = false;
                                transactionCon.rollback(); // Deshacer si falla la factura
                                request.setAttribute("mensajeError", "Error al generar la factura. La compra ha sido cancelada.");
                                request.getRequestDispatcher("confirmacionCompra.jsp").forward(request, response);
                            }
                        } else {
                            transactionCon.rollback(); // Deshacer si falla el detalle del pedido
                            request.setAttribute("mensajeError", "Error al guardar los detalles del pedido. La compra ha sido cancelada.");
                            request.getRequestDispatcher("confirmacionCompra.jsp").forward(request, response);
                        }

                    } else {
                        transactionCon.rollback(); // Deshacer si falla el pedido principal
                        request.setAttribute("mensajeError", "No se pudo generar el pedido principal. Inténtelo de nuevo.");
                        request.getRequestDispatcher("confirmacionCompra.jsp").forward(request, response);
                    }

                } catch (Exception e) {
                    try {
                        if (transactionCon != null) {
                            transactionCon.rollback(); // Asegurarse de hacer rollback en caso de cualquier excepción
                        }
                    } catch (SQLException ex) {
                        System.err.println("Error al hacer rollback: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                    System.err.println("Excepción al procesar la compra: " + e.getMessage());
                    e.printStackTrace();
                    request.setAttribute("mensajeError", "Ocurrió un error inesperado al procesar su compra. " + e.getMessage());
                    request.getRequestDispatcher("confirmacionCompra.jsp").forward(request, response);
                } finally {
                    try {
                        if (transactionCon != null && !transactionCon.isClosed()) {
                            transactionCon.setAutoCommit(true); // Restaurar auto-commit
                            transactionCon.close(); // Cerrar conexión
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                break;
            // ... (otros casos como listarCamisas, listarPantalones, etc.) ...
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
                    int precio = Integer.parseInt(request.getParameter("precio")); // Asegúrate de que esto sea int
                    String genero = request.getParameter("genero");
                    String descripcion = request.getParameter("descripcion");
                    // Asumiendo que la foto se maneja por separado si es un byte[]
                    int idCategoria = Integer.parseInt(request.getParameter("idCategoria"));
                    int idProveedor = Integer.parseInt(request.getParameter("idProveedor"));
                    int idColor = Integer.parseInt(request.getParameter("idColor"));
                    int idTalla = Integer.parseInt(request.getParameter("idTalla"));

                    Producto nuevoP = new Producto();
                    nuevoP.setNombre(nombre);
                    nuevoP.setPrecio(precio);
                    nuevoP.setGenero(genero);
                    nuevoP.setDescripcion(descripcion);
                    // nuevoP.setFoto(...) // Si manejas la foto aquí

                    // Crear objetos completos para las FK
                    nuevoP.setCategoria(categoriaDAO.listarId(idCategoria));
                    nuevoP.setProveedor(proveedorDAO.listarId(idProveedor));
                    nuevoP.setColor(colorDAO.listarId(idColor));
                    nuevoP.setTalla(tallaDAO.listarId(idTalla));

                    pdao.agregar(nuevoP); // Llama al método agregar del DAO
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
                    int precio = Integer.parseInt(request.getParameter("precio")); // Asegúrate de que sea int
                    String genero = request.getParameter("genero");
                    String descripcion = request.getParameter("descripcion");
                    // No se está manejando la foto en la actualización aquí, se necesitaría un input file en el form
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
                    // Puedes redirigir a una página de error o de vuelta al formulario de edición con el error
                    request.getRequestDispatcher("editarProducto.jsp").forward(request, response);
                }
                break;

            case "agregarTallaDesdeProducto": // Nuevo case
                try {
                    String nombreTalla = request.getParameter("nombreTalla");
                    if (nombreTalla != null && !nombreTalla.trim().isEmpty()) {
                        Talla talla = new Talla();
                        talla.setNombre(nombreTalla);
                        tallaDAO.agregar(talla);
                    }
                    response.sendRedirect("Controlador?accion=nuevoProducto"); // Redirige a la página de nuevo producto para que se actualicen las opciones
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("Controlador?accion=nuevoProducto&insertTallaError");
                }
                break;

            case "agregarColorDesdeProducto": // Nuevo case
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

            case "agregarCategoriaDesdeProducto": // Nuevo case
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
            case "agregarProveedorDesdeProducto": // Nuevo case
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
        return "Short description";
    }
}