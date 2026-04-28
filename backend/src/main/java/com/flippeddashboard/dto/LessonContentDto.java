package com.flippeddashboard.dto;

import java.util.List;

public class LessonContentDto {
    private Long lessonId;
    private String title;
    private List<LessonItemDto> items;

    public LessonContentDto(Long lessonId, String title, List<LessonItemDto> items) {
        this.lessonId = lessonId;
        this.title = title;
        this.items = items;
    }

    public Long getLessonId() { return lessonId; }
    public String getTitle() { return title; }
    public List<LessonItemDto> getItems() { return items; }
}
