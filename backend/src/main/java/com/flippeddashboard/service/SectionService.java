package com.flippeddashboard.service;

import com.flippeddashboard.dto.SectionRequest;
import com.flippeddashboard.dto.SectionResponse;
import java.util.List;
import com.flippeddashboard.model.Course;
import com.flippeddashboard.model.Section;
import com.flippeddashboard.repository.CourseRepository;
import com.flippeddashboard.repository.SectionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SectionService {

    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;

    public SectionService(SectionRepository sectionRepository, CourseRepository courseRepository) {
        this.sectionRepository = sectionRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    public List<SectionResponse> getSectionsByCourse(Long courseId) {
        return sectionRepository.findByCourseId(courseId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public SectionResponse createSection(Long courseId, SectionRequest req) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));
        if (req.getTitle() == null || req.getTitle().isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
        Section section = new Section();
        section.setCourse(course);
        section.setName(req.getTitle());
        return toResponse(sectionRepository.save(section));
    }

    @Transactional
    public SectionResponse updateSection(Long id, SectionRequest req) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Section not found: " + id));
        if (req.getTitle() != null) section.setName(req.getTitle());
        return toResponse(sectionRepository.save(section));
    }

    @Transactional
    public void deleteSection(Long id) {
        if (!sectionRepository.existsById(id)) {
            throw new EntityNotFoundException("Section not found: " + id);
        }
        sectionRepository.deleteById(id);
    }

    private SectionResponse toResponse(Section section) {
        SectionResponse resp = new SectionResponse();
        resp.setId(section.getId());
        resp.setTitle(section.getName());
        return resp;
    }
}
