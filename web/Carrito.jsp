<%-- 
    Document   : carrito.jsp
    Created on : 22/06/2025, 7:00:56 p. m.
    Author     : FAMILIA RUSSI
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, modelo.Producto, modelo.Talla, modelo.Color"%>
<%
    List<Producto> carrito = (List<Producto>) request.getAttribute("carrito");
    List<Talla> tallas = (List<Talla>) request.getAttribute("tallas");
    List<Color> colores = (List<Color>) request.getAttribute("colores");

    int total = 0;
    if (carrito != null) {
        for (Producto p : carrito) {
            total += p.getPrecio();
        }
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mi Carrito</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f7f7f7;
                padding: 20px;
            }
            h1 {
                text-align: center;
                color: #333;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                background: #fff;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                border-radius: 8px;
                overflow: hidden;
            }
            table th, table td {
                padding: 12px;
                border-bottom: 1px solid #ddd;
                text-align: left;
            }
            table th {
                background-color: #2196F3;
                color: white;
            }
            .total-row td {
                font-weight: bold;
                background: #eee;
            }
            .actions {
                text-align: center;
                padding-top: 20px;
            }
            a, button {
                text-decoration: none;
                padding: 10px 20px;
                background-color: #28A745;
                color: white;
                border-radius: 5px;
                border: none;
                cursor: pointer;
            }
            a:hover, button:hover {
                background-color: #218838;
            }
        </style>
    </head>
    <body>
        <h1>Mi Carrito</h1>

        <%
            if (carrito == null || carrito.isEmpty()) {
        %>
            <p style="text-align:center;">Tu carrito está vacío.</p>
            <div class="actions">
                <a href="Controlador?accion=listarProductos">Volver a la tienda</a>
            </div>
        <%
            } else {
        %>

        <!-- Formulario que envía la elección de tallas y colores -->
        <form action="Controlador" method="post">
            <input type="hidden" name="accion" value="procesarCarrito">
            <table>
                <thead>
                    <tr>
                        <th>Nombre</th>
                        <th>Precio ($)</th>
                        <th>Talla</th>
                        <th>Color</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Producto p : carrito) { %>
                        <tr>
                            <td><%= p.getNombre() %></td>
                            <td><%= p.getPrecio() %></td>
                            <td>
                                <select name="tallaSeleccionada_<%= p.getIdProducto() %>">
                                    <% for (Talla t : tallas) { %>
                                        <option value="<%= t.getIdTalla() %>"><%= t.getNombre() %></option>
                                    <% } %>
                                </select>
                            </td>
                            <td>
                                <select name="colorSeleccionado_<%= p.getIdProducto() %>">
                                    <% for (Color c : colores) { %>
                                        <option value="<%= c.getIdColor() %>"><%= c.getNombre() %></option>
                                    <% } %>
                                </select>
                            </td>
                        </tr>
                    <% } %>
                    <tr class="total-row">
                        <td>Total</td>
                        <td colspan="3"><%= total %> $</td>
                    </tr>
                </tbody>
            </table>
            <div class="actions">
                <button type="submit">Confirmar Compra</button>
                <a href="Controlador?accion=listarProductos">Seguir comprando</a>
            </div>
        </form>

        <%
            }
        %>
    </body>
</html>