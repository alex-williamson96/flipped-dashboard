package com.flippeddashboard.dto;

import java.util.List;

public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private List<SectionResponse> sections;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    private String term;
    private boolean isArchived;

    public List<SectionResponse> getSections() { return sections; }
    public void setSections(List<SectionResponse> sections) { this.sections = sections; }
    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }
    public boolean isArchived() { return isArchived; }
    public void setArchived(boolean isArchived) { this.isArchived = isArchived; }
}
