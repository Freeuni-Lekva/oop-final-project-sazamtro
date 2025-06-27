package bean.Questions;

public class PictureResponse extends TextQuestion{
    String image_url;
    public PictureResponse(int id, String questionText, int position, String image_url, int questionScore) {
        super(QuestionType.PICTURE_RESPONSE ,id, questionText, position, questionScore);
        this.image_url = image_url;
    }
    public String getImage_url() {return image_url;}
}
