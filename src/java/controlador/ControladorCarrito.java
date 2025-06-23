/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// Archivo: ControladorCarrito.java
package controlador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import modelo.Producto; 
import modelo.ProductoDAO; 
import modelo.ItemCarrito; 


@WebServlet(name = "ControladorCarrito", urlPatterns = {"/ControladorCarrito"})
public class ControladorCarrito extends HttpServlet {

    ProductoDAO pdao = new ProductoDAO(); // Instancia de tu DAO de Productos

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String accion = request.getParameter("accion");
        HttpSession session = request.getSession();

        // Recupera el carrito de la sesión
        List<ItemCarrito> carrito = (List<ItemCarrito>) session.getAttribute("carrito");
        if (carrito == null) {
            carrito = new ArrayList<>();
            session.setAttribute("carrito", carrito);
        }

        switch (accion) {
            case "agregar":
                // Lógica existente para agregar producto
                int idProductoAgregar = Integer.parseInt(request.getParameter("idProducto"));
                String tallaAgregar = request.getParameter("talla"); // Obtener la talla al agregar
                int cantidadAgregar = 1; // Por defecto 1 al agregar, o puedes permitir elegir en la página de producto

                Producto p = pdao.listarId(idProductoAgregar); // Obtener el producto de la DB

                // Buscar si el producto con la misma talla ya está en el carrito
                boolean encontrado = false;
                for (ItemCarrito item : carrito) {
                    if (item.getProducto().getIdProducto() == idProductoAgregar && item.getTalla().equals(tallaAgregar)) {
                        item.setCantidad(item.getCantidad() + cantidadAgregar);
                        encontrado = true;
                        break;
                    }
                }
                if (!encontrado) {
                    // Si no está, crear un nuevo ItemCarrito
                    ItemCarrito newItem = new ItemCarrito(p, cantidadAgregar, tallaAgregar);
                    carrito.add(newItem);
                }
                response.sendRedirect("carrito.jsp"); // Redirigir al carrito
                break;

            case "quitar":
                // Lógica para quitar un producto (ahora también por talla)
                int idProductoQuitar = Integer.parseInt(request.getParameter("idProducto"));
                String tallaQuitar = request.getParameter("talla"); // Obtener la talla para quitar
                Iterator<ItemCarrito> iterator = carrito.iterator();
                while (iterator.hasNext()) {
                    ItemCarrito item = iterator.next();
                    if (item.getProducto().getIdProducto() == idProductoQuitar && item.getTalla().equals(tallaQuitar)) {
                        iterator.remove();
                        break;
                    }
                }
                response.sendRedirect("carrito.jsp");
                break;

            case "vaciar":
                carrito.clear();
                response.sendRedirect("carrito.jsp");
                break;

            case "actualizarCantidadTalla":
                // Nueva lógica para actualizar cantidad y talla para cada item
                // Se itera sobre los parámetros enviados por el formulario
                for (int i = 0; ; i++) {
                    String idProductoParam = request.getParameter("idProducto_" + i);
                    String tallaOriginalParam = request.getParameter("tallaOriginal_" + i); // Talla usada para identificar
                    String nuevaTallaParam = request.getParameter("talla_" + i);
                    String nuevaCantidadParam = request.getParameter("cantidad_" + i);

                    if (idProductoParam == null) { // No hay más items en el formulario
                        break;
                    }

                    try {
                        int idProducto = Integer.parseInt(idProductoParam);
                        String tallaOriginal = tallaOriginalParam;
                        String nuevaTalla = nuevaTallaParam;
                        int nuevaCantidad = Integer.parseInt(nuevaCantidadParam);

                        // Encontrar y actualizar el item en el carrito
                        for (ItemCarrito item : carrito) {
                            // Identificamos el item por su ID de producto Y su talla original
                            if (item.getProducto().getIdProducto() == idProducto && item.getTalla().equals(tallaOriginal)) {
                                item.setTalla(nuevaTalla);
                                item.setCantidad(nuevaCantidad);
                                // Revisa si necesitas recalcular el precio si la talla afecta el precio
                                // Si cambiaste la talla y el precio se modifica, deberías obtener el nuevo producto o precio aquí:
                                // item.setProducto(pdao.obtenerProductoPorIdYTalla(idProducto, nuevaTalla)); // Método ficticio
                                break; // Item actualizado, salimos del bucle interno
                            }
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Error de formato al actualizar cantidad/talla: " + e.getMessage());
                        // Manejar el error, quizás loggearlo o notificar al usuario
                    }
                }
                response.sendRedirect("carrito.jsp"); // Redirigir de vuelta al carrito actualizado
                break;

            case "procederPago":
                // Lógica para proceder al pago
                // Aquí podrías validar el carrito final y redirigir a la página de pago
                response.sendRedirect("paginaDePago.jsp");
                break;

            default:
                // Si no se especifica una acción, mostrar el carrito
                response.sendRedirect("carrito.jsp");
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
}