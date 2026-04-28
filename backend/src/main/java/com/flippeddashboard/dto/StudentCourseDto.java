package com.flippeddashboard.dto;

public class StudentCourseDto {
    private Long courseId;
    private String title;
    private Long sectionId;
    private String sectionName;
    private boolean isArchived;

    public StudentCourseDto(Long courseId, String title, Long sectionId, String sectionName, boolean isArchived) {
        this.courseId = courseId;
        this.title = title;
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.isArchived = isArchived;
    }

    public Long getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public Long getSectionId() { return sectionId; }
    public String getSectionName() { return sectionName; }
    public boolean isArchived() { return isArchived; }
}
