package com.flippeddashboard.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    private String title;

    private int position;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "lesson", fetch = FetchType.LAZY)
    private List<LessonItem> items;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public List<LessonItem> getItems() { return items; }
    public void setItems(List<LessonItem> items) { this.items = items; }
}
