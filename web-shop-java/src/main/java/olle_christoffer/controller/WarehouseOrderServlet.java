package olle_christoffer.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import olle_christoffer.model.AuthenticatedUser;
import olle_christoffer.service.WarehouseOrderService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/warehouse/orders")
public class WarehouseOrderServlet extends HttpServlet {
    private final WarehouseOrderService orderService = new WarehouseOrderService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthenticatedUser user = getUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        showPage(request, response, user.getId(), null, HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        AuthenticatedUser user = getUser(session);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try {
            if (!"pack".equals(request.getParameter("action"))) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            long orderId = Long.parseLong(request.getParameter("orderId"));
            orderService.markPacked(user.getId(), orderId);
            session.setAttribute("warehouseNotice", "Beställningen markerades som packad.");
            response.sendRedirect(request.getContextPath() + "/warehouse/orders");
        } catch (IllegalArgumentException exception) {
            showPage(request, response, user.getId(), "Check the order and try again.",
                    HttpServletResponse.SC_BAD_REQUEST);
        } catch (IllegalStateException exception) {
            showPage(request, response, user.getId(), exception.getMessage(),
                    HttpServletResponse.SC_CONFLICT);
        } catch (SecurityException exception) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (SQLException exception) {
            getServletContext().log("Could not update order packing status", exception);
            showPage(request, response, user.getId(),
                    "The order could not be updated. Please try again later.",
                    HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    private void showPage(HttpServletRequest request, HttpServletResponse response, long userId,
                          String error, int status)
            throws ServletException, IOException {
        try {
            request.setAttribute("orders", orderService.listOrders(userId));
            if (error != null) {
                request.setAttribute("warehouseError", error);
            }
            HttpSession session = request.getSession(false);
            if (session != null) {
                Object notice = session.getAttribute("warehouseNotice");
                if (notice != null) {
                    request.setAttribute("warehouseNotice", notice);
                    session.removeAttribute("warehouseNotice");
                }
            }
            response.setStatus(status);
            request.getRequestDispatcher("/WEB-INF/views/warehouse/orders.jsp").forward(request, response);
        } catch (SecurityException exception) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (SQLException exception) {
            getServletContext().log("Could not load warehouse orders", exception);
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
}
