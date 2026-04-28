package com.flippeddashboard.model;

import jakarta.persistence.*;

@Entity
@Table(name = "quiz_choices")
public class QuizChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_question_id")
    private QuizQuestion question;

    @Column(name = "choice_text")
    private String choiceText;

    @Column(name = "is_correct")
    private boolean isCorrect;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public QuizQuestion getQuestion() { return question; }
    public void setQuestion(QuizQuestion question) { this.question = question; }
    public String getChoiceText() { return choiceText; }
    public void setChoiceText(String choiceText) { this.choiceText = choiceText; }
    public boolean isCorrect() { return isCorrect; }
    public void setCorrect(boolean correct) { isCorrect = correct; }
}
