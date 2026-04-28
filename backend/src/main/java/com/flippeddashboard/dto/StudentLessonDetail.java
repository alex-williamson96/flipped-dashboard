package com.flippeddashboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentLessonDetail {
    private Long lessonId;
    private String title;
    private boolean completed;
    private Integer watchPercent;
    private List<QuizResponseDetail> quizResponses;
    private List<SurveyResponseDetail> surveyResponses;

    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
    public Integer getWatchPercent() { return watchPercent; }
    public void setWatchPercent(Integer watchPercent) { this.watchPercent = watchPercent; }
    public List<QuizResponseDetail> getQuizResponses() { return quizResponses; }
    public void setQuizResponses(List<QuizResponseDetail> quizResponses) { this.quizResponses = quizResponses; }
    public List<SurveyResponseDetail> getSurveyResponses() { return surveyResponses; }
    public void setSurveyResponses(List<SurveyResponseDetail> surveyResponses) { this.surveyResponses = surveyResponses; }
}
