package com.flippeddashboard.controller;

import com.flippeddashboard.dto.*;
import com.flippeddashboard.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/class-overview")
    public ApiResponse<ClassOverviewResponse> getClassOverview(
            @RequestParam Long courseId,
            @RequestParam(required = false) Long sectionId,
            @RequestParam(required = false) Long lessonId,
            @RequestParam(required = false, defaultValue = "all") String completionStatus,
            @RequestParam(required = false) Integer minQuizScore,
            @RequestParam(required = false) Integer maxQuizScore,
            @RequestParam(required = false) Integer confidenceLevel) {
        return ApiResponse.ok(analyticsService.getClassOverview(
                courseId, sectionId, lessonId, completionStatus, minQuizScore, maxQuizScore, confidenceLevel));
    }

    @GetMapping("/lesson-timeline")
    public ApiResponse<LessonTimelineResponse> getLessonTimeline(
            @RequestParam Long courseId,
            @RequestParam(required = false) Long sectionId) {
        return ApiResponse.ok(analyticsService.getLessonTimeline(courseId, sectionId));
    }

    @GetMapping("/student/{studentId}")
    public ApiResponse<StudentDetailResponse> getStudentDetail(
            @PathVariable Long studentId,
            @RequestParam Long courseId,
            @RequestParam(required = false) Long lessonId) {
        return ApiResponse.ok(analyticsService.getStudentDetail(studentId, courseId, lessonId));
    }
}
