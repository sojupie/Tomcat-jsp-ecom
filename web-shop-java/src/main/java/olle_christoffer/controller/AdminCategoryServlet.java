package olle_christoffer.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import olle_christoffer.model.AuthenticatedUser;
import olle_christoffer.model.Category;
import olle_christoffer.service.CategoryAdminService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/categories")
public class AdminCategoryServlet extends HttpServlet {
    private final CategoryAdminService categoryService = new CategoryAdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthenticatedUser user = getUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            Category category = new Category();
            String editId = request.getParameter("edit");
            if (editId != null && !editId.isBlank()) {
                category = categoryService.findCategory(user.getId(), Long.parseLong(editId));
            }
            showPage(request, response, user.getId(), category, null, HttpServletResponse.SC_OK);
        } catch (SecurityException exception) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (IllegalArgumentException exception) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        AuthenticatedUser user = getUser(session);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Category category = new Category();
        try {
            category.setId(parseLong(request.getParameter("id"), 0));
            category.setName(request.getParameter("name"));
            category.setDescription(request.getParameter("description"));
            categoryService.saveCategory(user.getId(), category);
            session.setAttribute("adminNotice", "Kategorin sparades.");
            response.sendRedirect(request.getContextPath() + "/admin/categories");
        } catch (IllegalArgumentException exception) {
            showPage(request, response, user.getId(), category,
                    "Check the category fields and try again.", HttpServletResponse.SC_BAD_REQUEST);
        } catch (SecurityException exception) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (SQLException exception) {
            getServletContext().log("Could not save category", exception);
            showPage(request, response, user.getId(), category,
                    "The category could not be saved. Please try again later.",
                    HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    private void showPage(HttpServletRequest request, HttpServletResponse response, long userId,
                          Category category, String error, int status)
            throws ServletException, IOException {
        try {
            request.setAttribute("categories", categoryService.listCategories(userId));
            request.setAttribute("category", category);
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
            request.getRequestDispatcher("/WEB-INF/views/admin/categories.jsp").forward(request, response);
        } catch (SecurityException exception) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (SQLException exception) {
            getServletContext().log("Could not load category administration", exception);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    private AuthenticatedUser getUser(HttpSession session) {
        if (session == null) {
            return null;
        }
        Object user = session.getAttribute("authenticatedUser");
        return user instanceof AuthenticatedUser authenticatedUser ? authenticatedUser : null;
    }

    private long parseLong(String value, long defaultValue) {
        return value == null || value.isBlank() ? defaultValue : Long.parseLong(value);
    }
}
