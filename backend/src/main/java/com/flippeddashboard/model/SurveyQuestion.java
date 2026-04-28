package com.flippeddashboard.model;

import jakarta.persistence.*;

@Entity
@Table(name = "survey_questions")
public class SurveyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_item_id")
    private LessonItem lessonItem;

    @Column(name = "question_text")
    private String questionText;

    @Enumerated(EnumType.STRING)
    private SurveyQuestionType type;

    private int position;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LessonItem getLessonItem() { return lessonItem; }
    public void setLessonItem(LessonItem lessonItem) { this.lessonItem = lessonItem; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public SurveyQuestionType getType() { return type; }
    public void setType(SurveyQuestionType type) { this.type = type; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
}
