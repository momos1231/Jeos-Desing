package controlador;

import config.Conexion;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;
import modelo.ProductoDAO;

@WebServlet("/ControladorImg")
public class ControladorImg extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        ProductoDAO dao = new ProductoDAO();
        byte[] imagen = dao.obtenerFotoPorId(id); // Debes crear este método

        if (imagen != null) {
            response.setContentType("image/jpeg");
            OutputStream out = response.getOutputStream();
            out.write(imagen);
            out.flush();
            out.close();
        }
    }

}