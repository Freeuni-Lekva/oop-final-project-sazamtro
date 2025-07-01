package bean.Questions;

public class QuestionResponse extends TextQuestion {


    public QuestionResponse(int id, int quiz_id, String questionText, int position) {
        super(id, quiz_id, QuestionType.QUESTION_RESPONSE, questionText, position);
    }
    public QuestionResponse() {
        super();
        setQuestionType(QuestionType.QUESTION_RESPONSE);
    }
}
