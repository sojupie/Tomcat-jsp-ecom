package olle_christoffer.model;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * endast get/set-metoder,
 * samt par metoder för att se roll
 */

public class User implements Serializable {

    public enum Role {CUSTOMER, ADMIN, WAREHOUSE}
    private long id;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private Role role = Role.CUSTOMER; // ny användare blir som standard kund
    private boolean active = true;
    private OffsetDateTime createdAt;


    public User() {}

    // get/set metoderna
    public long getId() {return id;}

    public void setId(long id) {this.id = id;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getPasswordHash() {return passwordHash;}

    public void setPasswordHash(String passwordHash) {this.passwordHash = passwordHash;}

    public String getFullName() {return fullName;}

    public void setFullName(String fullName) {this.fullName = fullName;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public Role getRole() {return role;}

    public void setRole(Role role) {this.role = role;}

    public boolean isActive() {return active;}

    public void setActive(boolean active) {this.active = active;}

    public OffsetDateTime getCreatedAt() {return createdAt;}

    public void setCreatedAt(OffsetDateTime createdAt) {this.createdAt = createdAt;}

    public boolean isAdmin() {return role == Role.ADMIN;}

    public boolean isWarehouse() {return role == Role.WAREHOUSE;}
}

