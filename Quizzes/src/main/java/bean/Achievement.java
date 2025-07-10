package bean;

public class Achievement {
    private int achievement_id;
    private String achievement_name;
    private String achievement_descr;
    private String icon_url;

    public Achievement(int id, String name, String descr, String icon){
        this.achievement_id = id;
        this.achievement_name = name;
        this.achievement_descr = descr;
        this.icon_url = icon;
    }

    public int getAchievement_id(){
        return achievement_id;
    }

    public String getAchievement_name(){
        return achievement_name;
    }

    public String getAchievement_descr(){
        return achievement_descr;
    }

    public String getIcon_url(){
        return icon_url;
    }
}
