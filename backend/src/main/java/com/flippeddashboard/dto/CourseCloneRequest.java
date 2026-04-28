package com.flippeddashboard.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CourseCloneRequest {
    @NotBlank
    @Size(max = 50)
    private String term;

    public String getTerm() { return term; }
    public void setTerm(String term) { this.term = term; }
}
