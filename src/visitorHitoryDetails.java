public class visitorHitoryDetails {
    private String name;
    private String date;
    private int rating;

    public visitorHitoryDetails(String name, String date, int rating) {
        this.name = name;
        this.date = date;
        this.rating = rating;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public int getRating() {
        return rating;
    }

    public String toString(){
        return name;
    }


}
