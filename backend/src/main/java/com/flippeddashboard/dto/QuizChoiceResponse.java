package com.flippeddashboard.dto;

public class QuizChoiceResponse {
    private Long id;
    private String text;
    private boolean isCorrect;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(boolean isCorrect) { this.isCorrect = isCorrect; }
}
