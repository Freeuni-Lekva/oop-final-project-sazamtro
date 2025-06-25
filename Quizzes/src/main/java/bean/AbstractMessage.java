package bean;

import java.util.Date;
import bean.MessageType;

public abstract class AbstractMessage {
    private int message_id;
    private int sender_id;
    private int receiver_id;
    private MessageType type;
    private String content;
    private Date timestamp;
    private boolean is_read;

    public AbstractMessage(int message_id, int sender_id, int receiver_id, MessageType type, String content, Date timestamp, boolean is_read){
        this.message_id = message_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.type = type;
        this.content = content;
        this.timestamp = timestamp;
        this.is_read = is_read;
    }

    public int getSender_id(){return sender_id;}
    public int getReceiver_id(){return receiver_id;}
    public MessageType getType(){return type;}
    public String getContent(){return content;}
    public Date getTimestamp(){return timestamp;}
    public boolean isRead(){return is_read;}

}
