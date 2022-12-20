package modules;

import java.util.List;

public class Place {
    private String title ;
    private List<String> images ;
    private List<Comment> comments ;
    private int views ;
    private int navigations;
    private String description ;
    private double lng ;
    private double lat;


    public Place(String title, List<String> images, String description, double lng, double lat) {
        this.title = title;
        this.images = images;
        this.description = description;
        this.lng = lng;
        this.lat = lat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
