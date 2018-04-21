public class userPropDetails {

    private String propName;
    private String address;
    private String city;
    private String zip;
    private String size;
    private String type;
    private boolean ipublic;
    private boolean commercial;
    private int id;
    private int visits;
    private String valid;
    private double rating;

    //Default constructor
    public userPropDetails(String propName, String address, String city
            , String zip, String size, String type, boolean ipublic
            , boolean commercial, int id, String valid, int visits, double rating) {

        this.propName = propName;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.size = size;
        this.type = type;
        this.ipublic = ipublic;
        this.commercial = commercial;
        this.id = id;
        this.visits = visits;
        this.valid = valid;
        this.rating = rating;
    }

    public String getPropName() {
        return propName;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getZip() {
        return zip;
    }

    public String getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public boolean getIpublic() {
        return ipublic;
    }

    public boolean getCommercial() {
        return commercial;
    }

    public int getId() {
        return id;
    }

    public int getVisits() {
        return visits;
    }
    public String getValid() {
        return valid;
    }

    public double getRating() {
        return rating;
    }
}

