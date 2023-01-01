package modules;

public class Review {
    private String ownerID ;
    private String body ;
    private String time ;
    private String placeKey ;
    private double rate ;

    public Review() {
    }

    public Review(String ownerID, String body, String time, String placeKey, double rate) {
        this.ownerID = ownerID;
        this.body = body;
        this.time = time;
        this.placeKey = placeKey;
        this.rate = rate;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlaceKey() {
        return placeKey;
    }

    public void setPlaceKey(String placeKey) {
        this.placeKey = placeKey;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
