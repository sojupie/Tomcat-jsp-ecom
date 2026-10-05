package olle_christoffer.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import olle_christoffer.model.AuthenticatedUser;
import olle_christoffer.model.Product;
import olle_christoffer.service.CategoryAdminService;
import olle_christoffer.service.ProductAdminService;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import static olle_christoffer.controller.SessionUtilities.getAuthenticatedUser;

@WebServlet("/admin/products")
public class AdminProductServlet extends HttpServlet {
    private final ProductAdminService productService = new ProductAdminService();
    private final CategoryAdminService categoryService = new CategoryAdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthenticatedUser user = getAuthenticatedUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Product product = new Product();
            product.setActive(true);
            String editId = request.getParameter("edit");
            if (editId != null && !editId.isBlank()) {
                product = productService.findProduct(user.getId(), Long.parseLong(editId));
            }
            showPage(request, response, user.getId(), product, null, HttpServletResponse.SC_OK);
        } catch (SecurityException exception) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (IllegalArgumentException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException exception) {
            getServletContext().log("Could not load product administration", exception);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        AuthenticatedUser user = getAuthenticatedUser(session);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Product product = new Product();
        try {
            product.setId(parseLong(request.getParameter("id"), 0));
            product.setSku(request.getParameter("sku"));
            product.setName(request.getParameter("name"));
            product.setDescription(request.getParameter("description"));
            product.setPrice(new BigDecimal(request.getParameter("price")));
            product.setCategoryId(parseLong(request.getParameter("categoryId"), 0));
            product.setStock(Integer.parseInt(request.getParameter("stock")));
            product.setActive(request.getParameter("active") != null);

            productService.saveProduct(user.getId(), product);
            session.setAttribute("adminNotice", "Produkten sparades.");
            response.sendRedirect(request.getContextPath() + "/admin/products");
        } catch (IllegalArgumentException exception) {
            showAfterPost(request, response, user.getId(), product,
                    "Check the product fields and try again.", HttpServletResponse.SC_BAD_REQUEST);
        } catch (SecurityException exception) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (SQLException exception) {
            getServletContext().log("Could not save product", exception);
            showAfterPost(request, response, user.getId(), product,
                    "The product could not be saved. Please try again later.",
                    HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    private void showAfterPost(HttpServletRequest request, HttpServletResponse response, long userId,
                               Product product, String error, int status)
            throws ServletException, IOException {
        request.setAttribute("adminError", error);
        showPage(request, response, userId, product, null, status);
    }

    private void showPage(HttpServletRequest request, HttpServletResponse response, long userId,
                          Product product, String error, int status)
            throws ServletException, IOException {
        try {
            request.setAttribute("products", productService.listProducts(userId));
            request.setAttribute("categories", categoryService.listCategories(userId));
            request.setAttribute("product", product);
            if (error != null) {
                request.setAttribute("adminError", error);
            }
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object notice = session.getAttribute("adminNotice");
                if (notice != null) {
                    request.setAttribute("adminNotice", notice);
                    session.removeAttribute("adminNotice");
                }
            }
            response.setStatus(status);
            request.getRequestDispatcher("/WEB-INF/views/admin/products.jsp").forward(request, response);
        } catch (SecurityException exception) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (SQLException exception) {
            getServletContext().log("Could not load product administration", exception);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    private long parseLong(String value, long defaultValue) {
        return value == null || value.isBlank() ? defaultValue : Long.parseLong(value);
    }
}
