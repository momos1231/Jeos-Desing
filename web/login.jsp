<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/Style_login.css">
    <title>Iniciar Sesión</title>
</head>
<body>
    <div class="login-container">
        <h2>Iniciar Sesión</h2>

        <%-- Mostrar mensaje de error si existe --%>
        <% String mensajeError = (String) request.getAttribute("mensajeError");
           if (mensajeError != null) { %>
            <p class="error-message"><%= mensajeError %></p>
        <% } %>

        <form action="LoginServlet" method="post">
            <div class="form-group">
                <label for="user">Usuario:</label>
                <input type="text" id="user" name="user" required>
            </div>
            <div class="form-group">
                <label for="password">Contraseña:</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn-login">Ingresar</button>
        </form>
       
    </div>
</body>
</html>