public class User {
    private String username;
    private String type;
    private String email;

    private static User user = new User();
    private User() {
        this.username = username;
        this.type = type;

    }
    public static User getInstance( ) {
        return user;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
