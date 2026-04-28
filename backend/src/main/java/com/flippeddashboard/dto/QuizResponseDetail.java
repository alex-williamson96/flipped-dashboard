package com.flippeddashboard.dto;

public class QuizResponseDetail {
    private String questionText;
    private String chosenAnswer;
    private boolean correct;

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getChosenAnswer() { return chosenAnswer; }
    public void setChosenAnswer(String chosenAnswer) { this.chosenAnswer = chosenAnswer; }
    public boolean isCorrect() { return correct; }
    public void setCorrect(boolean correct) { this.correct = correct; }
}
