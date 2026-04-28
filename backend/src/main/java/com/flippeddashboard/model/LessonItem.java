package com.flippeddashboard.model;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.List;

@Entity
@Table(name = "lesson_items")
public class LessonItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lesson_id")
    private Lesson lesson;

    private int position;

    @Enumerated(EnumType.STRING)
    private LessonItemType type;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private String content;

    @OneToMany(mappedBy = "lessonItem", fetch = FetchType.LAZY)
    private List<QuizQuestion> quizQuestions;

    @OneToMany(mappedBy = "lessonItem", fetch = FetchType.LAZY)
    private List<SurveyQuestion> surveyQuestions;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Lesson getLesson() { return lesson; }
    public void setLesson(Lesson lesson) { this.lesson = lesson; }
    public int getPosition() { return position; }
    public void setPosition(int position) { this.position = position; }
    public LessonItemType getType() { return type; }
    public void setType(LessonItemType type) { this.type = type; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public List<QuizQuestion> getQuizQuestions() { return quizQuestions; }
    public void setQuizQuestions(List<QuizQuestion> quizQuestions) { this.quizQuestions = quizQuestions; }
    public List<SurveyQuestion> getSurveyQuestions() { return surveyQuestions; }
    public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) { this.surveyQuestions = surveyQuestions; }
}
