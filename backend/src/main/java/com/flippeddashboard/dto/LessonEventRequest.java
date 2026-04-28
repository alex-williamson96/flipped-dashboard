package com.flippeddashboard.dto;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public class LessonEventRequest {
    @NotNull
    private Long studentId;
    @NotNull
    private Long lessonId;
    private OffsetDateTime occurredAt;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getLessonId() { return lessonId; }
    public void setLessonId(Long lessonId) { this.lessonId = lessonId; }
    public OffsetDateTime getOccurredAt() { return occurredAt; }
    public void setOccurredAt(OffsetDateTime occurredAt) { this.occurredAt = occurredAt; }
}
