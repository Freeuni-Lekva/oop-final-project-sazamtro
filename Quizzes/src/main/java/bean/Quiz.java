package bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Quiz {

    private int quiz_id;
    private String title;
    private String description;
    private int creator_id;
    private boolean is_random;
    private boolean is_multipage;
    private boolean immediate_correction;
    private LocalDateTime created_at;

    public Quiz(int id, String title, String descr, int cr_id, boolean random, boolean multipage,
                boolean immediate_correction, LocalDateTime date_time){
        this.quiz_id = id;
        this.title = title;
        this.description = descr;
        this.creator_id = cr_id;
        this.is_random = random;
        this.is_multipage = multipage;
        this.immediate_correction = immediate_correction;
        this.created_at = date_time;
    }

    public int getQuiz_id(){
        return quiz_id;
    }

    public String getQuizTitle(){
        return title;
    }

    public String getQuizDescription(){
        return description;
    }

    public int getCreator_id(){
        return creator_id;
    }

    public boolean checkIfRandom(){
        return is_random;
    }

    public boolean checkIfMultipage(){
        return is_multipage;
    }

    public boolean checkIfImmediate_correction(){
        return immediate_correction;
    }

    public LocalDateTime getCreationDate(){
        return created_at;
    }

}
