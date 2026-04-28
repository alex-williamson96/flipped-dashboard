package com.flippeddashboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonItemDto {
    private Long lessonItemId;
    private int position;
    private String type;
    private String videoUrl;
    private String body;
    private List<?> questions;
    private Boolean completed;

    public Long getLessonItemId() { return lessonItemId; }
    public void setLessonItemId(Long lessonItemId) { this.lessonItemId = lessonItemId; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public List<?> getQuestions() { return questions; }
    public void setQuestions(List<?> questions) { this.questions = questions; }
    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }
}
