package bean.Message;

import java.util.Date;

public abstract class Message {
    public static final int DEFAULT_MESSAGE_ID = 1;


    private int message_id;
    private final int sender_id;
    private final int receiver_id;
    private MessageType type;
    private final Date timestamp;
    private boolean is_read;

    public Message(int message_id, int sender_id, int receiver_id, MessageType type, Date timestamp, boolean is_read){
        this.message_id = message_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.timestamp = timestamp;
        this.is_read = is_read;
    }

    public int getSender_id(){return sender_id;}
    public int getReceiver_id(){return receiver_id;}
    public abstract String getContent();
    public Date getTimestamp(){return timestamp;}
    public boolean isRead(){return is_read;}
    public abstract MessageType getMessageType();

    public void setMessage_id(int message_id){
        this.message_id = message_id;
    }
}