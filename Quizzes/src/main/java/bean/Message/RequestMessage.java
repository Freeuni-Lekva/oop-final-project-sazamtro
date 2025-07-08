package bean.Message;

import java.sql.Timestamp;
import java.util.Date;

public class RequestMessage extends Message {
    private static final String TEMPLATE = " Has Sent You A Friend Request";
    private final String sender_username;
    public RequestMessage(int message_id, int sender_id, int receiver_id, String sender_username, Timestamp timestamp, boolean is_read) {
        super(message_id, sender_id, receiver_id, MessageType.FRIEND_REQUEST, timestamp, is_read);
        this.sender_username = sender_username;
    }

    public RequestMessage(int sender_id, int receiver_id, String sender_username) {
        super(sender_id, receiver_id);
        this.sender_username = sender_username;
    }

    @Override
    public String getContent() {
        return sender_username;
    }

    @Override
    public String getMessageText(){return sender_username + TEMPLATE;}

    @Override
    public MessageType getMessageType() {
        return MessageType.FRIEND_REQUEST;
    }
}