package com.flippeddashboard.service;

import com.flippeddashboard.dto.*;
import com.flippeddashboard.exception.CourseArchivedException;
import com.flippeddashboard.model.*;
import com.flippeddashboard.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class SurveyService {

    private final StudentRepository studentRepository;
    private final LessonItemRepository lessonItemRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final SanitizationService sanitizationService;

    public SurveyService(StudentRepository studentRepository,
                         LessonItemRepository lessonItemRepository,
                         SurveyQuestionRepository surveyQuestionRepository,
                         SurveyResponseRepository surveyResponseRepository,
                         SanitizationService sanitizationService) {
        this.studentRepository = studentRepository;
        this.lessonItemRepository = lessonItemRepository;
        this.surveyQuestionRepository = surveyQuestionRepository;
        this.surveyResponseRepository = surveyResponseRepository;
        this.sanitizationService = sanitizationService;
    }

    @Transactional
    public SurveyQuestionResponse addQuestion(Long itemId, SurveyQuestionRequest req) {
        LessonItem item = lessonItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("LessonItem not found: " + itemId));
        if (item.getType() != LessonItemType.SURVEY) {
            throw new IllegalArgumentException("LessonItem " + itemId + " is not a SURVEY item");
        }
        if (req.getText() == null || req.getText().isBlank()) {
            throw new IllegalArgumentException("text is required");
        }
        SurveyQuestion question = new SurveyQuestion();
        question.setLessonItem(item);
        question.setQuestionText(req.getText());
        if (req.getPosition() != null) question.setPosition(req.getPosition());
        if (req.getQuestionType() != null) {
            question.setType(SurveyQuestionType.valueOf(req.getQuestionType()));
        }
        return toResponse(surveyQuestionRepository.save(question));
    }

    @Transactional
    public SurveyQuestionResponse updateQuestion(Long id, SurveyQuestionRequest req) {
        SurveyQuestion question = surveyQuestionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SurveyQuestion not found: " + id));
        if (req.getText() != null) question.setQuestionText(req.getText());
        if (req.getPosition() != null) question.setPosition(req.getPosition());
        if (req.getQuestionType() != null) {
            question.setType(SurveyQuestionType.valueOf(req.getQuestionType()));
        }
        return toResponse(surveyQuestionRepository.save(question));
    }

    @Transactional
    public void deleteQuestion(Long id) {
        if (!surveyQuestionRepository.existsById(id)) {
            throw new EntityNotFoundException("SurveyQuestion not found: " + id);
        }
        surveyQuestionRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<StudentSurveyQuestionDto> getStudentQuestions(Long lessonItemId) {
        return surveyQuestionRepository.findByLessonItemIdOrderByPositionAsc(lessonItemId).stream()
                .map(q -> new StudentSurveyQuestionDto(q.getId(), q.getQuestionText(), q.getType().toString()))
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean isSurveyCompletedBy(Long studentId, Long lessonItemId) {
        return surveyResponseRepository.existsByStudentIdAndQuestion_LessonItem_Id(studentId, lessonItemId);
    }

    @Transactional
    public void recordResponse(Long headerStudentId, SurveyResponseRequest request) {
        if (!Objects.equals(headerStudentId, request.getStudentId())) {
            throw new IllegalArgumentException("Student ID mismatch");
        }
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        SurveyQuestion question = surveyQuestionRepository.findById(request.getSurveyQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Survey question not found"));
        if (question.getLessonItem().getLesson().getCourse().isArchived()) {
            throw new CourseArchivedException("Course is archived");
        }
        if (request.getFreeText() != null) {
            String sanitized = sanitizationService.sanitizeHtml(request.getFreeText());
            if (sanitizationService.containsProfanity(sanitized)) {
                throw new IllegalArgumentException("Response contains content that is not allowed");
            }
            request.setFreeText(sanitized);
        }
        if (request.getLikertValue() == null && request.getFreeText() == null) {
            throw new IllegalArgumentException("At least one of likertValue or freeText must be provided");
        }

        if (surveyResponseRepository.existsByStudentIdAndQuestion_Id(student.getId(), question.getId())) {
            return;
        }

        SurveyResponse response = new SurveyResponse();
        response.setStudent(student);
        response.setQuestion(question);
        response.setLikertValue(request.getLikertValue());
        response.setFreeText(request.getFreeText());
        response.setSubmittedAt(request.getSubmittedAt() != null ? request.getSubmittedAt() : OffsetDateTime.now());
        surveyResponseRepository.save(response);
    }

    @Transactional
    public void cloneQuestions(Long sourceLessonItemId, LessonItem newLessonItem) {
        for (SurveyQuestion source : surveyQuestionRepository.findByLessonItemIdOrderByPositionAsc(sourceLessonItemId)) {
            SurveyQuestion copy = new SurveyQuestion();
            copy.setLessonItem(newLessonItem);
            copy.setQuestionText(source.getQuestionText());
            copy.setType(source.getType());
            copy.setPosition(source.getPosition());
            surveyQuestionRepository.save(copy);
        }
    }

    private SurveyQuestionResponse toResponse(SurveyQuestion q) {
        SurveyQuestionResponse resp = new SurveyQuestionResponse();
        resp.setId(q.getId());
        resp.setText(q.getQuestionText());
        resp.setPosition(q.getPosition());
        resp.setQuestionType(q.getType() != null ? q.getType().name() : null);
        return resp;
    }
}
