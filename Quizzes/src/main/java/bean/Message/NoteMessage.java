package bean.Message;

import java.sql.Timestamp;
import java.util.Date;

public class NoteMessage extends Message {
    private final String content;
    public NoteMessage(int message_id, int sender_id, int receiver_id, String content, Timestamp timestamp, boolean is_read) {
        super(message_id, sender_id, receiver_id, MessageType.NOTE, timestamp, is_read);
        this.content = content;
    }

    public NoteMessage(int sender_id, int receiver_id, String content){
        super(sender_id, receiver_id);
        this.content = content;
    }
    @Override
    public String getContent() {
        return content;
    }

    @Override
    public String getMessageText(){return content;}

    @Override
    public MessageType getMessageType(){
        return MessageType.NOTE;
    }
}