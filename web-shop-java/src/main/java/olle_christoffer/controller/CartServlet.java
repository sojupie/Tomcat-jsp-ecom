package olle_christoffer.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import olle_christoffer.model.Cart;
import olle_christoffer.service.CartService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {
    private static final String CART_ATTRIBUTE = "cart";
    private static final String CART_NOTICE_ATTRIBUTE = "cartNotice";
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Cart cart = getCart(session);
        try {
            if (cartService.adjustToAvailableStock(cart)) {
                request.setAttribute(CART_NOTICE_ATTRIBUTE,
                        "Varukorgen har justerats efter aktuellt lagersaldo.");
            }
        } catch (SQLException exception) {
            getServletContext().log("Could not check cart stock", exception);
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            request.setAttribute("cartError", "Stock could not be checked. Please try again later.");
        }
        request.setAttribute(CART_ATTRIBUTE, cart);
        request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();
        Cart cart = getCart(session);

        try {
            String action = request.getParameter("action");
            long productId = Long.parseLong(request.getParameter("productId"));

            if ("add".equals(action)) {
                int quantity = Integer.parseInt(request.getParameter("quantity"));
                cartService.addProduct(cart, productId, quantity);
            } else if ("remove".equals(action)) {
                cartService.removeProduct(cart, productId);
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            response.sendRedirect(request.getContextPath() + "/cart");
        } catch (IllegalArgumentException exception) {
            request.setAttribute(CART_ATTRIBUTE, cart);
            request.setAttribute("cartError", "Check the product and quantity, then try again.");
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);
        } catch (SQLException exception) {
            getServletContext().log("Could not add product to cart", exception);
            request.setAttribute(CART_ATTRIBUTE, cart);
            request.setAttribute("cartError", "The cart could not be updated. Please try again later.");
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            request.getRequestDispatcher("/WEB-INF/views/cart.jsp").forward(request, response);
        }
    }

    private Cart getCart(HttpSession session) {
        Cart cart = (Cart) session.getAttribute(CART_ATTRIBUTE);
        if (cart == null) {
            cart = new Cart();
            session.setAttribute(CART_ATTRIBUTE, cart);
        }
        return cart;
    }
}
