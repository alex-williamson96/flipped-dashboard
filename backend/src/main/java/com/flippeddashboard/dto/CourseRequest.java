package com.flippeddashboard.dto;

import jakarta.validation.constraints.Size;

public class CourseRequest {
    @Size(max = 200)
    private String title;
    @Size(max = 2000)
    private String description;
    @Size(max = 50)
    private String term;
    private Boolean isArchived;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }
    public Boolean getIsArchived() { return isArchived; }
    public void setIsArchived(Boolean isArchived) { this.isArchived = isArchived; }
}
