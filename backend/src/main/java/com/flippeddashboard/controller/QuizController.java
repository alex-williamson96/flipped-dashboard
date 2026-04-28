package com.flippeddashboard.controller;

import com.flippeddashboard.dto.*;
import com.flippeddashboard.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/lesson-items/{itemId}/quiz-questions")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<QuizQuestionResponse> addQuestion(@PathVariable Long itemId,
                                                          @Valid @RequestBody QuizQuestionRequest req) {
        return ApiResponse.ok(quizService.addQuestion(itemId, req));
    }

    @PutMapping("/quiz-questions/{id}")
    public ApiResponse<QuizQuestionResponse> updateQuestion(@PathVariable Long id,
                                                             @Valid @RequestBody QuizQuestionRequest req) {
        return ApiResponse.ok(quizService.updateQuestion(id, req));
    }

    @DeleteMapping("/quiz-questions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(@PathVariable Long id) {
        quizService.deleteQuestion(id);
    }

    @PostMapping("/quiz-questions/{questionId}/choices")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<QuizChoiceResponse> addChoice(@PathVariable Long questionId,
                                                      @Valid @RequestBody QuizChoiceRequest req) {
        return ApiResponse.ok(quizService.addChoice(questionId, req));
    }

    @PutMapping("/quiz-choices/{id}")
    public ApiResponse<QuizChoiceResponse> updateChoice(@PathVariable Long id,
                                                         @Valid @RequestBody QuizChoiceRequest req) {
        return ApiResponse.ok(quizService.updateChoice(id, req));
    }

    @DeleteMapping("/quiz-choices/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteChoice(@PathVariable Long id) {
        quizService.deleteChoice(id);
    }
}
