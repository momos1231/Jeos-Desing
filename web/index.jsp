<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tienda - Principal</title>
    <link rel="stylesheet" href="css/Style_index.css">
   
</head>
<body>
    <div class="navbar">
        <h1>Tienda de Ropa</h1>
        <div class="dropdown">
            <button class="dropdown-button" onclick="toggleDropdown()">Mi Cuenta</button>
            <div class="dropdown-content" id="myDropdown">
                <button onclick="location.href='login.jsp'">Iniciar Sesión</button>
                </div>
        </div>
    </div>

    <div class="header">
        <h1>Productos Disponibles</h1>
    </div>

    <div class="categories">
        <form action="Controlador" method="get" style="display: inline;">
            <button type="submit" name="accion" value="listarProductos">Ver Todos</button>
        </form>
        <form action="Controlador" method="get" style="display: inline;">
            <button type="submit" name="accion" value="listarCamisas">Camisas</button>
        </form>
        <form action="Controlador" method="get" style="display: inline;">
            <button type="submit" name="accion" value="listarPantalones">Pantalones</button>
        </form>
        <form action="Controlador" method="get" style="display: inline;">
            <button type="submit" name="accion" value="listarChaquetas">Chaquetas</button>
        </form>
        </div>

    <hr/>

    <div class="product-display">
      
       
    </div>

    <script>
        /* Función para mostrar/ocultar el desplegable */
        function toggleDropdown() {
            document.getElementById("myDropdown").parentNode.classList.toggle("show");
        }

        // Cierra el desplegable si el usuario hace clic fuera de él
        window.onclick = function(event) {
            if (!event.target.matches('.dropdown-button')) {
                var dropdowns = document.getElementsByClassName("dropdown-content");
                for (var i = 0; i < dropdowns.length; i++) {
                    var openDropdown = dropdowns[i];
                    if (openDropdown.parentNode.classList.contains('show')) {
                        openDropdown.parentNode.classList.remove('show');
                    }
                }
            }
        }
    </script>
</body>
</html>