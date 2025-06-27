package bean.Questions;

public class AnswerOptions {
    private int id;
    private int questionId;
    private String answerText; //shemosaxazis teqsti
    private boolean is_correct;
    //option position sheidzleba

    public AnswerOptions(int questionId, String answerText, boolean is_correct) {
        this.questionId = questionId;
        this.answerText = answerText;
        this.is_correct = is_correct;
    }

    public int getId() {return id;}
    public int getQuestionId() {return questionId;}
    public String getAnswerText() {return answerText;}
    public boolean isCorrect() {return is_correct;}
}
