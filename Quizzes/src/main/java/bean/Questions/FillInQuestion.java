package bean.Questions;

public class FillInQuestion extends TextQuestion {

    public FillInQuestion(int id, int quiz_id, String questionText, int position) {
        super(id, quiz_id, QuestionType.FILL_IN_THE_BLANK, questionText, position);
    }
    public FillInQuestion() {
        super();
        setQuestionType(QuestionType.FILL_IN_THE_BLANK);
    }
}
