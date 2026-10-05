package olle_christoffer.model;

import java.io.Serializable;

/** Information about the signed-in user that is safe to keep in the session. */
public class AuthenticatedUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private final long id;
    private final String username;
    private final String fullName;
    private final User.Role role;

    public AuthenticatedUser(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.fullName = user.getFullName();
        this.role = user.getRole();
    }

    public long getId() { return id; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public User.Role getRole() { return role; }
    public boolean isAdmin() { return role == User.Role.ADMIN; }
    public boolean isWarehouse() { return role == User.Role.WAREHOUSE; }
}
