package com.flippeddashboard.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class LessonItemRequest {
    @Pattern(regexp = "VIDEO|TEXT|QUIZ|SURVEY", message = "must be one of VIDEO, TEXT, QUIZ, SURVEY")
    private String type;
    @Min(0)
    private Integer position;
    @Size(max = 200)
    private String title;
    @Size(max = 2000)
    private String videoUrl;
    @Size(max = 20000)
    private String body;

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
}
