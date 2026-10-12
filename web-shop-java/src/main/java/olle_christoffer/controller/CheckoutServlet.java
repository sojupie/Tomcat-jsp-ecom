package olle_christoffer.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import olle_christoffer.model.AuthenticatedUser;
import olle_christoffer.model.Cart;
import olle_christoffer.service.CartService;
import olle_christoffer.service.OrderService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/checkout")
public class CheckoutServlet extends HttpServlet {
    private static final String CART_ATTRIBUTE = "cart";
    private static final String USER_ATTRIBUTE = "authenticatedUser";
    private final OrderService orderService = new OrderService();
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Allow", "POST");
        response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !(session.getAttribute(USER_ATTRIBUTE) instanceof AuthenticatedUser user)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        Cart cart = (Cart) session.getAttribute(CART_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(CART_ATTRIBUTE, cart);
        }

        try {
            orderService.placeOrder(user.getId(), cart);
            cart.clear();
            session.setAttribute("orderNotice", "Beställningen har sparats.");
            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (IllegalArgumentException exception) {
            forwardCart(request, response, cart,
                    HttpServletResponse.SC_BAD_REQUEST, "The cart is empty or contains invalid items.");
        } catch (IllegalStateException exception) {
            forwardCart(request, response, cart,
                    HttpServletResponse.SC_CONFLICT, exception.getMessage());
        } catch (SQLException exception) {
            getServletContext().log("Could not place order", exception);
            forwardCart(request, response, cart, HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                    "The order could not be saved. Please try again later.");
        } catch (RuntimeException exception) {
            getServletContext().log("Unexpected error while placing order", exception);
            forwardCart(request, response, cart, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "The order could not be completed. Please try again later.");
        }
    }

    private void forwardCart(HttpServletRequest request, HttpServletResponse response, Cart cart,
                             int status, String error)
            throws ServletException, IOException {
        request.setAttribute(CART_ATTRIBUTE, cartService.toView(cart));
        request.setAttribute("cartError", error);
        response.setStatus(status);
        request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);
    }
}
