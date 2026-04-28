package com.flippeddashboard.controller;

import com.flippeddashboard.dto.*;
import com.flippeddashboard.service.SurveyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    @PostMapping("/lesson-items/{itemId}/survey-questions")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<SurveyQuestionResponse> addQuestion(@PathVariable Long itemId,
                                                            @Valid @RequestBody SurveyQuestionRequest req) {
        return ApiResponse.ok(surveyService.addQuestion(itemId, req));
    }

    @PutMapping("/survey-questions/{id}")
    public ApiResponse<SurveyQuestionResponse> updateQuestion(@PathVariable Long id,
                                                               @Valid @RequestBody SurveyQuestionRequest req) {
        return ApiResponse.ok(surveyService.updateQuestion(id, req));
    }

    @DeleteMapping("/survey-questions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(@PathVariable Long id) {
        surveyService.deleteQuestion(id);
    }
}
