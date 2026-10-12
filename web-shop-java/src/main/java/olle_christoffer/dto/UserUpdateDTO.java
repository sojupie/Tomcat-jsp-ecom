package olle_christoffer.dto;

/** Submitted changes to a user's role and account status. */
public final class UserUpdateDTO {
    private final long userId;
    private final String role;
    private final boolean active;

    public UserUpdateDTO(long userId, String role, boolean active) {
        this.userId = userId;
        this.role = role;
        this.active = active;
    }

    public long getUserId() { return userId; }
    public String getRole() { return role; }
    public boolean isActive() { return active; }
}
