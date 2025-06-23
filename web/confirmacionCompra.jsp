<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirmación de Compra</title>
        <style>
            body { font-family: Arial, sans-serif; text-align: center; padding: 50px; background-color: #e9ecef; }
            .container {
                background-color: #fff;
                padding: 40px;
                border-radius: 8px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.1);
                max-width: 600px;
                margin: 0 auto;
            }
            .success-message { color: #28a745; font-size: 1.8em; margin-bottom: 20px; font-weight: bold; }
            .error-message { color: #dc3545; font-size: 1.8em; margin-bottom: 20px; font-weight: bold; }
            p { font-size: 1.1em; color: #555; }
            a { text-decoration: none; padding: 12px 25px; background-color: #007bff; color: white; border-radius: 5px; transition: background-color 0.3s ease; }
            a:hover { background-color: #0056b3; }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>Estado de la Compra</h1>
            <% String mensajeExito = (String) request.getAttribute("mensajeExito"); %>
            <% String mensajeError = (String) request.getAttribute("mensajeError"); %>

            <% if (mensajeExito != null) { %>
                <div class="success-message"><%= mensajeExito %></div>
                <p>¡Gracias por su compra! Esperamos que disfrute sus productos.</p>
            <% } else if (mensajeError != null) { %>
                <div class="error-message"><%= mensajeError %></div>
                <p>Lo sentimos, hubo un problema al procesar su pedido. Por favor, inténtelo de nuevo.</p>
            <% } else { %>
                <p>No se pudo determinar el estado de la compra. Regrese e inténtelo de nuevo.</p>
            <% } %>
            <br>
            <a href="Controlador?accion=listarProductos">Volver a la tienda</a>
        </div>
    </body>
</html>