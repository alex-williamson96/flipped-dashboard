package com.flippeddashboard.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.OffsetDateTime;

public class SurveyResponseRequest {
    @NotNull
    private Long studentId;
    @NotNull
    private Long surveyQuestionId;
    @Min(1) @Max(5)
    private Integer likertValue;
    @Size(max = 5000)
    private String freeText;
    private OffsetDateTime submittedAt;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getSurveyQuestionId() { return surveyQuestionId; }
    public void setSurveyQuestionId(Long surveyQuestionId) { this.surveyQuestionId = surveyQuestionId; }
    public Integer getLikertValue() { return likertValue; }
    public void setLikertValue(Integer likertValue) { this.likertValue = likertValue; }
    public String getFreeText() { return freeText; }
    public void setFreeText(String freeText) { this.freeText = freeText; }
    public OffsetDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(OffsetDateTime submittedAt) { this.submittedAt = submittedAt; }
}
