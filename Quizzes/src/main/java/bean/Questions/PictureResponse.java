package bean.Questions;

public class PictureResponse extends TextQuestion{
    String imageUrl;

    public PictureResponse(int id, int quizId, String questionText, int position, String imageUrl) {
        super(id, quizId, QuestionType.PICTURE_RESPONSE, questionText, position);
        this.imageUrl = imageUrl;
    }
    public PictureResponse(int quizId, String questionText, int position, String imageUrl) {
        super(quizId, QuestionType.PICTURE_RESPONSE, questionText, position);
        this.imageUrl = imageUrl;
    }
    public PictureResponse() {
        super();
        setQuestionType(QuestionType.PICTURE_RESPONSE);
    }

    @Override
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String image_url) {
        this.imageUrl = image_url;
    }
}
