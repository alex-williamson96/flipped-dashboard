package com.flippeddashboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class LessonItemResponse {
    private Long id;
    private String type;
    private int position;
    private String title;
    private String videoUrl;
    private String body;
    private List<?> questions;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public List<?> getQuestions() { return questions; }
    public void setQuestions(List<?> questions) { this.questions = questions; }
}
