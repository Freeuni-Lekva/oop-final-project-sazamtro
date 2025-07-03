package bean.Questions;

public class FillInQuestion extends TextQuestion {

    public FillInQuestion(int id, int quizId, String questionText, int position) {
        super(id, quizId, QuestionType.FILL_IN_THE_BLANK, questionText, position);
    }
    public FillInQuestion(int quizId, String questionText, int position) {
        super(quizId, QuestionType.FILL_IN_THE_BLANK, questionText, position);
    }
    public FillInQuestion() {
        super();
        setQuestionType(QuestionType.FILL_IN_THE_BLANK);
    }
}
