package com.flippeddashboard.dto;

import java.util.List;

public class SectionResponse {
    private Long id;
    private String title;
    private List<LessonResponse> lessons;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public List<LessonResponse> getLessons() { return lessons; }
    public void setLessons(List<LessonResponse> lessons) { this.lessons = lessons; }
}
