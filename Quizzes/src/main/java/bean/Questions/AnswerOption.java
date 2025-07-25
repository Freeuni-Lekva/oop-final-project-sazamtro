package bean.Questions;

public class AnswerOption {
    private int id;
    private int questionId;
    private String answerText; //shemosaxazis teqsti
    private boolean is_correct;

    public AnswerOption(int id, int questionId, String answerText, boolean is_correct) {
        this.questionId = questionId;
        this.answerText = answerText;
        this.is_correct = is_correct;
        this.id = id;
    }

    public AnswerOption(int questionId, String answerText, boolean is_correct) {
        this(-1, questionId, answerText, is_correct);
    }


    public void setId(int id) {this.id = id;}
    public int getId() {return id;}
    public int getQuestionId() {return questionId;}
    public String getAnswerText() {return answerText;}
    public boolean isCorrect() {return is_correct;}
}
