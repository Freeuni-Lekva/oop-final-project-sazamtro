package bean.Questions;

public class Question {
    private static final int DEFAULT_QUESTION_ID = -1;
    private static final int DEFAULT_POSITION = -1;

    private QuestionType questionType;
    private int id;
    private String questionText;
    private int position;
    private int quizId;

    private static final int defaultId = -1, defaultPosition = -1, defaultQuiz_id = -1;
    private static final String defaultText = null;
    private static final QuestionType defaultType = QuestionType.UNSPECIFIED;


    public Question(int id, int quizId, QuestionType type, String questionText, int position) {
        this.questionType = type;
        this.id = id;
        this.questionText = questionText;
        this.position = position;
        this.quizId = quizId;
    }
    public Question(int quiz_id, QuestionType type, String questionText, int position) {
        this.questionType = type;
        this.id = defaultId;
        this.questionText = questionText;
        this.position = position;
        this.quizId = quiz_id;
    }
    public Question(){
        this(defaultId, defaultQuiz_id, defaultType, defaultText, defaultPosition);
    }

    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public int getPosition() {
        return position;
    }

    public QuestionType getQuestionType() {
        return questionType;
    }

    public int getQuizId() {
        return quizId;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setQuestionType(QuestionType questionType) {
        this.questionType = questionType;
    }

    public void setQuiz_id(int quiz_id) {
        this.quizId = quiz_id;
    }


    public String getImageUrl(){
        return null;
    }
    public boolean hasChoices(){
        return false;
    }

}
