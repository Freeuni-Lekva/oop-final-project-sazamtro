package bean;

import java.sql.Timestamp;

public class QuizAttempt {

    private int attemptId;
    private int userId;
    private int quizId;
    private int score;
    private double timeTakenMin;
    private boolean isPractice;
    private Timestamp takenAt;


    public QuizAttempt() {}

    public QuizAttempt(int attemptId, int userId, int quizId, int score, double timeTakenMin, boolean isPractice, Timestamp takenAt) {
        this.attemptId = attemptId;
        this.userId = userId;
        this.quizId = quizId;
        this.score = score;
        this.timeTakenMin = timeTakenMin;
        this.isPractice = isPractice;
        this.takenAt = takenAt;
    }

    public int getAttemptId() {
        return attemptId;
    }

    public void setAttemptId(int attemptId) {
        this.attemptId = attemptId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getTimeTakenMin() {
        return timeTakenMin;
    }

    public void setTimeTakenMin(double timeTakenMin) {
        this.timeTakenMin = timeTakenMin;
    }

    public boolean isPractice() {
        return isPractice;
    }

    public void setPractice(boolean isPractice) {
        this.isPractice = isPractice;
    }

    public Timestamp getTakenAt() {
        return takenAt;
    }

    public void setTakenAt(Timestamp takenAt) {
        this.takenAt = takenAt;
    }
}