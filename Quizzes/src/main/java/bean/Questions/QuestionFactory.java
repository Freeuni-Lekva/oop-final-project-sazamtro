package bean.Questions;

public class QuestionFactory {
    public static Question createQuestion(int quizId, QuestionType type, String questionText, int position, String imageUrl) {
        switch (type) {
            case MULTIPLE_CHOICE: return new MultipleChoice(quizId, questionText, position);
            case QUESTION_RESPONSE: return new QuestionResponse(quizId, questionText, position);
            case MULTI_SELECT: return new MultiSelect(quizId, questionText, position);
            case FILL_IN_THE_BLANK: return new FillInQuestion(quizId, questionText, position);
            case PICTURE_RESPONSE: return new PictureResponse(quizId, questionText, position, imageUrl);
            default: return null;
        }
    }
}