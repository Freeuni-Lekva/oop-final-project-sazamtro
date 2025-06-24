package bean;

public class QuestionResponse extends AbstractQuestion{
    public QuestionResponse(int id, String questionText, int position, int questionScore) {
        super(QuestionTypes.QUESTION_RESPONSE, id, questionText, position, questionScore);
    }
}
