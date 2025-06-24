package bean;

public abstract class AbstractQuestion {
    private int id;
    private String questionText;
    private int position;
    private int questionScore;

    public int getId() {return id;}
    public String getQuestionText() {return questionText;}
    public int getPosition() {return position;}

}
