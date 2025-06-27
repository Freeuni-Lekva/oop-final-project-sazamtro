package bean.Questions;

public class MultipleChoice extends ChoiceQuestions {
    public MultipleChoice(int id, String questionText, int position, int questionScore) {
        super(QuestionType.MULTIPLE_CHOICE, id, questionText, position, questionScore);
    }

}


