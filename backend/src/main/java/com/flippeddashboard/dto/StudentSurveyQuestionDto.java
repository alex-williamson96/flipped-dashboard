package com.flippeddashboard.dto;

public class StudentSurveyQuestionDto {
    private Long surveyQuestionId;
    private String prompt;
    private String questionType;

    public StudentSurveyQuestionDto(Long surveyQuestionId, String prompt, String questionType) {
        this.surveyQuestionId = surveyQuestionId;
        this.prompt = prompt;
        this.questionType = questionType;
    }

    public Long getSurveyQuestionId() { return surveyQuestionId; }
    public String getPrompt() { return prompt; }
    public String getQuestionType() { return questionType; }
}
