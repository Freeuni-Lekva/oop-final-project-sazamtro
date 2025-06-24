package bean;

public class MultipleChoice extends AbstractQuestion {
    public MultipleChoice(int id, String questionText, int position, int questionScore) {
        super(QuestionTypes.MULTIPLE_CHOICE, id, questionText, position, questionScore);
    }
}
