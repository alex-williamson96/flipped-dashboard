package com.flippeddashboard.dto;

public class SurveyQuestionResponse {
    private Long id;
    private String text;
    private int position;
    private String questionType;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public String getQuestionType() { return questionType; }
    public void setQuestionType(String questionType) { this.questionType = questionType; }
}
