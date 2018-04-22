public class ownerPropDetails {

    private String userName;
    private String Email;
    private int numProp;

    public ownerPropDetails(String userName, String email, int numProp) {
        this.userName = userName;
        Email = email;
        this.numProp = numProp;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return Email;
    }

    public int getNumProp() {
        return numProp;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setNumProp(int numProp) {
        this.numProp = numProp;
    }
}
