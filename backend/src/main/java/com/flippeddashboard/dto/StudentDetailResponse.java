package com.flippeddashboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDetailResponse {
    private Long studentId;
    private String name;
    private String sectionName;
    private StudentLessonDetail lesson;
    private List<LessonScoreRow> allLessons;
    private Double totalGrade;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSectionName() { return sectionName; }
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }
    public StudentLessonDetail getLesson() { return lesson; }
    public void setLesson(StudentLessonDetail lesson) { this.lesson = lesson; }
    public List<LessonScoreRow> getAllLessons() { return allLessons; }
    public void setAllLessons(List<LessonScoreRow> allLessons) { this.allLessons = allLessons; }
    public Double getTotalGrade() { return totalGrade; }
    public void setTotalGrade(Double totalGrade) { this.totalGrade = totalGrade; }
}
