public class unconfirmedPropDetails {
    String name;
    String address;
    String city;
    String zip;
    String size;
    String type;
    Boolean ispublic;
    Boolean iscommercial;
    String id;
    String owner;

    public unconfirmedPropDetails(String name, String address, String city, String zip, String size, String type, Boolean ispublic, Boolean iscommercial, String id, String owner) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.zip = zip;
        this.size = size;
        this.type = type;
        this.ispublic = ispublic;
        this.iscommercial = iscommercial;
        this.id = id;
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIspublic() {
        return ispublic;
    }

    public void setIspublic(Boolean ispublic) {
        this.ispublic = ispublic;
    }

    public Boolean getIscommercial() {
        return iscommercial;
    }

    public void setIscommercial(Boolean iscommercial) {
        this.iscommercial = iscommercial;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}

