package com.flippeddashboard.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class SurveyQuestionRequest {
    @Size(max = 2000)
    private String text;
    @Min(0)
    private Integer position;
    @Pattern(regexp = "LIKERT|FREE_TEXT", message = "must be LIKERT or FREE_TEXT")
    private String questionType;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }
}
