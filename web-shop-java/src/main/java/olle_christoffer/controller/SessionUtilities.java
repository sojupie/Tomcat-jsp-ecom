package olle_christoffer.controller;

import jakarta.servlet.http.HttpSession;
import olle_christoffer.model.AuthenticatedUser;

public class SessionUtilities {
    public static final String AUTHENTICATED_USER_ATTRIBUTE = "authenticatedUser";

    private SessionUtilities() {}

    public static AuthenticatedUser getAuthenticatedUser(HttpSession session) {
        if (session == null) {
            return null;
        }

        Object user = session.getAttribute(AUTHENTICATED_USER_ATTRIBUTE);
        return user instanceof AuthenticatedUser authenticatedUser
                ? authenticatedUser
                : null;
    }
}
