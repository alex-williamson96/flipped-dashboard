package com.flippeddashboard.dto;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public class QuizResponseRequest {
    @NotNull
    private Long studentId;
    @NotNull
    private Long quizQuestionId;
    @NotNull
    private Long quizChoiceId;
    private OffsetDateTime submittedAt;

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }
    public Long getQuizQuestionId() { return quizQuestionId; }
    public void setQuizQuestionId(Long quizQuestionId) { this.quizQuestionId = quizQuestionId; }
    public Long getQuizChoiceId() { return quizChoiceId; }
    public void setQuizChoiceId(Long quizChoiceId) { this.quizChoiceId = quizChoiceId; }
    public OffsetDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(OffsetDateTime submittedAt) { this.submittedAt = submittedAt; }
}
