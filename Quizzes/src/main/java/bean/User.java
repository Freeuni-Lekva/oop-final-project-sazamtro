package bean;

public class User {
    private int user_id;
    private String username;
    private String hashedPassword;
    private String profilePictureUrl;
    private boolean isAdmin;


    public User(int id, String name, String hashedPassword, String profilePictureUrl, boolean b){
        this.user_id = id;
        this.username = name;
        this.hashedPassword = hashedPassword;
        this.profilePictureUrl = profilePictureUrl;
        this.isAdmin = b;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int newId) {
        this.user_id = newId;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return hashedPassword;
    }

    public boolean checkIfAdmin(){
        return isAdmin;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String url) {
        this.profilePictureUrl = url;
    }
}
