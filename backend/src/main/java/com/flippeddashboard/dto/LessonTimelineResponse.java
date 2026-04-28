package com.flippeddashboard.dto;

import java.util.List;

public class LessonTimelineResponse {
    private List<LessonTimelineRow> lessons;

    public List<LessonTimelineRow> getLessons() { return lessons; }
    public void setLessons(List<LessonTimelineRow> lessons) { this.lessons = lessons; }
}
