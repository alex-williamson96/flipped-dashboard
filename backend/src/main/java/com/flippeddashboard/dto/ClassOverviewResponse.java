package com.flippeddashboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassOverviewResponse {
    private int totalStudents;
    private int completedCount;
    private double completionRate;
    private Double avgQuizScore;
    private Double avgConfidence;
    private List<StudentOverviewRow> students;
    private List<LoAccuracyDto> loAccuracy;
    private int enrolledCount;
    private int startedCount;
    private int surveyedCount;

    public int getTotalStudents() { return totalStudents; }
    public void setTotalStudents(int totalStudents) { this.totalStudents = totalStudents; }
    public int getEnrolledCount() { return enrolledCount; }
    public void setEnrolledCount(int enrolledCount) { this.enrolledCount = enrolledCount; }
    public int getCompletedCount() { return completedCount; }
    public void setCompletedCount(int completedCount) { this.completedCount = completedCount; }
    public double getCompletionRate() { return completionRate; }
    public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }
    public Double getAvgQuizScore() { return avgQuizScore; }
    public void setAvgQuizScore(Double avgQuizScore) { this.avgQuizScore = avgQuizScore; }
    public Double getAvgConfidence() { return avgConfidence; }
    public void setAvgConfidence(Double avgConfidence) { this.avgConfidence = avgConfidence; }
    public List<StudentOverviewRow> getStudents() { return students; }
    public void setStudents(List<StudentOverviewRow> students) { this.students = students; }
    public List<LoAccuracyDto> getLoAccuracy() { return loAccuracy; }
    public void setLoAccuracy(List<LoAccuracyDto> loAccuracy) { this.loAccuracy = loAccuracy; }
    public int getStartedCount() { return startedCount; }
    public void setStartedCount(int startedCount) { this.startedCount = startedCount; }
    public int getSurveyedCount() { return surveyedCount; }
    public void setSurveyedCount(int surveyedCount) { this.surveyedCount = surveyedCount; }
}
