package com.flippeddashboard.dto;

import jakarta.validation.constraints.Size;

public class QuizChoiceRequest {
    @Size(max = 1000)
    private String text;
    private Boolean isCorrect;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public Boolean getIsCorrect() { return isCorrect; }
    public void setIsCorrect(Boolean isCorrect) { this.isCorrect = isCorrect; }
}
