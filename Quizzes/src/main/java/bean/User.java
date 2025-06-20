package bean;

public class User {
    private int user_id;
    private String username;
    private String password;
    private boolean isAdmin;

    public User(int id, String name, String password, boolean b){
        this.user_id = id;
        this.username = name;
        this.password = password;
        this.isAdmin = b;
    }

    public int getUserId(){
        return user_id;
    }

    public String getUsername(){
        return username;
    }

    public String getPassword(){
        return password;
    }

    public boolean checkIfAdmin(){
        return isAdmin;
    }

}
