package com.flippeddashboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyResponseDetail {
    private String questionText;
    private String type;
    private Integer likertValue;
    private String freeText;

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getLikertValue() { return likertValue; }
    public void setLikertValue(Integer likertValue) { this.likertValue = likertValue; }
    public String getFreeText() { return freeText; }
    public void setFreeText(String freeText) { this.freeText = freeText; }
}
