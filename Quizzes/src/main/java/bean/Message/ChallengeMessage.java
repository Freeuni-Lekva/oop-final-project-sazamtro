package bean.Message;

import java.util.Date;

public class ChallengeMessage extends Message {
    private final int quiz_id;
    private final String content;
    private static final String TEMPLATE = "You Have Received A Challenge \n";
    public ChallengeMessage(int message_id, int sender_id, int receiver_id, String content, int quiz_id, Date timestamp, boolean is_read) {
        super(message_id, sender_id, receiver_id, MessageType.CHALLENGE, timestamp, is_read);
        this.quiz_id = quiz_id;
        this.content = content == null ? "" : content;
    }

    public int getQuiz_id(){return quiz_id;}

    @Override
    public String getContent() {
        return TEMPLATE + content;
    }

    @Override
    public MessageType getMessageType() {
        return MessageType.CHALLENGE;
    }
}