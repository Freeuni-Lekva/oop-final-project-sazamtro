package bean;

import java.sql.Timestamp;

public class Announcement {
    private int id;
    private int administratorId;
    private String administratorUsername;
    private String text;
    private Timestamp doneAt;


    public Announcement(int id, int administratorId, String administratorUsername, String text, Timestamp doneAt) {
        this.id = id;
        this.administratorId = administratorId;
        this.administratorUsername = administratorUsername;
        this.text = text;
        this.doneAt = doneAt;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int newId) {
        this.id = newId;
    }

    public int getAdministratorId() {
        return this.administratorId;
    }

    public String getAdministratorUsername() {
        return this.administratorUsername;
    }

    public String getText() {
        return this.text;
    }

    public Timestamp getDoneAt() {
        return doneAt;
    }
}
