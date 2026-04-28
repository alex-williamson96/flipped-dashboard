package com.flippeddashboard.controller;

import com.flippeddashboard.dto.*;
import com.flippeddashboard.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ApiResponse<List<CourseResponse>> listCourses(@RequestParam(required = false) String archived) {
        return ApiResponse.ok(courseService.listCourses(archived));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CourseResponse> createCourse(@Valid @RequestBody CourseRequest req) {
        return ApiResponse.ok(courseService.createCourse(req));
    }

    @GetMapping("/{id}")
    public ApiResponse<CourseResponse> getCourse(@PathVariable Long id) {
        return ApiResponse.ok(courseService.getCourseWithSections(id));
    }

    @PutMapping("/{id}")
    public ApiResponse<CourseResponse> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseRequest req) {
        return ApiResponse.ok(courseService.updateCourse(id, req));
    }

    @PatchMapping("/{id}")
    public ApiResponse<CourseResponse> patchCourse(@PathVariable Long id, @Valid @RequestBody CourseRequest req) {
        return ApiResponse.ok(courseService.patchCourse(id, req));
    }

    @PostMapping("/{id}/clone")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<CourseResponse> cloneCourse(@PathVariable Long id, @Valid @RequestBody CourseCloneRequest req) {
        return ApiResponse.ok(courseService.cloneCourse(id, req));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }
}
