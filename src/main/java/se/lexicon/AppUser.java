package se.lexicon;

public class AppUser {

    public enum Role {
        ROLE_APP_USER,
        ROLE_APP_ADMIN
    }

    private String username;
    private String password;
    private final Role role;

    public AppUser(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "Användare: " + username + " (" + role + ")";
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
