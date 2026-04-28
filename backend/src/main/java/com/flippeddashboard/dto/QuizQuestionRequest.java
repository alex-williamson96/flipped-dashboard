package com.flippeddashboard.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

public class QuizQuestionRequest {
    @Size(max = 2000)
    private String text;
    @Min(0)
    private Integer position;
    @Size(max = 500)
    private String learningObjective;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public Integer getPosition() { return position; }
    public void setPosition(Integer position) { this.position = position; }
    public String getLearningObjective() { return learningObjective; }
    public void setLearningObjective(String learningObjective) { this.learningObjective = learningObjective; }
}
