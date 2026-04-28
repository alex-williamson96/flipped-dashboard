package com.flippeddashboard.service;

import com.flippeddashboard.dto.CourseRequest;
import com.flippeddashboard.dto.CourseResponse;
import com.flippeddashboard.model.Course;
import com.flippeddashboard.model.Section;
import com.flippeddashboard.repository.CourseRepository;
import com.flippeddashboard.repository.LessonItemRepository;
import com.flippeddashboard.repository.LessonRepository;
import com.flippeddashboard.repository.SectionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock private CourseRepository courseRepository;
    @Mock private SectionRepository sectionRepository;
    @Mock private LessonRepository lessonRepository;
    @Mock private LessonItemRepository lessonItemRepository;
    @Mock private QuizService quizService;
    @Mock private SurveyService surveyService;

    @InjectMocks private CourseService courseService;

    @Test
    void listCourses_returnsAllCourses() {
        Course course = course(1L, "Bio 101", "desc");
        when(courseRepository.findByIsArchivedFalse()).thenReturn(List.of(course));

        List<CourseResponse> result = courseService.listCourses(null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Bio 101");
    }

    @Test
    void createCourse_savesAndReturns() {
        CourseRequest req = new CourseRequest();
        req.setTitle("Bio 101");
        req.setDescription("desc");

        Course saved = course(1L, "Bio 101", "desc");
        when(courseRepository.save(any())).thenReturn(saved);

        CourseResponse result = courseService.createCourse(req);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Bio 101");
    }

    @Test
    void getCourseWithSections_returnsCourseWithSections() {
        Course course = course(1L, "Bio 101", "desc");
        Section section = new Section();
        section.setId(10L);
        section.setName("Period 1");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(sectionRepository.findByCourseId(1L)).thenReturn(List.of(section));

        CourseResponse result = courseService.getCourseWithSections(1L);

        assertThat(result.getTitle()).isEqualTo("Bio 101");
        assertThat(result.getSections()).hasSize(1);
        assertThat(result.getSections().get(0).getTitle()).isEqualTo("Period 1");
    }

    @Test
    void getCourseWithSections_throwsWhenNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.getCourseWithSections(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void updateCourse_updatesNonNullFields() {
        Course course = course(1L, "Old Title", "old desc");
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(courseRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        CourseRequest req = new CourseRequest();
        req.setTitle("New Title");

        CourseResponse result = courseService.updateCourse(1L, req);

        assertThat(result.getTitle()).isEqualTo("New Title");
    }

    @Test
    void updateCourse_throwsWhenNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.updateCourse(99L, new CourseRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteCourse_deletesById() {
        when(courseRepository.existsById(1L)).thenReturn(true);

        courseService.deleteCourse(1L);

        verify(courseRepository).deleteById(1L);
    }

    @Test
    void deleteCourse_throwsWhenNotFound() {
        when(courseRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> courseService.deleteCourse(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private Course course(Long id, String name, String description) {
        Course c = new Course();
        c.setId(id);
        c.setName(name);
        c.setDescription(description);
        return c;
    }
}
