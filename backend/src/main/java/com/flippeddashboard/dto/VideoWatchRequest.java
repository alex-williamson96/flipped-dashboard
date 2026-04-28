package com.flippeddashboard.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public class VideoWatchRequest {
    @NotNull
    private Long studentId;
    @NotNull
    private Long lessonItemId;
    @Min(0) @Max(100)
    private int watchPercent;
    private OffsetDateTime markedAt;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getLessonItemId() { return lessonItemId; }
    public void setLessonItemId(Long lessonItemId) { this.lessonItemId = lessonItemId; }
    public int getWatchPercent() { return watchPercent; }
    public void setWatchPercent(int watchPercent) { this.watchPercent = watchPercent; }
    public OffsetDateTime getMarkedAt() { return markedAt; }
    public void setMarkedAt(OffsetDateTime markedAt) { this.markedAt = markedAt; }
}
