package com.flippeddashboard.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "video_watch_events")
public class VideoWatchEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_item_id")
    private LessonItem lessonItem;

    @Column(name = "watch_percent")
    private int watchPercent;

    @Column(name = "marked_at")
    private OffsetDateTime markedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }
    public LessonItem getLessonItem() { return lessonItem; }
    public void setLessonItem(LessonItem lessonItem) { this.lessonItem = lessonItem; }
    public int getWatchPercent() { return watchPercent; }
    public void setWatchPercent(int watchPercent) { this.watchPercent = watchPercent; }
    public OffsetDateTime getMarkedAt() { return markedAt; }
    public void setMarkedAt(OffsetDateTime markedAt) { this.markedAt = markedAt; }
}
