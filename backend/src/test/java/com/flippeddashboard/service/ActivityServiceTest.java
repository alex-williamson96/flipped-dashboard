package com.flippeddashboard.service;

import com.flippeddashboard.dto.*;
import com.flippeddashboard.model.*;
import com.flippeddashboard.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private LessonRepository lessonRepository;
    @Mock private LessonEventRepository lessonEventRepository;
    @Mock private LessonItemRepository lessonItemRepository;
    @Mock private VideoWatchEventRepository videoWatchEventRepository;
    @Mock private QuizService quizService;
    @Mock private SurveyService surveyService;

    @InjectMocks
    private ActivityService activityService;

    private Student makeStudent(Long id) {
        Student s = new Student();
        s.setId(id);
        s.setName("Test Student");
        return s;
    }

    private Course makeActiveCourse() {
        Course c = new Course();
        c.setId(1L);
        c.setName("Test Course");
        c.setArchived(false);
        return c;
    }

    private Lesson makeLesson(Long id) {
        Lesson l = new Lesson();
        l.setId(id);
        l.setTitle("Test Lesson");
        l.setCourse(makeActiveCourse());
        return l;
    }

    private LessonItem makeVideoItem(Long id) {
        LessonItem item = new LessonItem();
        item.setId(id);
        item.setType(LessonItemType.VIDEO);
        item.setLesson(makeLesson(50L));
        return item;
    }

    private LessonEventRequest makeLessonEventRequest(Long studentId, Long lessonId) {
        LessonEventRequest r = new LessonEventRequest();
        r.setStudentId(studentId);
        r.setLessonId(lessonId);
        r.setOccurredAt(OffsetDateTime.now());
        return r;
    }

    // --- lesson-start ---

    @Test
    void recordLessonStart_mismatch_throwsIllegalArgument() {
        assertThatThrownBy(() -> activityService.recordLessonStart(2L, makeLessonEventRequest(1L, 12L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student ID mismatch");
    }

    @Test
    void recordLessonStart_studentNotFound_throwsIllegalArgument() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.recordLessonStart(1L, makeLessonEventRequest(1L, 12L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student not found");
    }

    @Test
    void recordLessonStart_lessonNotFound_throwsIllegalArgument() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(makeStudent(1L)));
        when(lessonRepository.findById(12L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.recordLessonStart(1L, makeLessonEventRequest(1L, 12L)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Lesson not found");
    }

    @Test
    void recordLessonStart_happyPath_savesEvent() {
        Student student = makeStudent(1L);
        Lesson lesson = makeLesson(12L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(lessonRepository.findById(12L)).thenReturn(Optional.of(lesson));
        when(lessonEventRepository.existsByStudentIdAndLessonIdAndEventType(1L, 12L, LessonEventType.STARTED))
                .thenReturn(false);

        activityService.recordLessonStart(1L, makeLessonEventRequest(1L, 12L));

        ArgumentCaptor<LessonEvent> captor = ArgumentCaptor.forClass(LessonEvent.class);
        verify(lessonEventRepository).save(captor.capture());
        assertThat(captor.getValue().getEventType()).isEqualTo(LessonEventType.STARTED);
    }

    @Test
    void recordLessonStart_idempotent_doesNotSaveSecondTime() {
        Student student = makeStudent(1L);
        Lesson lesson = makeLesson(12L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(lessonRepository.findById(12L)).thenReturn(Optional.of(lesson));
        when(lessonEventRepository.existsByStudentIdAndLessonIdAndEventType(1L, 12L, LessonEventType.STARTED))
                .thenReturn(true);

        activityService.recordLessonStart(1L, makeLessonEventRequest(1L, 12L));

        verify(lessonEventRepository, never()).save(any());
    }

    // --- lesson-complete ---

    @Test
    void recordLessonComplete_happyPath_savesCompletedEvent() {
        Student student = makeStudent(1L);
        Lesson lesson = makeLesson(12L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(lessonRepository.findById(12L)).thenReturn(Optional.of(lesson));
        when(lessonEventRepository.existsByStudentIdAndLessonIdAndEventType(1L, 12L, LessonEventType.COMPLETED))
                .thenReturn(false);

        activityService.recordLessonComplete(1L, makeLessonEventRequest(1L, 12L));

        ArgumentCaptor<LessonEvent> captor = ArgumentCaptor.forClass(LessonEvent.class);
        verify(lessonEventRepository).save(captor.capture());
        assertThat(captor.getValue().getEventType()).isEqualTo(LessonEventType.COMPLETED);
    }

    @Test
    void recordLessonComplete_idempotent_doesNotSaveSecondTime() {
        Student student = makeStudent(1L);
        Lesson lesson = makeLesson(12L);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(lessonRepository.findById(12L)).thenReturn(Optional.of(lesson));
        when(lessonEventRepository.existsByStudentIdAndLessonIdAndEventType(1L, 12L, LessonEventType.COMPLETED))
                .thenReturn(true);

        activityService.recordLessonComplete(1L, makeLessonEventRequest(1L, 12L));

        verify(lessonEventRepository, never()).save(any());
    }

    // --- quiz-response delegation ---

    @Test
    void recordQuizResponse_delegatesToQuizService() {
        QuizResponseRequest req = new QuizResponseRequest();
        req.setStudentId(1L);
        req.setQuizQuestionId(44L);
        req.setQuizChoiceId(201L);

        activityService.recordQuizResponse(1L, req);

        verify(quizService).recordResponse(1L, req);
    }

    // --- survey-response delegation ---

    @Test
    void recordSurveyResponse_delegatesToSurveyService() {
        SurveyResponseRequest req = new SurveyResponseRequest();
        req.setStudentId(1L);
        req.setSurveyQuestionId(18L);
        req.setLikertValue(4);

        activityService.recordSurveyResponse(1L, req);

        verify(surveyService).recordResponse(1L, req);
    }

    // --- video-watched ---

    @Test
    void recordVideoWatched_watchPercentTooHigh_throwsIllegalArgument() {
        VideoWatchRequest req = new VideoWatchRequest();
        req.setStudentId(1L);
        req.setLessonItemId(101L);
        req.setWatchPercent(150);
        req.setMarkedAt(OffsetDateTime.now());

        LessonItem item = makeVideoItem(101L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(makeStudent(1L)));
        when(lessonItemRepository.findById(101L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> activityService.recordVideoWatched(1L, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("watchPercent must be between 0 and 100");
    }

    @Test
    void recordVideoWatched_watchPercentNegative_throwsIllegalArgument() {
        VideoWatchRequest req = new VideoWatchRequest();
        req.setStudentId(1L);
        req.setLessonItemId(101L);
        req.setWatchPercent(-1);
        req.setMarkedAt(OffsetDateTime.now());

        LessonItem item = makeVideoItem(101L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(makeStudent(1L)));
        when(lessonItemRepository.findById(101L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> activityService.recordVideoWatched(1L, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("watchPercent must be between 0 and 100");
    }

    @Test
    void recordVideoWatched_itemNotVideo_throwsIllegalArgument() {
        VideoWatchRequest req = new VideoWatchRequest();
        req.setStudentId(1L);
        req.setLessonItemId(101L);
        req.setWatchPercent(80);
        req.setMarkedAt(OffsetDateTime.now());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(makeStudent(1L)));
        when(lessonItemRepository.findById(101L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.recordVideoWatched(1L, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Lesson item not found or is not of type VIDEO");
    }

    @Test
    void recordVideoWatched_happyPath_savesEvent() {
        VideoWatchRequest req = new VideoWatchRequest();
        req.setStudentId(1L);
        req.setLessonItemId(101L);
        req.setWatchPercent(95);
        req.setMarkedAt(OffsetDateTime.now());

        LessonItem item = makeVideoItem(101L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(makeStudent(1L)));
        when(lessonItemRepository.findById(101L)).thenReturn(Optional.of(item));

        activityService.recordVideoWatched(1L, req);

        ArgumentCaptor<VideoWatchEvent> captor = ArgumentCaptor.forClass(VideoWatchEvent.class);
        verify(videoWatchEventRepository).save(captor.capture());
        assertThat(captor.getValue().getWatchPercent()).isEqualTo(95);
    }
}
