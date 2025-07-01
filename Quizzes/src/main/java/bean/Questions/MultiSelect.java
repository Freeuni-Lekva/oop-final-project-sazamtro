package bean.Questions;

public class MultiSelect extends ChoiceQuestions {


    public MultiSelect(int id, int quiz_id, String questionText, int position) {
        super(id, quiz_id, QuestionType.MULTI_SELECT, questionText, position);
    }
    public MultiSelect() {
        super();
        setQuestionType(QuestionType.MULTI_SELECT);
    }
}
