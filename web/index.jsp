<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, modelo.Producto" %>
<%
    if (request.getAttribute("productos") == null) {
        response.sendRedirect("Controlador?accion=listarProductos");
        return;
    }

    List<Producto> productos = (List<Producto>) request.getAttribute("productos");
%>
<%
    List<Producto> carrito = (List<Producto>) session.getAttribute("carrito");
    int cantidadCarrito = (carrito != null) ? carrito.size() : 0;
%>

<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Tienda - Principal</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f7f7f7;
                margin: 0;
                padding: 0;
            }

            .navbar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                background-color: #333;
                color: white;
                padding: 10px 20px;
            }

            .navbar h1 {
                margin: 0;
                font-size: 24px;
            }

            .dropdown {
                position: relative;
                display: inline-block;
            }

            .dropdown-button {
                background-color: #555;
                color: white;
                padding: 10px;
                border: none;
                cursor: pointer;
                border-radius: 5px;
            }

            .dropdown-content {
                display: none;
                position: absolute;
                right: 0;
                background-color: white;
                min-width: 160px;
                box-shadow: 0px 8px 16px rgba(0,0,0,0.2);
                z-index: 1;
                border-radius: 5px;
            }

            .dropdown-content button {
                color: black;
                padding: 10px 16px;
                text-decoration: none;
                display: block;
                width: 100%;
                border: none;
                background: none;
                text-align: left;
                cursor: pointer;
            }

            .dropdown-content button:hover {
                background-color: #ddd;
            }

            .show .dropdown-content {
                display: block;
            }

            .header {
                text-align: center;
                padding: 20px;
            }

            .categories {
                display: flex;
                align-items: center;
                padding: 0 20px;
                gap: 10px;
            }

            .categories form {
                display: inline-block;
                margin: 0 10px;
            }

            .categories button {
                padding: 10px 20px;
                border: none;
                background-color: #2196F3;
                color: white;
                border-radius: 5px;
                cursor: pointer;
            }

            .categories .botones {
                display: flex;
                gap: 10px;
                flex: 1;           /* Ocupa todo el espacio */
                justify-content: center;
            }
            .categories button:hover {
                background-color: #1976D2;

            }

            .contenedor-productos {
                display: flex;
                flex-wrap: wrap;
                gap: 20px;
                justify-content: center;
                padding: 20px;
            }

            .producto-card {
                background: #fff;
                border: 1px solid #ccc;
                border-radius: 10px;
                padding: 15px;
                width: 230px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                text-align: center;
            }

            .producto-card img {
                max-width: 100%;
                max-height: 180px;
                object-fit: cover;
                border-radius: 5px;
            }

            .producto-card h3 {
                margin: 10px 0 5px;
            }

            .producto-card p {
                margin: 4px 0;
                font-size: 14px;
            }
        </style>
    </head>
    <body>

        <div class="navbar">
            <h1>Tienda de Ropa</h1>
            <div class="dropdown">
                <button class="dropdown-button" onclick="toggleDropdown()">Mi Cuenta</button>
                <div class="dropdown-content" id="myDropdown">
                    <button onclick="location.href = 'login.jsp'">Iniciar Sesión</button>
                    </div>
            </div>
        </div>

        <div class="header">
            <h1>Productos Disponibles</h1>
        </div>

        <div class="categories">
            <div class="botones" style="flex:1; display:flex; gap:10px; justify-content:center;">
                <form action="Controlador" method="get">
                    <button type="submit" name="accion" value="listarProductos">Ver Todos</button>
                </form>
                <form action="Controlador" method="get">
                    <button type="submit" name="accion" value="listarCamisas">Camisas</button>
                </form>
                <form action="Controlador" method="get">
                    <button type="submit" name="accion" value="listarPantalones">Pantalones</button>
                </form>
                <form action="Controlador" method="get">
                    <button type="submit" name="accion" value="listarChaquetas">Chaquetas</button>
                </form>
            </div>

            <div style="margin-left:40px; position:relative; cursor:pointer; font-size:28px;" onclick="location.href = 'Controlador?accion=verCarrito'">
                🛒
                <% if (cantidadCarrito > 0) {%>
                <span style="position:absolute; top:-8px; right:-8px; background:red; color:white; border-radius:50%; padding:2px 7px; font-size:12px;">
                    <%= cantidadCarrito%>
                </span>
                <% } %>
            </div>
        </div>

        <hr/>

        <div class="product-display">
            <div class="contenedor-productos">
                <%
                    if (productos != null && !productos.isEmpty()) {
                        for (Producto p : productos) {
                %>
                <div class="producto-card">
                    <img src="ControladorImg?id=<%= p.getIdProducto()%>" alt="Imagen del producto">
                    <h3><%= p.getNombre()%></h3>
                    <%-- Línea modificada para manejar 'precio' como int y formatearlo a double --%>
                    <p><strong>$<%= String.format("%.2f", (double) p.getPrecio()) %></strong></p> 
                    <p><%= p.getGenero()%></p>
                    <p><%= p.getDescripcion()%></p>
                    <p class="color"><strong>Color:</strong> <%= (p.getColor() != null) ? p.getColor().getNombre() : "No definido"%></p>
                    <p class="categoria"><strong>Categoría:</strong> <%= (p.getCategoria() != null) ? p.getCategoria().getNombre() : "No definido"%></p>
                    <p class="talla"><strong>Talla:</strong> <%= (p.getTalla() != null) ? p.getTalla().getNombre() : "No definido"%></p>
                    <p class="proveedor"><strong>Proveedor:</strong> <%= (p.getProveedor() != null) ? p.getProveedor().getNombre_proveedor() : "No definido"%></p>

                    <form action="Controlador" method="get" style="display:inline-block;">
                        <input type="hidden" name="accion" value="editarProducto">
                        <input type="hidden" name="id" value="<%= p.getIdProducto()%>">
                        <button type="submit" style="margin-top: 10px; background-color: #FFA500; color: white; border: none; padding: 8px 12px; border-radius: 5px;">Editar</button>
                    </form>
                    <form action="Controlador" method="post" style="display:inline-block;" onsubmit="return confirm('¿Estás seguro de eliminar este producto?');">
                        <input type="hidden" name="accion" value="eliminarProducto">
                        <input type="hidden" name="id" value="<%= p.getIdProducto()%>">
                        <button type="submit" style="margin-top: 10px; background-color: #DC3545; color: white; border: none; padding: 8px 12px; border-radius: 5px;">Eliminar</button>
                    </form>
                    <form action="Controlador" method="get" style="display:inline-block;">
                        <input type="hidden" name="accion" value="agregarAlCarrito">
                        <input type="hidden" name="idProducto" value="<%= p.getIdProducto()%>">
                        <button type="submit" 
                                style="margin-top: 10px; background-color: #28A745; color: white; border: none; padding: 8px 12px; border-radius: 5px;">
                            Comprar
                        </button>
                    </form>   
                </div>

                <%
                        }
                    } else {
                %>
                <p>No hay productos disponibles.</p>
                <%
                    }
                %>
            </div>
        </div>
        <div style="text-align:center; padding: 30px;">
            <form action="Controlador" method="get">
                <input type="hidden" name="accion" value="nuevoProducto">
                <button type="submit" style="background-color: #28A745; color: white; padding: 12px 20px; font-size: 16px; border: none; border-radius: 8px; cursor: pointer;">
                    Crear Nuevo Producto
                </button>
            </form>
        </div>

        <script>
            /* Función para mostrar/ocultar el desplegable */
            function toggleDropdown() {
                document.getElementById("myDropdown").parentNode.classList.toggle("show");
            }

            window.onclick = function (event) {
                if (!event.target.matches('.dropdown-button')) {
                    var dropdowns = document.getElementsByClassName("dropdown-content");
                    for (var i = 0; i < dropdowns.length; i++) {
                        var openDropdown = dropdowns[i];
                        if (openDropdown.parentNode.classList.contains('show')) {
                            openDropdown.parentNode.classList.remove('show');
                        }
                    }
                }
            };
        </script>
    </body>
</html>