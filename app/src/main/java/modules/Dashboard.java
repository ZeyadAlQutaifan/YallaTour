package modules;

public class Dashboard {
    private int usersCount ;
    private int commentsCount ;
    private int navigationsCount ;
    private int nearbyCount ;
    private int placesCount ;

    public Dashboard(int usersCount, int commentsCount, int navigationsCount, int nearbyCount, int placesCount) {
        this.usersCount = usersCount;
        this.commentsCount = commentsCount;
        this.navigationsCount = navigationsCount;
        this.nearbyCount = nearbyCount;
        this.placesCount = placesCount;
    }

    public Dashboard() {
    }

    public int getUsersCount() {
        return usersCount;
    }

    public void setUsersCount(int usersCount) {
        this.usersCount = usersCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getNavigationsCount() {
        return navigationsCount;
    }

    public void setNavigationsCount(int navigationsCount) {
        this.navigationsCount = navigationsCount;
    }

    public int getNearbyCount() {
        return nearbyCount;
    }

    public void setNearbyCount(int nearbyCount) {
        this.nearbyCount = nearbyCount;
    }

    public int getPlacesCount() {
        return placesCount;
    }

    public void setPlacesCount(int placesCount) {
        this.placesCount = placesCount;
    }
    public void increaseUsers(){
        usersCount += 1 ;
    }
    public void increaseComments(){
        commentsCount += 1 ;
    }
    public void increaseNavigationsCount(){
        navigationsCount += 1 ;
    }
    public void increaseNearbyCount(){
        nearbyCount += 1 ;
    }
    public void increasePlacesCount(){
        placesCount += 1 ;
    }
    public void decreaseComments(){
        commentsCount -= 1 ;
    }
    public void decreaseNavigationsCount(){
        navigationsCount -= 1 ;
    }
    public void decreaseNearbyCount(){
        nearbyCount -= 1 ;
    }
    public void decreasePlacesCount(){
        placesCount -= 1 ;
    }
    public void decreaseUsers(){
        usersCount -= 1 ;
    }


}
