package com.flippeddashboard.dto;

public class StudentDto {
    private Long id;
    private String name;
    private Long sectionId;

    public StudentDto(Long id, String name, Long sectionId) {
        this.id = id;
        this.name = name;
        this.sectionId = sectionId;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public Long getSectionId() { return sectionId; }
}
