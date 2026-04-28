package com.flippeddashboard.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class LessonRequest {
    @Size(max = 200)
    private String title;
    @Min(0)
    private Integer position;
    private Boolean isActive;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
