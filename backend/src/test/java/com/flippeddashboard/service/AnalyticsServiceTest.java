package com.flippeddashboard.service;

import com.flippeddashboard.dto.ClassOverviewResponse;
import com.flippeddashboard.dto.LessonTimelineResponse;
import com.flippeddashboard.dto.StudentDetailResponse;
import com.flippeddashboard.model.*;
import com.flippeddashboard.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceTest {

    @Mock private CourseRepository courseRepository;
    @Mock private StudentRepository studentRepository;
    @Mock private LessonRepository lessonRepository;
    @Mock private LessonItemRepository lessonItemRepository;
    @Mock private LessonEventRepository lessonEventRepository;
    @Mock private QuizResponseRepository quizResponseRepository;
    @Mock private SurveyResponseRepository surveyResponseRepository;
    @Mock private VideoWatchEventRepository videoWatchEventRepository;
    @Mock private AnalyticsQuestionCache questionCache;

    @InjectMocks private AnalyticsService analyticsService;

    @Test
    void patternFlagForLesson_lowCompletionHighScore_returnsFlag() {
        assertThat(analyticsService.patternFlagForLesson(0.5, 80.0))
                .isEqualTo("LOW_COMPLETION_HIGH_SCORE");
    }

    @Test
    void patternFlagForLesson_highCompletionLowScore_returnsFlag() {
        assertThat(analyticsService.patternFlagForLesson(0.8, 55.0))
                .isEqualTo("HIGH_COMPLETION_LOW_SCORE");
    }

    @Test
    void patternFlagForLesson_neitherCondition_returnsNull() {
        assertThat(analyticsService.patternFlagForLesson(0.7, 70.0)).isNull();
    }

    @Test
    void patternFlagForLesson_nullScore_returnsNull() {
        assertThat(analyticsService.patternFlagForLesson(0.5, null)).isNull();
    }

    @Test
    void getClassOverview_nullCourseId_throwsIllegalArgument() {
        assertThatThrownBy(() -> analyticsService.getClassOverview(null, null, null, "all", null, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("courseId is required");
    }

    @Test
    void getClassOverview_invalidCompletionStatus_throwsIllegalArgument() {
        assertThatThrownBy(() -> analyticsService.getClassOverview(1L, null, null, "unknown", null, null, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid completionStatus value");
    }

    @Test
    void getClassOverview_courseNotFound_throwsEntityNotFound() {
        when(courseRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> analyticsService.getClassOverview(99L, null, null, "all", null, null, null))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getClassOverview_computesCompletionRate() {
        when(courseRepository.existsById(1L)).thenReturn(true);

        Lesson lesson = lesson(10L, "Lesson A", 1);
        when(lessonRepository.findByCourseIdOrderByPositionAsc(1L)).thenReturn(List.of(lesson));

        Section section = section(5L, "Section 1");
        Student s1 = student(1L, "Alice", section);
        Student s2 = student(2L, "Bob", section);
        when(studentRepository.findBySectionCourseId(1L)).thenReturn(List.of(s1, s2));

        when(lessonItemRepository.findByLessonIdInOrderByPositionAsc(List.of(10L))).thenReturn(List.of());
        when(questionCache.forItems(any())).thenReturn(new AnalyticsQuestionCache.Snapshot(Map.of(), Map.of()));

        LessonEvent completionEvent = new LessonEvent();
        completionEvent.setStudent(s1);
        completionEvent.setLesson(lesson);
        completionEvent.setEventType(LessonEventType.COMPLETED);
        when(lessonEventRepository.findByStudentIdInAndEventType(List.of(1L, 2L), LessonEventType.COMPLETED))
                .thenReturn(List.of(completionEvent));

        when(quizResponseRepository.findByStudentIdIn(List.of(1L, 2L))).thenReturn(List.of());
        when(surveyResponseRepository.findByStudentIdIn(List.of(1L, 2L))).thenReturn(List.of());

        ClassOverviewResponse result = analyticsService.getClassOverview(1L, null, null, "all", null, null, null);

        assertThat(result.getTotalStudents()).isEqualTo(2);
        assertThat(result.getCompletedCount()).isEqualTo(1);
        assertThat(result.getCompletionRate()).isEqualTo(0.5);
    }

    @Test
    void getLessonTimeline_patternFlagAppearsInResponse() {
        when(courseRepository.existsById(1L)).thenReturn(true);

        Lesson lesson = lesson(10L, "Lesson A", 1);
        when(lessonRepository.findByCourseIdOrderByPositionAsc(1L)).thenReturn(List.of(lesson));

        Section section = section(5L, "Section 1");
        Student s1 = student(1L, "Alice", section);
        when(studentRepository.findBySectionCourseId(1L)).thenReturn(List.of(s1));

        when(lessonItemRepository.findByLessonIdInOrderByPositionAsc(List.of(10L))).thenReturn(List.of());
        when(questionCache.forItems(any())).thenReturn(new AnalyticsQuestionCache.Snapshot(Map.of(), Map.of()));
        when(lessonEventRepository.findByStudentIdInAndEventType(List.of(1L), LessonEventType.COMPLETED))
                .thenReturn(List.of());
        when(quizResponseRepository.findByStudentIdIn(List.of(1L))).thenReturn(List.of());

        LessonTimelineResponse result = analyticsService.getLessonTimeline(1L, null);

        assertThat(result.getLessons()).hasSize(1);
        assertThat(result.getLessons().get(0).getLessonId()).isEqualTo(10L);
        assertThat(result.getLessons().get(0).getPatternFlag()).isNull();
    }

    @Test
    void getStudentDetail_studentNotFound_throwsEntityNotFound() {
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> analyticsService.getStudentDetail(99L, 1L, null))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Student not found");
    }

    private Lesson lesson(Long id, String title, int position) {
        Lesson l = new Lesson();
        l.setId(id);
        l.setTitle(title);
        l.setPosition(position);
        l.setActive(true);
        return l;
    }

    private Section section(Long id, String name) {
        Section s = new Section();
        s.setId(id);
        s.setName(name);
        return s;
    }

    private Student student(Long id, String name, Section section) {
        Student s = new Student();
        s.setId(id);
        s.setName(name);
        s.setSection(section);
        return s;
    }
}
