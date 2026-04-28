package com.flippeddashboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonTimelineRow {
    private Long lessonId;
    private String title;
    private int position;
    private double completionRate;
    private Double avgQuizScore;
    private String patternFlag;

    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public double getCompletionRate() { return completionRate; }
    public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }
    public Double getAvgQuizScore() { return avgQuizScore; }
    public void setAvgQuizScore(Double avgQuizScore) { this.avgQuizScore = avgQuizScore; }
    public String getPatternFlag() { return patternFlag; }
    public void setPatternFlag(String patternFlag) { this.patternFlag = patternFlag; }
}
