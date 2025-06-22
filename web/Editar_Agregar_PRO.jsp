<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="modelo.Producto"%>
<%@page import="modelo.Categoria, modelo.Proveedor, modelo.Color, modelo.Talla"%>
<%
    Producto producto = (Producto) request.getAttribute("producto");
    boolean esEdicion = (producto != null);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><%= esEdicion ? "Editar" : "Agregar"%> Producto</title>
        <style>
            form {
                width: 400px;
                margin: 30px auto;
                background: #f9f9f9;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.2);
            }

            input[type="text"], input[type="number"], textarea, select {
                width: 100%;
                padding: 8px;
                margin-bottom: 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }

            input[type="file"] {
                margin-bottom: 10px;
            }

            input[type="submit"] {
                padding: 10px 20px;
                background-color: #007BFF;
                border: none;
                color: white;
                border-radius: 5px;
                cursor: pointer;
            }

            h2 {
                text-align: center;
            }
        </style>
    </head>
    <body>

        <h2><%= esEdicion ? "Editar Producto" : "Agregar Nuevo Producto"%></h2>

       <form action="Controlador?accion=<%= esEdicion ? "actualizarProducto" : "agregarProducto" %>" method="post" enctype="multipart/form-data">

            <% if (esEdicion) {%>
            <input type="hidden" name="idProducto" value="<%= producto.getIdProducto()%>">
            <% }%>

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
            <% if (esEdicion) {%>
            <p>Imagen actual:</p>
            <img src="ControladorImg?id=<%= producto.getIdProducto()%>" width="150" height="150">
            <% }%>

            <label>Categoría ID:</label>
            <input type="number" name="categoriaId" value="<%= esEdicion && producto.getCategoria() != null ? producto.getCategoria().getIdCategoria() : "" %>" required>

            <label>Proveedor ID:</label>
            <input type="number" name="proveedorId" value="<%= (esEdicion && producto.getProveedor() != null) ? String.valueOf(producto.getProveedor().getIdProveedor()) : "0"%>" required>

            <label>Color ID:</label>
            <input type="number" name="colorId" value="<%= (esEdicion && producto.getColor() != null) ? String.valueOf(producto.getColor().getIdColor()) : "0"%>" required>

            <label>Talla ID:</label>
            <input type="number" name="tallaId" value="<%= (esEdicion && producto.getTalla() != null) ? String.valueOf(producto.getTalla().getIdTalla()) : "0"%>" required>
            <input type="submit" value="<%= esEdicion ? "Actualizar" : "Agregar"%> Producto">
        </form>

    </body>
</html>