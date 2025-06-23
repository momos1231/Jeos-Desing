<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="modelo.ItemCarrito" %>
<%@ page import="modelo.Producto" %>

<%
    List<ItemCarrito> carritoItems = (List<ItemCarrito>) session.getAttribute("carrito");
    if (carritoItems == null) {
        carritoItems = new ArrayList<>();
    }

    double totalGeneral = 0.0;
    for (ItemCarrito item : carritoItems) {
        totalGeneral += item.getSubtotal();
    }

    String[] tallasDisponibles = {"XS", "S", "M", "L", "XL", "Única"};
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
            color: #333;
        }
        h1, h3 {
            color: #333;
            border-bottom: 2px solid #ccc;
            padding-bottom: 10px;
            margin-bottom: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
            background-color: #fff;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        th, td {
            padding: 12px;
            border: 1px solid #ddd;
            text-align: left;
            vertical-align: middle;
        }
        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        .total-row {
            font-weight: bold;
            text-align: right;
            padding: 15px 0;
            font-size: 1.3em;
            color: #0056b3;
        }
        .acciones {
            margin-top: 30px;
            text-align: center;
        }
        .acciones button, .acciones a {
            padding: 12px 20px;
            margin: 8px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
            font-size: 16px;
            transition: background-color 0.3s ease;
        }
        .acciones button.comprar {
            background-color: #28a745;
            color: white;
        }
        .acciones button.comprar:hover {
            background-color: #218838;
        }
        .acciones a.vaciar {
            background-color: #dc3545;
            color: white;
        }
        .acciones a.vaciar:hover {
            background-color: #c82333;
        }
        .acciones a.volver {
            background-color: #007bff;
            color: white;
        }
        .acciones a.volver:hover {
            background-color: #0056b3;
        }
        .producto-img {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 5px;
            border: 1px solid #eee;
        }
        .cantidad-input {
            width: 70px;
            text-align: center;
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 1em;
        }
        .talla-select {
            padding: 8px;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 1em;
            min-width: 80px;
        }
        /* Estilos del formulario de cliente */
        .customer-form-section {
            background-color: #fff;
            padding: 25px;
            margin-top: 30px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.08);
        }
        .customer-form-section label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
            color: #555;
        }
        .customer-form-section input[type="text"],
        .customer-form-section input[type="email"],
        .customer-form-section input[type="number"] { /* Añadido para input type=number */
            width: calc(100% - 20px); /* Ajusta para padding */
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box; /* Para que padding no aumente el ancho total */
            font-size: 1em;
        }
        .customer-form-section .form-group {
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
    <h1>Mi Carrito de Compras</h1>

    <% if (carritoItems == null || carritoItems.isEmpty()) { %>
        <p style="text-align: center; font-size: 1.1em; color: #666;">Tu carrito está vacío.</p>
        <div class="acciones">
            <a href="Controlador?accion=listarProductos" class="volver">Volver a la tienda</a>
        </div>
    <% } else { %>
        <form id="checkoutForm" action="Controlador" method="POST">
            <input type="hidden" name="accion" value="procesarCompra">

            <table>
                <thead>
                    <tr>
                        <th>Producto</th>
                        <th>Imagen</th>
                        <th>Precio Unitario</th>
                        <th>Talla</th>
                        <th>Cantidad</th>
                        <th>Subtotal</th>
                        <th>Acción</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        int itemIndex = 0; // Para identificar cada fila y sus campos en el formulario
                        for (ItemCarrito item : carritoItems) {
                    %>
                    <tr>
                        <td><%= item.getProducto().getNombre()%></td>
                        <td><img src="ControladorImg?id=<%= item.getProducto().getIdProducto()%>" alt="<%= item.getProducto().getNombre()%>" class="producto-img"></td>
                        <td>$<%= String.format("%.2f", (double) item.getProducto().getPrecio()) %></td>
                        <td>
                            <select name="talla_<%= itemIndex %>" class="talla-select" onchange="updateSubtotal(this)">
                                <% for (String talla : tallasDisponibles) { %>
                                    <option value="<%= talla %>" <%= talla.equals(item.getTalla()) ? "selected" : "" %>>
                                        <%= talla %>
                                    </option>
                                <% } %>
                            </select>
                        </td>
                        <td>
                            <input type="number" name="cantidad_<%= itemIndex %>"
                                   value="<%= item.getCantidad() %>" min="1" class="cantidad-input"
                                   onchange="updateSubtotal(this)">
                            <!-- Campos ocultos necesarios para el Controlador para identificar el item -->
                            <input type="hidden" name="idProducto_<%= itemIndex %>" value="<%= item.getProducto().getIdProducto() %>">
                            <input type="hidden" name="tallaOriginal_<%= itemIndex %>" value="<%= item.getTalla() %>">
                            <input type="hidden" name="precioProducto_<%= itemIndex %>" value="<%= (double) item.getProducto().getPrecio() %>">
                        </td>
                        <td id="subtotal_<%= itemIndex %>">$<%= String.format("%.2f", item.getSubtotal()) %></td>
                        <td>
                            <a href="Controlador?accion=quitar&idProducto=<%= item.getProducto().getIdProducto()%>&talla=<%= item.getTalla()%>"
                               class="btn btn-danger btn-sm"
                               onclick="return confirm('¿Estás seguro de quitar este producto?');"
                               style="background-color: #f44336; color: white; padding: 5px 10px; border-radius: 3px; cursor: pointer; text-decoration: none;">Quitar</a>
                        </td>
                    </tr>
                    <%
                                itemIndex++;
                        }
                    %>
                </tbody>
            </table>

            <div class="total-row">
                Total a Pagar: <span id="totalGeneral">$<%= String.format("%.2f", totalGeneral) %></span>
            </div>

            <!-- Sección del Formulario de Datos del Cliente -->
            <div class="customer-form-section">
                <h3>Datos del Cliente</h3>
                <div class="form-group">
                    <label for="nombre">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" required>
                </div>
                <!-- REMOVIDO: Apellido, ya que no está en tu tabla cliente -->
                <div class="form-group">
                    <label for="email">Email:</label> <!-- CAMBIADO de 'correo' a 'email' -->
                    <input type="email" id="email" name="email" required>
                </div>
                <!-- REMOVIDO: Teléfono, ya que no está en tu tabla cliente -->
                <div class="form-group">
                    <label for="direccion">Dirección:</label>
                    <input type="text" id="direccion" name="direccion" required>
                </div>
                <div class="form-group">
                    <label for="edad">Edad:</label> <!-- AÑADIDO: Campo de edad -->
                    <input type="number" id="edad" name="edad" required min="1" max="120">
                </div>
            </div>

            <div class="acciones">
                <button type="submit" class="comprar">Proceder al Pago</button>
                <a href="Controlador?accion=vaciar" class="vaciar" onclick="return confirm('¿Estás seguro de vaciar el carrito?');">Vaciar Carrito</a>
                <a href="Controlador?accion=listarProductos" class="volver">Seguir Comprando</a>
            </div>
        </form>
    <% } %>

    <script>
        // Función para actualizar el subtotal de una fila y el total general del carrito
        function updateSubtotal(element) {
            const row = element.closest('tr');
            const index = element.name.split('_')[1];

            const cantidadInput = row.querySelector(`input[name="cantidad_${index}"]`);
            const precioProductoInput = row.querySelector(`input[name="precioProducto_${index}"]`);
            const subtotalCell = row.querySelector(`#subtotal_${index}`);

            let cantidad = parseInt(cantidadInput.value);
            const precioUnitario = parseFloat(precioProductoInput.value);

            if (isNaN(cantidad) || cantidad < 1) {
                cantidad = 1;
                cantidadInput.value = 1;
            }

            const nuevoSubtotal = cantidad * precioUnitario;
            subtotalCell.textContent = '$' + nuevoSubtotal.toFixed(2);

            updateTotalGeneral();
        }

        // Función para recalcular y actualizar el total general de todo el carrito
        function updateTotalGeneral() {
            let totalGeneral = 0.0;
            document.querySelectorAll('[id^="subtotal_"]').forEach(subtotalCell => {
                const subtotalValue = parseFloat(subtotalCell.textContent.replace('$', ''));
                if (!isNaN(subtotalValue)) {
                    totalGeneral += subtotalValue;
                }
            });
            document.getElementById('totalGeneral').textContent = '$' + totalGeneral.toFixed(2);
        }

        document.addEventListener('DOMContentLoaded', updateTotalGeneral);
    </script>
</body>
</html>