package olle_christoffer.dto;


public final class UserDTO {
    private final long id;
    private final String username;
    private final String fullName;
    private final String email;
    private final String role;
    private final boolean active;

    public UserDTO(long id, String username, String fullName, String email, String role, boolean active) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.active = active;
    }

    public long getId() {return id;}
    public String getUsername() { return username; }
    public String getFullName() {return fullName;}
    public String getEmail() {return email; }
    public String getRole() {return role; }
    public boolean isActive() { return active;}

}
