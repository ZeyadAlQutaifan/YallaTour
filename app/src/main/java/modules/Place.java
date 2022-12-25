package modules;

import java.io.Serializable;
import java.util.List;

public class Place implements Serializable {
    private String title ;
    private List<String> images ;

    private int views ;
    private int navigations;
    private int commentsCount ;
    private String description ;
    private double lng ;
    private double lat;
    private double rate ;
    private int rates ;
    private String city ;

    public Place(String title, List<String> images, int views, int navigations, int commentsCount, String description, double lng, double lat, double rate, int rates, String city) {
        this.title = title;
        this.images = images;
        this.views = views;
        this.navigations = navigations;
        this.commentsCount = commentsCount;
        this.description = description;
        this.lng = lng;
        this.lat = lat;
        this.rate = rate;
        this.rates = rates;
        this.city = city;
    }


    public Place(String title, List<String> images, int views, int navigations, int commentsCount, String description, double lng, double lat) {
        this.title = title;
        this.images = images;
        this.views = views;
        this.navigations = navigations;
        this.commentsCount = commentsCount;
        this.description = description;
        this.lng = lng;
        this.lat = lat;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public Place(String title, List<String> images, int views, int navigations, String description, double lng, double lat) {
        this.title = title;
        this.images = images;
        this.views = views;
        this.navigations = navigations;
        this.description = description;
        this.lng = lng;
        this.lat = lat;
    }

    public Place() {
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

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getNavigations() {
        return navigations;
    }

    public void setNavigations(int navigations) {
        this.navigations = navigations;
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

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getRates() {
        return rates;
    }

    public void setRates(int rates) {
        this.rates = rates;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
