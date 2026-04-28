package com.flippeddashboard.service;

import com.flippeddashboard.dto.SurveyQuestionRequest;
import com.flippeddashboard.dto.SurveyQuestionResponse;
import com.flippeddashboard.dto.SurveyResponseRequest;
import com.flippeddashboard.model.*;
import com.flippeddashboard.repository.LessonItemRepository;
import com.flippeddashboard.repository.StudentRepository;
import com.flippeddashboard.repository.SurveyQuestionRepository;
import com.flippeddashboard.repository.SurveyResponseRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SurveyServiceTest {

    @Mock private StudentRepository studentRepository;
    @Mock private LessonItemRepository lessonItemRepository;
    @Mock private SurveyQuestionRepository surveyQuestionRepository;
    @Mock private SurveyResponseRepository surveyResponseRepository;
    @Mock private SanitizationService sanitizationService;

    @InjectMocks private SurveyService surveyService;

    @Test
    void addQuestion_savesAndReturns() {
        LessonItem item = surveyItem(1L);
        when(lessonItemRepository.findById(1L)).thenReturn(Optional.of(item));
        SurveyQuestion saved = question(10L, "How did you find this?", 1, SurveyQuestionType.LIKERT);
        when(surveyQuestionRepository.save(any())).thenReturn(saved);

        SurveyQuestionRequest req = new SurveyQuestionRequest();
        req.setText("How did you find this?");
        req.setPosition(1);
        req.setQuestionType("LIKERT");

        SurveyQuestionResponse result = surveyService.addQuestion(1L, req);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getText()).isEqualTo("How did you find this?");
        assertThat(result.getQuestionType()).isEqualTo("LIKERT");
    }

    @Test
    void addQuestion_throwsWhenItemNotFound() {
        when(lessonItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> surveyService.addQuestion(99L, new SurveyQuestionRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void addQuestion_throwsWhenNotSurveyType() {
        LessonItem item = new LessonItem();
        item.setId(1L);
        item.setType(LessonItemType.QUIZ);
        when(lessonItemRepository.findById(1L)).thenReturn(Optional.of(item));

        assertThatThrownBy(() -> surveyService.addQuestion(1L, new SurveyQuestionRequest()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateQuestion_updatesNonNullFields() {
        SurveyQuestion question = question(10L, "Old?", 1, SurveyQuestionType.FREE_TEXT);
        when(surveyQuestionRepository.findById(10L)).thenReturn(Optional.of(question));
        when(surveyQuestionRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        SurveyQuestionRequest req = new SurveyQuestionRequest();
        req.setText("New?");

        SurveyQuestionResponse result = surveyService.updateQuestion(10L, req);

        assertThat(result.getText()).isEqualTo("New?");
    }

    @Test
    void updateQuestion_throwsWhenNotFound() {
        when(surveyQuestionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> surveyService.updateQuestion(99L, new SurveyQuestionRequest()))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void deleteQuestion_deletesById() {
        when(surveyQuestionRepository.existsById(10L)).thenReturn(true);

        surveyService.deleteQuestion(10L);

        verify(surveyQuestionRepository).deleteById(10L);
    }

    @Test
    void deleteQuestion_throwsWhenNotFound() {
        when(surveyQuestionRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> surveyService.deleteQuestion(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void recordResponse_studentIdMismatch_throwsIllegalArgument() {
        SurveyResponseRequest req = new SurveyResponseRequest();
        req.setStudentId(1L);
        req.setSurveyQuestionId(18L);
        req.setLikertValue(4);

        assertThatThrownBy(() -> surveyService.recordResponse(2L, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Student ID mismatch");
    }

    @Test
    void recordResponse_surveyQuestionNotFound_throwsIllegalArgument() {
        SurveyResponseRequest req = new SurveyResponseRequest();
        req.setStudentId(1L);
        req.setSurveyQuestionId(18L);
        req.setLikertValue(4);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student(1L)));
        when(surveyQuestionRepository.findById(18L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> surveyService.recordResponse(1L, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Survey question not found");
    }

    @Test
    void recordResponse_bothValuesNull_throwsIllegalArgument() {
        SurveyResponseRequest req = new SurveyResponseRequest();
        req.setStudentId(1L);
        req.setSurveyQuestionId(18L);
        req.setLikertValue(null);
        req.setFreeText(null);

        SurveyQuestion question = questionWithLessonItem(18L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student(1L)));
        when(surveyQuestionRepository.findById(18L)).thenReturn(Optional.of(question));

        assertThatThrownBy(() -> surveyService.recordResponse(1L, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("At least one of likertValue or freeText must be provided");
    }

    @Test
    void recordResponse_profanity_throwsIllegalArgument() {
        SurveyResponseRequest req = new SurveyResponseRequest();
        req.setStudentId(1L);
        req.setSurveyQuestionId(18L);
        req.setFreeText("some clean text");

        SurveyQuestion question = questionWithLessonItem(18L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student(1L)));
        when(surveyQuestionRepository.findById(18L)).thenReturn(Optional.of(question));
        when(sanitizationService.sanitizeHtml(any())).thenReturn("some clean text");
        when(sanitizationService.containsProfanity(any())).thenReturn(true);

        assertThatThrownBy(() -> surveyService.recordResponse(1L, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Response contains content that is not allowed");
    }

    @Test
    void recordResponse_happyPath_savesResponse() {
        SurveyResponseRequest req = new SurveyResponseRequest();
        req.setStudentId(1L);
        req.setSurveyQuestionId(18L);
        req.setLikertValue(4);
        req.setSubmittedAt(OffsetDateTime.now());

        SurveyQuestion question = questionWithLessonItem(18L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student(1L)));
        when(surveyQuestionRepository.findById(18L)).thenReturn(Optional.of(question));

        surveyService.recordResponse(1L, req);

        verify(surveyResponseRepository).save(any(SurveyResponse.class));
    }

    private LessonItem surveyItem(Long id) {
        LessonItem item = new LessonItem();
        item.setId(id);
        item.setType(LessonItemType.SURVEY);
        return item;
    }

    private SurveyQuestion question(Long id, String text, int position, SurveyQuestionType type) {
        SurveyQuestion q = new SurveyQuestion();
        q.setId(id);
        q.setQuestionText(text);
        q.setPosition(position);
        q.setType(type);
        return q;
    }

    private Student student(Long id) {
        Student s = new Student();
        s.setId(id);
        return s;
    }

    private SurveyQuestion questionWithLessonItem(Long id) {
        Course course = new Course();
        course.setArchived(false);
        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        LessonItem lessonItem = new LessonItem();
        lessonItem.setId(90L);
        lessonItem.setType(LessonItemType.SURVEY);
        lessonItem.setLesson(lesson);
        SurveyQuestion q = new SurveyQuestion();
        q.setId(id);
        q.setLessonItem(lessonItem);
        return q;
    }
}
