package bean;

public class Achievement {
    private int achievement_id;
    private String achievement_name;
    private String achievement_descr;
    private String icon_url;
    private String username;


    public Achievement(int id, String name, String descr, String icon){
        this.achievement_id = id;
        this.achievement_name = name;
        this.achievement_descr = descr;
        this.icon_url = icon;
    }

    public Achievement(int id, String name, String descr, String icon, String username) {
        this(id, name, descr, icon);
        this.username = username;
    }

    public int getAchievement_id() {
        return achievement_id;
    }

    public String getAchievement_name() {
        return achievement_name;
    }

    public String getAchievement_descr() {
        return achievement_descr;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public String getUsername() {
        return username;
    }

    public void setAchievement_id(int achievement_id) {
        this.achievement_id = achievement_id;
    }

    public void setAchievement_name(String achievement_name) {
        this.achievement_name = achievement_name;
    }

    public void setAchievement_descr(String achievement_descr) {
        this.achievement_descr = achievement_descr;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
