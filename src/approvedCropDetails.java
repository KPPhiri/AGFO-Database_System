public class approvedCropDetails {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public approvedCropDetails(String name, String type) {

        this.name = name;
        this.type = type;
    }

    private String type;

    public String toString() {
        return name;
    }



}
