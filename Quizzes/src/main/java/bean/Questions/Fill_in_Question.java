package bean.Questions;

public class Fill_in_Question extends TextQuestion {
    public Fill_in_Question(int id, String questionText, int position, int questionScore) {
        super(QuestionType.FILL_IN_THE_BLANK ,id, questionText, position, questionScore);
    }
}
