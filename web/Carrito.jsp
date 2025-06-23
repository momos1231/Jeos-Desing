<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, modelo.Producto" %>
<%
    List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
%>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mi Carrito de Compras</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f4f4f4;
        }
        h1 {
            color: #333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        th, td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .total {
            font-weight: bold;
            text-align: right;
            padding-top: 10px;
        }
        .acciones {
            margin-top: 20px;
            text-align: center;
        }
        .acciones button, .acciones a {
            padding: 10px 15px;
            margin: 5px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block; /* Para que los botones se vean bien en la misma línea */
            font-size: 16px;
        }
        .acciones button.comprar {
            background-color: #28a745;
            color: white;
        }
        .acciones button.vaciar {
            background-color: #dc3545;
            color: white;
        }
        .acciones a.volver {
            background-color: #007bff;
            color: white;
        }
        .producto-img {
            width: 80px; /* Tamaño fijo para la imagen del producto en el carrito */
            height: 80px;
            object-fit: cover;
            border-radius: 5px;
        }
    </style>
</head>
<body>
    <h1>Mi Carrito de Compras</h1>

    <% if (carrito == null || carrito.isEmpty()) { %>
        <p>Tu carrito está vacío.</p>
    <% } else { %>
        <table>
            <thead>
                <tr>
                    <th>Producto</th>
                    <th>Imagen</th>
                    <th>Precio Unitario</th>
                    <th>Cantidad</th>
                    <th>Subtotal</th>
                    <th>Acción</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    double total = 0.0; // <-- CAMBIO IMPORTANTE: Declarar total como double
                    for (Producto p : carrito) { 
                        // Aquí asumo que cada producto en el carrito representa una "unidad" o un "item"
                        // Si tuvieras una cantidad por producto en el carrito, deberías ajustarlo
                        // Por ahora, estoy sumando el precio de cada producto individualmente.
                        // Si necesitas manejar cantidades, el modelo de carrito debe ser más complejo (e.g., Map<Producto, Integer> o una clase CarritoItem)
                        double subtotalItem = p.getPrecio(); // p.getPrecio() devuelve int, pero se asigna a double sin problema
                        total += subtotalItem; // Suma a double
                %>
                <tr>
                    <td><%= p.getNombre()%></td>
                    <td><img src="ControladorImg?id=<%= p.getIdProducto()%>" alt="<%= p.getNombre()%>" class="producto-img"></td>
                    <td>$<%= String.format("%.2f", (double) p.getPrecio()) %></td> <%-- Castear a double para formateo --%>
                    <td>1</td> <%-- Asumiendo cantidad 1 por cada item en el carrito, ajusta si tu lógica es diferente --%>
                    <td>$<%= String.format("%.2f", subtotalItem) %></td>
                    <td>
                        <form action="Controlador" method="post" onsubmit="return confirm('¿Estás seguro de quitar este producto?');">
                            <input type="hidden" name="accion" value="quitarDelCarrito">
                            <input type="hidden" name="idProducto" value="<%= p.getIdProducto()%>">
                            <button type="submit" style="background-color: #f44336; color: white; padding: 5px 10px; border-radius: 3px; cursor: pointer;">Quitar</button>
                        </form>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
        <div class="total">
            Total a Pagar: $<%= String.format("%.2f", total) %>
        </div>
    <% } %>

    <div class="acciones">
        <% if (carrito != null && !carrito.isEmpty()) { %>
            <form action="Controlador" method="post" style="display:inline-block;">
                <input type="hidden" name="accion" value="procesarCompra">
                <button type="submit" class="comprar">Proceder al Pago</button>
            </form>
            <form action="Controlador" method="post" style="display:inline-block;" onsubmit="return confirm('¿Estás seguro de vaciar el carrito?');">
                <input type="hidden" name="accion" value="vaciarCarrito">
                <button type="submit" class="vaciar">Vaciar Carrito</button>
            </form>
        <% } %>
        <a href="Controlador?accion=listarProductos" class="volver">Seguir Comprando</a>
    </div>
</body>
</html>