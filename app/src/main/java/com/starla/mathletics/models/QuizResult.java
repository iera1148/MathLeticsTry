package com.starla.mathletics.models;

public class QuizResult {
    private int id;
    private int studentId;
    private int quizId;
    private String studentName;
    private String quizTitle;
    private int score;
    private int totalQuestions;
    private int timeTaken;
    private boolean timerMode;
    private boolean flashcardMode;
    private String completedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public int getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(int timeTaken) {
        this.timeTaken = timeTaken;
    }

    public boolean isTimerMode() {
        return timerMode;
    }

    public void setTimerMode(boolean timerMode) {
        this.timerMode = timerMode;
    }

    public boolean isFlashcardMode() {
        return flashcardMode;
    }

    public void setFlashcardMode(boolean flashcardMode) {
        this.flashcardMode = flashcardMode;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}
