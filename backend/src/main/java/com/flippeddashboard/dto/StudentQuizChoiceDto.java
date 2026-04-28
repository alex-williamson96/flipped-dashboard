package com.flippeddashboard.dto;

public class StudentQuizChoiceDto {
    private Long quizChoiceId;
    private String text;

    public StudentQuizChoiceDto(Long quizChoiceId, String text) {
        this.quizChoiceId = quizChoiceId;
        this.text = text;
    }

    public Long getQuizChoiceId() { return quizChoiceId; }
    public String getText() { return text; }
}
