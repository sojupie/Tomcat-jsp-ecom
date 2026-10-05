package olle_christoffer.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import olle_christoffer.model.AuthenticatedUser;
import olle_christoffer.service.AccountService;

import java.io.IOException;
import java.sql.SQLException;

@WebServlet(urlPatterns = {"/login", "/register", "/logout"})
public class AccountServlet extends HttpServlet {
    public static final String USER_ATTRIBUTE = "authenticatedUser";
    private final AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getServletPath();
        if ("/logout".equals(path)) {
            response.setHeader("Allow", "POST");
            response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute(USER_ATTRIBUTE) != null) {
            response.sendRedirect(request.getContextPath() + "/products");
            return;
        }

        String view = "/WEB-INF/views" + ("/register".equals(path) ? "/register.jsp" : "/login.jsp");
        request.getRequestDispatcher(view).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getServletPath();
        if ("/logout".equals(path)) {
            logout(request, response);
            return;
        }

        boolean registering = "/register".equals(path);
        String username = request.getParameter("username");
        char[] password = toChars(request.getParameter("password"));
        try {
            AuthenticatedUser user;
            if (registering) {
                user = accountService.register(username, request.getParameter("fullName"),
                        request.getParameter("email"), password);
            } else {
                user = accountService.login(username, password);
            }
            HttpSession session = request.getSession();
            request.changeSessionId();
            session.setAttribute(USER_ATTRIBUTE, user);
            response.sendRedirect(request.getContextPath() + "/products");
        } catch (IllegalArgumentException exception) {
            getServletContext().log("Account request rejected: " + exception.getMessage());
            if (registering) {
                request.setAttribute("fullName", request.getParameter("fullName"));
                request.setAttribute("email", request.getParameter("email"));
            }
            request.setAttribute("username", username);
            request.setAttribute("accountError", exception.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            forwardForm(request, response, registering);
        } catch (SQLException exception) {
            getServletContext().log("Could not process account request", exception);
            request.setAttribute("accountError", "The account request could not be completed. Please try again later.");
            response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
            forwardForm(request, response, registering);
        } catch (RuntimeException exception) {
            getServletContext().log("Unexpected error while processing account request", exception);
            request.setAttribute("accountError", "The account request failed. Please try again later.");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            forwardForm(request, response, registering);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(USER_ATTRIBUTE);
            request.changeSessionId();
        }
        response.sendRedirect(request.getContextPath() + "/products");
    }

    private void forwardForm(HttpServletRequest request, HttpServletResponse response, boolean registering)
            throws ServletException, IOException {
        String view = registering ? "/WEB-INF/views/register.jsp" : "/WEB-INF/views/login.jsp";
        request.getRequestDispatcher(view).forward(request, response);
    }

    private char[] toChars(String value) {
        return value == null ? new char[0] : value.toCharArray();
    }
}
