package bean.Questions;

public class PictureResponse extends TextQuestion{
    String image_url;

    public PictureResponse(int id, int quiz_id, String questionText, int position, String image_url) {
        super(id, quiz_id, QuestionType.PICTURE_RESPONSE, questionText, position);
        this.image_url = image_url;
    }
    public PictureResponse() {
        super();
        setQuestionType(QuestionType.PICTURE_RESPONSE);
    }

    public String getImage_url() {
        return image_url;
    }
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
