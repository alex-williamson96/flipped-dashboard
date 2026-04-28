package com.flippeddashboard.controller;

import com.flippeddashboard.dto.*;
import com.flippeddashboard.service.LessonService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class LessonController {

    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    @PutMapping("/courses/{courseId}/lessons/reorder")
    public ApiResponse<List<ItemPositionResponse>> reorderLessons(@PathVariable Long courseId,
                                                                   @Valid @RequestBody ReorderRequest req) {
        return ApiResponse.ok(lessonService.reorderLessons(courseId, req));
    }

    @GetMapping("/courses/{courseId}/lessons")
    public ApiResponse<List<LessonResponse>> getLessons(@PathVariable Long courseId) {
        return ApiResponse.ok(lessonService.getLessonsByCourse(courseId));
    }

    @PostMapping("/courses/{courseId}/lessons")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<LessonResponse> createLesson(@PathVariable Long courseId,
                                                     @Valid @RequestBody LessonRequest req) {
        return ApiResponse.ok(lessonService.createLesson(courseId, req));
    }

    @GetMapping("/lessons/{id}")
    public ApiResponse<LessonDetailResponse> getLesson(@PathVariable Long id) {
        return ApiResponse.ok(lessonService.getLessonDetail(id));
    }

    @PutMapping("/lessons/{id}")
    public ApiResponse<LessonResponse> updateLesson(@PathVariable Long id,
                                                     @Valid @RequestBody LessonRequest req) {
        return ApiResponse.ok(lessonService.updateLesson(id, req));
    }

    @DeleteMapping("/lessons/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLesson(@PathVariable Long id) {
        lessonService.deleteLesson(id);
    }
}
