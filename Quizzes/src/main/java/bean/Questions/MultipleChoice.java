package bean.Questions;

public class MultipleChoice extends ChoiceQuestions {


    public MultipleChoice(int id, int quizId, String questionText, int position) {
        super(id, quizId, QuestionType.MULTIPLE_CHOICE, questionText, position);
    }
    public MultipleChoice(int quizId, String questionText, int position) {
        super(quizId, QuestionType.MULTIPLE_CHOICE, questionText, position);
    }
    public MultipleChoice() {
        super();
        setQuestionType(QuestionType.MULTIPLE_CHOICE);
    }
}


