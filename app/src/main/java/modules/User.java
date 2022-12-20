package modules;

public class User {
    private String username ;
    private String imageUrl ;
    private String email ;
    private boolean isAdmin = false;

    public User(String username, String imageUrl, String email, boolean isAdmin) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public User(String username, String imageUrl, String email) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.email = email;
    }

    public User() {
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
