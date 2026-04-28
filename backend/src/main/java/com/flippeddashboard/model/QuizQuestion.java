package com.flippeddashboard.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "quiz_questions")
public class QuizQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_item_id")
    private LessonItem lessonItem;

    @Column(name = "question_text")
    private String questionText;

    private int position;

    @Column(name = "learning_objective")
    private String learningObjective;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
    private List<QuizChoice> choices;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LessonItem getLessonItem() { return lessonItem; }
    public void setLessonItem(LessonItem lessonItem) { this.lessonItem = lessonItem; }
    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public List<QuizChoice> getChoices() { return choices; }
    public void setChoices(List<QuizChoice> choices) { this.choices = choices; }
    public String getLearningObjective() { return learningObjective; }
    public void setLearningObjective(String learningObjective) { this.learningObjective = learningObjective; }
}
