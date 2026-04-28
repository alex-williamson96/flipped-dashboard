package com.flippeddashboard.dto;

import java.util.List;

public class LessonDetailResponse {
    private Long id;
    private String title;
    private int position;
    private boolean isActive;
    private Long courseId;
    private List<LessonItemResponse> items;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }
    public List<LessonItemResponse> getItems() { return items; }
    public void setItems(List<LessonItemResponse> items) { this.items = items; }
}
