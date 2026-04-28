package com.flippeddashboard.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flippeddashboard.dto.ItemPositionResponse;
import com.flippeddashboard.dto.LessonDetailResponse;
import com.flippeddashboard.dto.LessonRequest;
import com.flippeddashboard.dto.LessonResponse;
import com.flippeddashboard.dto.ReorderRequest;
import com.flippeddashboard.model.*;
import com.flippeddashboard.repository.*;
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
class LessonServiceTest {

    @Mock private LessonRepository lessonRepository;
    @Mock private CourseRepository courseRepository;
    @Mock private LessonItemRepository lessonItemRepository;
    @Mock private QuizQuestionRepository quizQuestionRepository;
    @Mock private QuizChoiceRepository quizChoiceRepository;
    @Mock private SurveyQuestionRepository surveyQuestionRepository;
    @Mock private ObjectMapper objectMapper;

    @InjectMocks private LessonService lessonService;

    @Test
    void getLessonsByCourse_returnsLessonsOrderedByPosition() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        Lesson lesson = lesson(100L, "Cell Structure", 1);
        when(lessonRepository.findByCourseIdOrderByPositionAsc(1L)).thenReturn(List.of(lesson));

        List<LessonResponse> result = lessonService.getLessonsByCourse(1L);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("Cell Structure");
    }

    @Test
    void getLessonsByCourse_throwsWhenCourseNotFound() {
        when(courseRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> lessonService.getLessonsByCourse(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void createLesson_savesAndReturns() {
        Course course = new Course();
        course.setId(1L);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        Lesson saved = lesson(100L, "Cell Structure", 1);
        when(lessonRepository.save(any())).thenReturn(saved);

        LessonRequest req = new LessonRequest();
        req.setTitle("Cell Structure");
        req.setPosition(1);
        req.setIsActive(true);

        LessonResponse result = lessonService.createLesson(1L, req);

        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getTitle()).isEqualTo("Cell Structure");
    }

    @Test
    void createLesson_throwsWhenCourseNotFound() {
        when(courseRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.createLesson(99L, new LessonRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getLessonDetail_returnsLessonWithItems() {
        Course course = new Course();
        course.setId(1L);
        Lesson lesson = lesson(100L, "Cell Structure", 1);
        lesson.setCourse(course);
        when(lessonRepository.findById(100L)).thenReturn(Optional.of(lesson));
        when(lessonItemRepository.findByLessonIdOrderByPositionAsc(100L)).thenReturn(Collections.emptyList());

        LessonDetailResponse result = lessonService.getLessonDetail(100L);

        assertThat(result.getId()).isEqualTo(100L);
        assertThat(result.getCourseId()).isEqualTo(1L);
        assertThat(result.getItems()).isEmpty();
    }

    @Test
    void getLessonDetail_throwsWhenNotFound() {
        when(lessonRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.getLessonDetail(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void updateLesson_updatesNonNullFields() {
        Lesson lesson = lesson(100L, "Old Title", 1);
        when(lessonRepository.findById(100L)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        LessonRequest req = new LessonRequest();
        req.setTitle("New Title");

        LessonResponse result = lessonService.updateLesson(100L, req);

        assertThat(result.getTitle()).isEqualTo("New Title");
    }

    @Test
    void updateLesson_throwsWhenNotFound() {
        when(lessonRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> lessonService.updateLesson(99L, new LessonRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteLesson_deletesById() {
        when(lessonRepository.existsById(100L)).thenReturn(true);

        lessonService.deleteLesson(100L);

        verify(lessonRepository).deleteById(100L);
    }

    @Test
    void deleteLesson_throwsWhenNotFound() {
        when(lessonRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> lessonService.deleteLesson(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void reorderLessons_reassignsPositions() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        Lesson l1 = lesson(10L, "Lesson A", 1);
        Lesson l2 = lesson(20L, "Lesson B", 2);
        when(lessonRepository.findByCourseIdOrderByPositionAsc(1L)).thenReturn(List.of(l1, l2));
        when(lessonRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ReorderRequest req = new ReorderRequest();
        req.setItemIds(List.of(20L, 10L));

        List<ItemPositionResponse> result = lessonService.reorderLessons(1L, req);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(20L);
        assertThat(result.get(0).getPosition()).isEqualTo(1);
        assertThat(result.get(1).getId()).isEqualTo(10L);
        assertThat(result.get(1).getPosition()).isEqualTo(2);
    }

    @Test
    void reorderLessons_throwsWhenCourseNotFound() {
        when(courseRepository.existsById(99L)).thenReturn(false);

        ReorderRequest req = new ReorderRequest();
        req.setItemIds(List.of(1L));

        assertThatThrownBy(() -> lessonService.reorderLessons(99L, req))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void reorderLessons_throwsWhenLessonDoesNotBelongToCourse() {
        when(courseRepository.existsById(1L)).thenReturn(true);
        Lesson l1 = lesson(10L, "Lesson A", 1);
        when(lessonRepository.findByCourseIdOrderByPositionAsc(1L)).thenReturn(List.of(l1));

        ReorderRequest req = new ReorderRequest();
        req.setItemIds(List.of(99L));

        assertThatThrownBy(() -> lessonService.reorderLessons(1L, req))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private Lesson lesson(Long id, String title, int position) {
        Lesson l = new Lesson();
        l.setId(id);
        l.setTitle(title);
        l.setPosition(position);
        l.setActive(true);
        return l;
    }
}
