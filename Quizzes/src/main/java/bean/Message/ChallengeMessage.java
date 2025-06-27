package bean.Message;

import java.util.Date;

public class ChallengeMessage extends Message {
    private int quiz_id;

    public ChallengeMessage(int message_id, int sender_id, int receiver_id, String content, int quiz_id, Date timestamp, boolean is_read) {
        super(message_id, sender_id, receiver_id, MessageType.CHALLENGE, content, timestamp, is_read);
        this.quiz_id = quiz_id;
    }

    public int getQuiz_id(){return quiz_id;}
}