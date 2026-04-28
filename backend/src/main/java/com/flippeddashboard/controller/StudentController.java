package com.flippeddashboard.controller;

import com.flippeddashboard.dto.*;
import com.flippeddashboard.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/students")
    public ApiResponse<List<StudentDto>> getAllStudents() {
        return ApiResponse.ok(studentService.getAllStudents());
    }

    @GetMapping("/students/{studentId}/courses")
    public ApiResponse<List<StudentCourseDto>> getCourses(
            @PathVariable Long studentId,
            @RequestHeader("X-Student-Id") Long headerStudentId,
            @RequestParam(required = false) String archived) {
        return ApiResponse.ok(studentService.getCoursesForStudent(headerStudentId, studentId, archived));
    }

    @GetMapping("/students/{studentId}/courses/{courseId}/lessons")
    public ApiResponse<List<LessonSummaryDto>> getLessons(
            @PathVariable Long studentId,
            @PathVariable Long courseId,
            @RequestHeader("X-Student-Id") Long headerStudentId) {
        return ApiResponse.ok(studentService.getLessonsForStudentCourse(headerStudentId, studentId, courseId));
    }

    @GetMapping("/lessons/{lessonId}/content")
    public ApiResponse<LessonContentDto> getLessonContent(
            @PathVariable Long lessonId,
            @RequestHeader(value = "X-Student-Id", required = false) Long headerStudentId) {
        return ApiResponse.ok(studentService.getLessonContent(headerStudentId, lessonId));
    }
}
