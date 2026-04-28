package com.flippeddashboard.dto;

import java.util.List;

public class StudentQuizQuestionDto {
    private Long quizQuestionId;
    private String prompt;
    private List<StudentQuizChoiceDto> choices;

    public StudentQuizQuestionDto(Long quizQuestionId, String prompt, List<StudentQuizChoiceDto> choices) {
        this.quizQuestionId = quizQuestionId;
        this.prompt = prompt;
        this.choices = choices;
    }

    public Long getQuizQuestionId() { return quizQuestionId; }
    public String getPrompt() { return prompt; }
    public List<StudentQuizChoiceDto> getChoices() { return choices; }
}
