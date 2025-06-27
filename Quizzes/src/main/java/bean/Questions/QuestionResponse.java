package bean.Questions;

public class QuestionResponse extends TextQuestion {
    public QuestionResponse(int id, String questionText, int position, int questionScore) {
        super(QuestionType.QUESTION_RESPONSE, id, questionText, position, questionScore);
    }

}
