package com.flippeddashboard.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(name = "term")
    private String term;

    @Column(name = "is_archived")
    private boolean isArchived = false;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Section> sections;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY)
    private List<Lesson> lessons;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Section> getSections() { return sections; }
    public void setSections(List<Section> sections) { this.sections = sections; }
    public List<Lesson> getLessons() { return lessons; }
    public void setLessons(List<Lesson> lessons) { this.lessons = lessons; }
    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }
    public boolean isArchived() { return isArchived; }
    public void setArchived(boolean isArchived) { this.isArchived = isArchived; }
}
