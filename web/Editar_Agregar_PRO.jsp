<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="modelo.Producto, modelo.Categoria, modelo.Proveedor, modelo.Color, modelo.Talla"%>
<%
    Producto producto = (Producto) request.getAttribute("producto");
    boolean esEdicion = (producto != null);

    List<Categoria> listaCategorias = (List<Categoria>) request.getAttribute("categorias");
    List<Proveedor> proveedores = (List<Proveedor>) request.getAttribute("proveedores");
    List<Color> listaColores = (List<Color>) request.getAttribute("colores");
    List<Talla> listaTallas = (List<Talla>) request.getAttribute("tallas");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><%= esEdicion ? "Editar" : "Agregar"%> Producto</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background: #eee;
                padding: 20px;
            }
            h2 {
                text-align: center;
            }
            form {
                width: 450px;
                margin: 20px auto;
                background: #f9f9f9;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.2);
            }
            label {
                font-weight: bold;
                display: block;
                margin-top: 10px;
            }
            input[type="text"], input[type="number"], input[type="email"], textarea, select {
                width: 100%;
                padding: 8px;
                border: 1px solid #ccc;
                border-radius: 5px;
                margin-bottom: 10px;
            }
            input[type="file"] {
                margin-bottom: 10px;
            }
            input[type="submit"], button {
                padding: 10px 20px;
                background-color: #007BFF;
                border: none;
                color: white;
                border-radius: 5px;
                cursor: pointer;
                margin-top: 10px;
            }
            .hidden {
                display: none;
                margin-top: 10px;
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
                background: #f1f1f1;
            }
        </style>
    </head>
    <body>

        <h2><%= esEdicion ? "Editar Producto" : "Agregar Nuevo Producto"%></h2>

        <!-- Formulario principal de producto -->
        <form action="Controlador?accion=<%= esEdicion ? "actualizarProducto" : "agregarProducto"%>" method="post" enctype="multipart/form-data">
            <% if (esEdicion) { %>
                <input type="hidden" name="idProducto" value="<%= producto.getIdProducto()%>">
            <% } %>

            <label>Nombre:</label>
            <input type="text" name="nombre" value="<%= esEdicion ? producto.getNombre() : ""%>" required>

            <label>Precio:</label>
            <input type="number" name="precio" value="<%= esEdicion ? producto.getPrecio() : ""%>" required>

            <label>Género:</label>
            <input type="text" name="genero" value="<%= esEdicion ? producto.getGenero() : ""%>" required>

            <label>Descripción:</label>
            <textarea name="descripcion" rows="3"><%= esEdicion ? producto.getDescripcion() : ""%></textarea>

            <label>Foto:</label>
            <input type="file" name="foto" <%= esEdicion ? "" : "required"%>>
            <% if (esEdicion) { %>
                <p>Imagen actual:</p>
                <img src="ControladorImg?id=<%= producto.getIdProducto()%>" width="150" height="150">
            <% } %>

            <!-- Categoría -->
            <label>Categoría:</label>
            <select name="categoriaId" required>
                <option value="">-- Selecciona categoría --</option>
                <% for (Categoria cat : listaCategorias) { %>
                    <option value="<%= cat.getIdCategoria()%>" <%= esEdicion && producto.getCategoria()!=null && producto.getCategoria().getIdCategoria()==cat.getIdCategoria() ? "selected" : ""%>><%= cat.getNombre()%></option>
                <% } %>
            </select>

            <!-- Proveedor -->
            <label>Proveedor:</label>
            <select name="proveedorId" required>
                <option value="">-- Selecciona proveedor --</option>
                <% for (Proveedor pro : proveedores) { %>
                    <option value="<%= pro.getIdProveedor()%>" <%= esEdicion && producto.getProveedor()!=null && producto.getProveedor().getIdProveedor()==pro.getIdProveedor() ? "selected" : ""%>><%= pro.getNombre_proveedor()%></option>
                <% } %>
            </select>
            <% if (!esEdicion) { %>
                <button type="button" onclick="toggleFormulario('nuevoProveedor')">+ Nuevo proveedor</button>
                <div id="nuevoProveedor" class="hidden">
                    <label>Nombre proveedor:</label>
                    <input type="text" name="nombreProveedor">
                    <label>Correo proveedor:</label>
                    <input type="email" name="correoProveedor">
                    <button type="submit" name="accion" value="agregarProveedorDesdeProducto">Guardar proveedor</button>
                </div>
            <% } %>

            <!-- Color -->
            <label>Color:</label>
            <select name="colorId" required>
                <option value="">-- Selecciona color --</option>
                <% for (Color c : listaColores) { %>
                    <option value="<%= c.getIdColor()%>" <%= esEdicion && producto.getColor()!=null && producto.getColor().getIdColor()==c.getIdColor() ? "selected" : ""%>><%= c.getNombre()%></option>
                <% } %>
            </select>
            <% if (!esEdicion) { %>
                <button type="button" onclick="toggleFormulario('nuevoColor')">+ Nuevo color</button>
                <div id="nuevoColor" class="hidden">
                    <label>Nombre color:</label>
                    <input type="text" name="nombreColor">
                    <button type="submit" name="accion" value="agregarColorDesdeProducto">Guardar color</button>
                </div>
            <% } %>

            <!-- Talla -->
            <label>Talla:</label>
            <select name="tallaId" required>
                <option value="">-- Selecciona talla --</option>
                <% for (Talla t : listaTallas) { %>
                    <option value="<%= t.getIdTalla()%>" <%= esEdicion && producto.getTalla()!=null && producto.getTalla().getIdTalla()==t.getIdTalla() ? "selected" : ""%>><%= t.getNombre()%></option>
                <% } %>
            </select>
            <% if (!esEdicion) { %>
                <button type="button" onclick="toggleFormulario('nuevaTalla')">+ Nueva talla</button>
                <div id="nuevaTalla" class="hidden">
                    <label>Nombre talla:</label>
                    <input type="text" name="nombreTalla">
                    <button type="submit" name="accion" value="agregarTallaDesdeProducto">Guardar talla</button>
                </div>
            <% } %>

            <input type="submit" value="<%= esEdicion ? "Actualizar" : "Agregar"%> Producto">
        </form>

        <script>
            function toggleFormulario(id) {
                const form = document.getElementById(id);
                form.style.display = (form.style.display === "none" || form.style.display === "") ? "block" : "none";
            }
        </script>
    </body>
</html>