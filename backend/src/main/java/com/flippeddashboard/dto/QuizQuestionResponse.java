package com.flippeddashboard.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuizQuestionResponse {
    private Long id;
    private String text;
    private int position;
    private String learningObjective;
    private List<QuizChoiceResponse> choices;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public String getLearningObjective() { return learningObjective; }
    public void setLearningObjective(String learningObjective) { this.learningObjective = learningObjective; }
    public List<QuizChoiceResponse> getChoices() { return choices; }
    public void setChoices(List<QuizChoiceResponse> choices) { this.choices = choices; }
}
