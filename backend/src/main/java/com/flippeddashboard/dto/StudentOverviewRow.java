package com.flippeddashboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentOverviewRow {
    private Long studentId;
    private String name;
    private String sectionName;
    private boolean completed;
    private Integer quizScore;
    private Double totalGrade;
    private Integer confidence;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSectionName() { return sectionName; }
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public Integer getQuizScore() { return quizScore; }
    public void setQuizScore(Integer quizScore) { this.quizScore = quizScore; }
    public Double getTotalGrade() { return totalGrade; }
    public void setTotalGrade(Double totalGrade) { this.totalGrade = totalGrade; }
    public Integer getConfidence() { return confidence; }
    public void setConfidence(Integer confidence) { this.confidence = confidence; }
}
