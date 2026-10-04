package olle_christoffer.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import olle_christoffer.service.ProductService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"", "/products"})
public class ProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.setAttribute("catalogError", false);

        try {
            request.setAttribute("products", productService.findActiveProducts());
        } catch (SQLException exception) {
            getServletContext().log("Could not load the product catalogue", exception);
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            request.setAttribute("catalogError", true);
        }

        request.getRequestDispatcher("/WEB-INF/views/products.jsp").forward(request, response);
    }

}
