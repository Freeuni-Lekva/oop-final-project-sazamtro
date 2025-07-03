package bean.Questions;

public class QuestionResponse extends TextQuestion {


    public QuestionResponse(int id, int quizId, String questionText, int position) {
        super(id, quizId, QuestionType.QUESTION_RESPONSE, questionText, position);
    }
    public QuestionResponse(int quizId, String questionText, int position) {
        super(quizId, QuestionType.QUESTION_RESPONSE, questionText, position);
    }
    public QuestionResponse() {
        super();
        setQuestionType(QuestionType.QUESTION_RESPONSE);
    }
}
