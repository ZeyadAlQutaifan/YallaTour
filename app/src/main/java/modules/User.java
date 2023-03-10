package modules;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String imageUrl ;
    private String email ;
    private boolean isAdmin = false;
    private String firstName ;
    private String lastName;
    private List<String> favPlaces;
    private List<String> reviewedPlaces = new ArrayList<>();;
    public User() {
    }

    public User(String imageUrl, String email, boolean isAdmin, String firstName, String lastName) {
        this.imageUrl = imageUrl;
        this.email = email;
        this.isAdmin = isAdmin;
        this.firstName = firstName;
        this.lastName = lastName;
        favPlaces = new ArrayList<>();
        reviewedPlaces = new ArrayList<>();
    }

    public User(String imageUrl, String email, boolean isAdmin, String firstName, String lastName, List<String> favPlaces) {
        this.imageUrl = imageUrl;
        this.email = email;
        this.isAdmin = isAdmin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.favPlaces = favPlaces;
        reviewedPlaces = new ArrayList<>();
    }

    public User(String imageUrl, String email, boolean isAdmin, String firstName, String lastName, List<String> favPlaces, List<String> reviewedPlaces) {
        this.imageUrl = imageUrl;
        this.email = email;
        this.isAdmin = isAdmin;
        this.firstName = firstName;
        this.lastName = lastName;
        this.favPlaces = favPlaces;
        this.reviewedPlaces = reviewedPlaces;

    }

    public List<String> getReviewedPlaces() {
        return reviewedPlaces;
    }

    public void setReviewedPlaces(List<String> reviewedPlaces) {
        this.reviewedPlaces = reviewedPlaces;
    }

    public List<String> getFavPlaces() {
        return favPlaces;
    }

    public void setFavPlaces(List<String> favPlaces) {
        this.favPlaces = favPlaces;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
