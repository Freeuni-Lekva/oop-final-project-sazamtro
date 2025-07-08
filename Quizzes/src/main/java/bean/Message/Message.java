package bean.Message;

import java.sql.Timestamp;
import java.util.Date;

public abstract class Message {
    public static final int DEFAULT_MESSAGE_ID = -1;


    private int message_id;
    private final int sender_id;
    private final int receiver_id;
    private MessageType type;
    private Timestamp timestamp;
    private boolean is_read;

    public Message(int message_id, int sender_id, int receiver_id, MessageType type, Timestamp timestamp, boolean is_read){
        this.message_id = message_id;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.timestamp = timestamp;
        this.is_read = is_read;
    }
    public Message(int sender_id, int receiver_id){
        this.message_id = DEFAULT_MESSAGE_ID;
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.timestamp = null;
        this.is_read = false;
    }

    public int getSender_id(){return sender_id;}
    public int getReceiver_id(){return receiver_id;}
    public abstract String getMessageText();
    public abstract String getContent();
    public Timestamp getTimestamp(){return timestamp;}
    public boolean isRead(){return is_read;}
    public abstract MessageType getMessageType();
    public int getMessageId(){return message_id;}

    public void setMessage_id(int message_id){
        this.message_id = message_id;
    }

    public void setTimestamp(Timestamp timestamp){
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if(! (o instanceof Message)) return false;
        Message other = (Message) o;


        return other.getSender_id() == this.getSender_id() &&
                other.getReceiver_id() == this.getReceiver_id() &&
                other.getMessageText().equals(this.getMessageText()) &&
                other.getContent().equals(this.getContent()) &&
                other.isRead() == this.isRead() &&
                other.getMessageType() == this.getMessageType() &&
                other.getMessageId() == this.getMessageId() &&
                other.getTimestamp().equals(this.getTimestamp());
    }
}