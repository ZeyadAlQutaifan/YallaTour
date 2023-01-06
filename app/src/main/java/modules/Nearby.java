package modules;

import java.io.Serializable;

public class Nearby implements Serializable {

    private String imgUrl;
    private String title;
    private String key;

    public Nearby(String imgUrl, String title, String key) {
        this.imgUrl = imgUrl;
        this.title = title;
        this.key = key;
    }

    public Nearby() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Nearby(String imgUrl, String title) {
        this.imgUrl = imgUrl;
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
