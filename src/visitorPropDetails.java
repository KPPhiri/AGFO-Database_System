public class visitorPropDetails {

    private String username1;
    private String email1;
    private int loggedvisit;

    public String getUsername1() {
        return username1;
    }

    public void setUsername1(String username1) {
        this.username1 = username1;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public int getLoggedvisit() {
        return loggedvisit;
    }

    public void setLoggedvisit(int loggedvisit) {
        this.loggedvisit = loggedvisit;
    }

    public visitorPropDetails(String username1, String email1, int loggedvisit) {
        this.username1 = username1;
        this.email1 = email1;
        this.loggedvisit = loggedvisit;
    }



}