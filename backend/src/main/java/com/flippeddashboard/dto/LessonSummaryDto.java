package com.flippeddashboard.dto;

public class LessonSummaryDto {
    private Long lessonId;
    private String title;
    private int position;
    private boolean completed;

    public LessonSummaryDto(Long lessonId, String title, int position, boolean completed) {
        this.lessonId = lessonId;
        this.title = title;
        this.position = position;
        this.completed = completed;
    }

    public Long getLessonId() { return lessonId; }
    public String getTitle() { return title; }
    public int getPosition() { return position; }
    public boolean isCompleted() { return completed; }
}
