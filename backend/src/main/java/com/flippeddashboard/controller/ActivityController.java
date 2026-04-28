package com.flippeddashboard.controller;

import com.flippeddashboard.dto.*;
import com.flippeddashboard.service.ActivityService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/activity")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/lesson-start")
    public ApiResponse<String> lessonStart(
            @Valid @RequestBody LessonEventRequest request,
            @RequestHeader("X-Student-Id") Long headerStudentId) {
        activityService.recordLessonStart(headerStudentId, request);
        return ApiResponse.ok("ok");
    }

    @PostMapping("/lesson-complete")
    public ApiResponse<String> lessonComplete(
            @Valid @RequestBody LessonEventRequest request,
            @RequestHeader("X-Student-Id") Long headerStudentId) {
        activityService.recordLessonComplete(headerStudentId, request);
        return ApiResponse.ok("ok");
    }

    @PostMapping("/quiz-response")
    public ApiResponse<String> quizResponse(
            @Valid @RequestBody QuizResponseRequest request,
            @RequestHeader("X-Student-Id") Long headerStudentId) {
        activityService.recordQuizResponse(headerStudentId, request);
        return ApiResponse.ok("ok");
    }

    @PostMapping("/survey-response")
    public ApiResponse<String> surveyResponse(
            @Valid @RequestBody SurveyResponseRequest request,
            @RequestHeader("X-Student-Id") Long headerStudentId) {
        activityService.recordSurveyResponse(headerStudentId, request);
        return ApiResponse.ok("ok");
    }

    @PostMapping("/video-watched")
    public ApiResponse<String> videoWatched(
            @Valid @RequestBody VideoWatchRequest request,
            @RequestHeader("X-Student-Id") Long headerStudentId) {
        activityService.recordVideoWatched(headerStudentId, request);
        return ApiResponse.ok("ok");
    }
}
