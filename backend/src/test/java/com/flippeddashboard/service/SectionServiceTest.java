package com.flippeddashboard.service;

import com.flippeddashboard.dto.SectionRequest;
import com.flippeddashboard.dto.SectionResponse;
import com.flippeddashboard.model.Course;
import com.flippeddashboard.model.Section;
import com.flippeddashboard.repository.CourseRepository;
import com.flippeddashboard.repository.SectionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @Mock private SectionRepository sectionRepository;
    @Mock private CourseRepository courseRepository;

    @InjectMocks private SectionService sectionService;

    @Test
    void createSection_savesAndReturns() {
        Course course = new Course();
        course.setId(1L);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        Section saved = section(10L, "Period 1");
        when(sectionRepository.save(any())).thenReturn(saved);

        SectionRequest req = new SectionRequest();
        req.setTitle("Period 1");
        SectionResponse result = sectionService.createSection(1L, req);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getTitle()).isEqualTo("Period 1");
    }

    @Test
    void createSection_throwsWhenCourseNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sectionService.createSection(99L, new SectionRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void updateSection_updatesNonNullFields() {
        Section section = section(10L, "Old");
        when(sectionRepository.findById(10L)).thenReturn(Optional.of(section));
        when(sectionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SectionRequest req = new SectionRequest();
        req.setTitle("New");
        SectionResponse result = sectionService.updateSection(10L, req);

        assertThat(result.getTitle()).isEqualTo("New");
    }

    @Test
    void updateSection_throwsWhenNotFound() {
        when(sectionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> sectionService.updateSection(99L, new SectionRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteSection_deletesById() {
        when(sectionRepository.existsById(10L)).thenReturn(true);

        sectionService.deleteSection(10L);

        verify(sectionRepository).deleteById(10L);
    }

    @Test
    void deleteSection_throwsWhenNotFound() {
        when(sectionRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> sectionService.deleteSection(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private Section section(Long id, String name) {
        Section s = new Section();
        s.setId(id);
        s.setName(name);
        return s;
    }
}
