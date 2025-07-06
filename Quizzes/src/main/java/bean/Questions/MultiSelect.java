package bean.Questions;

public class MultiSelect extends ChoiceQuestions {


    public MultiSelect(int id, int quizId, String questionText, int position) {
        super(id, quizId, QuestionType.MULTI_SELECT, questionText, position);
    }
    public MultiSelect(int quizId, String questionText, int position) {
        super(quizId, QuestionType.MULTI_SELECT, questionText, position);
    }
    public MultiSelect() {
        super();
        setQuestionType(QuestionType.MULTI_SELECT);
    }
}
