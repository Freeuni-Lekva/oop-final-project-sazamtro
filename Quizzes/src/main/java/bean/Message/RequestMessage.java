package bean.Message;

import java.util.Date;

public class RequestMessage extends Message {
    public RequestMessage(int message_id, int sender_id, int receiver_id, String content, Date timestamp, boolean is_read) {
        super(message_id, sender_id, receiver_id, MessageType.FRIEND_REQUEST, content, timestamp, is_read);
    }
}