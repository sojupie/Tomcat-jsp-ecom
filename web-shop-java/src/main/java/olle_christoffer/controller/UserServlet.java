package olle_christoffer.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import olle_christoffer.model.AuthenticatedUser;
import olle_christoffer.model.User;
import olle_christoffer.service.UserAdminService;

import java.io.IOException;
import java.sql.SQLException;

import static olle_christoffer.controller.SessionUtilities.getAuthenticatedUser;

@WebServlet("/admin/users")
public class UserServlet extends HttpServlet {
    private final UserAdminService userAdminService = new UserAdminService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AuthenticatedUser user = getAuthenticatedUser(request.getSession(false));
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        showPage(request, response, user, null, HttpServletResponse.SC_OK);
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

        try {
            long targetUserId = Long.parseLong(request.getParameter("userId"));
            User.Role role = parseRole(request.getParameter("role"));
            boolean active = "true".equals(request.getParameter("active"));
            userAdminService.changeRoleAndActive(user.getId(), targetUserId, role, active);
            session.setAttribute("userAdminNotice", "Användaren uppdaterades.");
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } catch (IllegalArgumentException exception) {
            showPage(request, response, user,
                    "Check the user ID and role, then try again.", HttpServletResponse.SC_BAD_REQUEST);
        } catch (SecurityException exception) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (SQLException exception) {
            getServletContext().log("Could not update user", exception);
            showPage(request, response, user,
                    "The user could not be updated. Please try again later.",
                    HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    private void showPage(HttpServletRequest request, HttpServletResponse response, AuthenticatedUser user,
                          String error, int status)
            throws ServletException, IOException {
        try {
            request.setAttribute("users", userAdminService.listUsers(user.getId()));
            request.setAttribute("currentUserId", user.getId());
            if (error != null) {
                request.setAttribute("userAdminError", error);
            }

            HttpSession session = request.getSession(false);
            if (session != null) {
                Object notice = session.getAttribute("userAdminNotice");
                if (notice != null) {
                    request.setAttribute("userAdminNotice", notice);
                    session.removeAttribute("userAdminNotice");
                }
            }
            response.setStatus(status);
            request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
        } catch (SecurityException exception) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (SQLException exception) {
            getServletContext().log("Could not load user administration", exception);
            response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        }
    }

    private User.Role parseRole(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("A role is required.");
        }
        return User.Role.valueOf(value);
    }
}
