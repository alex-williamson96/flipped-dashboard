package com.flippeddashboard.controller;

import com.flippeddashboard.dto.*;
import com.flippeddashboard.service.SectionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class SectionController {

    private final SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @GetMapping("/courses/{courseId}/sections")
    public ApiResponse<List<SectionResponse>> getSections(@PathVariable Long courseId) {
        return ApiResponse.ok(sectionService.getSectionsByCourse(courseId));
    }

    @PostMapping("/courses/{courseId}/sections")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SectionResponse> createSection(@PathVariable Long courseId,
                                                       @Valid @RequestBody SectionRequest req) {
        return ApiResponse.ok(sectionService.createSection(courseId, req));
    }

    @PutMapping("/sections/{id}")
    public ApiResponse<SectionResponse> updateSection(@PathVariable Long id,
                                                       @Valid @RequestBody SectionRequest req) {
        return ApiResponse.ok(sectionService.updateSection(id, req));
    }

    @DeleteMapping("/sections/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteSection(@PathVariable Long id) {
        sectionService.deleteSection(id);
    }
}
