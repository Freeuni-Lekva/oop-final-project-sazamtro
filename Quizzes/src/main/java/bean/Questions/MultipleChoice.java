package bean.Questions;

public class MultipleChoice extends ChoiceQuestions {


    public MultipleChoice(int id, int quiz_id, String questionText, int position) {
        super(id, quiz_id, QuestionType.MULTIPLE_CHOICE, questionText, position);
    }
    public MultipleChoice() {
        super();
        setQuestionType(QuestionType.MULTIPLE_CHOICE);
    }
}


