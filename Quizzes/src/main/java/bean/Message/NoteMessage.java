package bean.Message;

import java.util.Date;

public class NoteMessage extends Message {
    public NoteMessage(int message_id, int sender_id, int receiver_id, String content, Date timestamp, boolean is_read) {
        super(message_id, sender_id, receiver_id, MessageType.NOTE, content, timestamp, is_read);
    }
}