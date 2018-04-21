public class User {
    private String username;
    String type;
    public User(String username, String type) {
        this.username = username;
        this.type = type;

    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }
}
