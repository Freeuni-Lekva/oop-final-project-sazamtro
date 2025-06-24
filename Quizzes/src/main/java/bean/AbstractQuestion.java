package bean;

public class AbstractQuestion {
    private QuestionTypes questionType;
    private int id;
    private String questionText;
    private int position;

    public AbstractQuestion(QuestionTypes type,int id, String questionText, int position, int questionScore) {
        this.questionType = type;
        this.id = id;
        this.questionText = questionText;
        this.position = position;
    }

    public int getId() {return id;}
    public String getQuestionText() {return questionText;}
    public int getPosition() {return position;}
    public QuestionTypes getQuestionType() {return questionType;}
}
