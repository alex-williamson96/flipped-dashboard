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
public class QuizService {

    private final StudentRepository studentRepository;
    private final LessonItemRepository lessonItemRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizChoiceRepository quizChoiceRepository;
    private final QuizResponseRepository quizResponseRepository;

    public QuizService(StudentRepository studentRepository,
                       LessonItemRepository lessonItemRepository,
                       QuizQuestionRepository quizQuestionRepository,
                       QuizChoiceRepository quizChoiceRepository,
                       QuizResponseRepository quizResponseRepository) {
        this.studentRepository = studentRepository;
        this.lessonItemRepository = lessonItemRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizChoiceRepository = quizChoiceRepository;
        this.quizResponseRepository = quizResponseRepository;
    }

    @Transactional
    public QuizQuestionResponse addQuestion(Long itemId, QuizQuestionRequest req) {
        LessonItem item = lessonItemRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("LessonItem not found: " + itemId));
        if (item.getType() != LessonItemType.QUIZ) {
            throw new IllegalArgumentException("LessonItem " + itemId + " is not a QUIZ item");
        }
        if (req.getText() == null || req.getText().isBlank()) {
            throw new IllegalArgumentException("text is required");
        }
        QuizQuestion question = new QuizQuestion();
        question.setLessonItem(item);
        question.setQuestionText(req.getText());
        if (req.getPosition() != null) question.setPosition(req.getPosition());
        if (req.getLearningObjective() != null) question.setLearningObjective(req.getLearningObjective());
        return toQuestionResponse(quizQuestionRepository.save(question));
    }

    @Transactional
    public QuizQuestionResponse updateQuestion(Long id, QuizQuestionRequest req) {
        QuizQuestion question = quizQuestionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("QuizQuestion not found: " + id));
        if (req.getText() != null) question.setQuestionText(req.getText());
        if (req.getPosition() != null) question.setPosition(req.getPosition());
        if (req.getLearningObjective() != null)
            question.setLearningObjective(req.getLearningObjective().isBlank() ? null : req.getLearningObjective());
        return toQuestionResponse(quizQuestionRepository.save(question));
    }

    @Transactional
    public void deleteQuestion(Long id) {
        if (!quizQuestionRepository.existsById(id)) {
            throw new EntityNotFoundException("QuizQuestion not found: " + id);
        }
        quizQuestionRepository.deleteById(id);
    }

    @Transactional
    public QuizChoiceResponse addChoice(Long questionId, QuizChoiceRequest req) {
        QuizQuestion question = quizQuestionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("QuizQuestion not found: " + questionId));
        if (req.getText() == null || req.getText().isBlank()) {
            throw new IllegalArgumentException("text is required");
        }
        QuizChoice choice = new QuizChoice();
        choice.setQuestion(question);
        choice.setChoiceText(req.getText());
        if (req.getIsCorrect() != null) choice.setCorrect(req.getIsCorrect());
        return toChoiceResponse(quizChoiceRepository.save(choice));
    }

    @Transactional
    public QuizChoiceResponse updateChoice(Long id, QuizChoiceRequest req) {
        QuizChoice choice = quizChoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("QuizChoice not found: " + id));
        if (req.getText() != null) choice.setChoiceText(req.getText());
        if (req.getIsCorrect() != null) choice.setCorrect(req.getIsCorrect());
        return toChoiceResponse(quizChoiceRepository.save(choice));
    }

    @Transactional
    public void deleteChoice(Long id) {
        if (!quizChoiceRepository.existsById(id)) {
            throw new EntityNotFoundException("QuizChoice not found: " + id);
        }
        quizChoiceRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<StudentQuizQuestionDto> getStudentQuestions(Long lessonItemId) {
        return quizQuestionRepository.findByLessonItemIdOrderByPositionAsc(lessonItemId).stream()
                .map(q -> {
                    List<StudentQuizChoiceDto> choices = quizChoiceRepository.findByQuestionId(q.getId()).stream()
                            .map(c -> new StudentQuizChoiceDto(c.getId(), c.getChoiceText()))
                            .toList();
                    return new StudentQuizQuestionDto(q.getId(), q.getQuestionText(), choices);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean isQuizCompletedBy(Long studentId, Long lessonItemId) {
        return quizResponseRepository.existsByStudentIdAndQuestion_LessonItem_Id(studentId, lessonItemId);
    }

    @Transactional
    public void recordResponse(Long headerStudentId, QuizResponseRequest request) {
        if (!Objects.equals(headerStudentId, request.getStudentId())) {
            throw new IllegalArgumentException("Student ID mismatch");
        }
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        QuizQuestion question = quizQuestionRepository.findById(request.getQuizQuestionId())
                .orElseThrow(() -> new IllegalArgumentException("Quiz question not found"));
        QuizChoice choice = quizChoiceRepository.findById(request.getQuizChoiceId())
                .filter(c -> c.getQuestion().getId().equals(question.getId()))
                .orElseThrow(() -> new IllegalArgumentException("Quiz choice not found or does not belong to that question"));
        if (question.getLessonItem().getLesson().getCourse().isArchived()) {
            throw new CourseArchivedException("Course is archived");
        }

        if (quizResponseRepository.existsByStudentIdAndQuestion_Id(student.getId(), question.getId())) {
            return;
        }

        QuizResponse response = new QuizResponse();
        response.setStudent(student);
        response.setQuestion(question);
        response.setChoice(choice);
        response.setSubmittedAt(request.getSubmittedAt() != null ? request.getSubmittedAt() : OffsetDateTime.now());
        quizResponseRepository.save(response);
    }

    @Transactional
    public void cloneQuestions(Long sourceLessonItemId, LessonItem newLessonItem) {
        for (QuizQuestion source : quizQuestionRepository.findByLessonItemIdOrderByPositionAsc(sourceLessonItemId)) {
            QuizQuestion copy = new QuizQuestion();
            copy.setLessonItem(newLessonItem);
            copy.setQuestionText(source.getQuestionText());
            copy.setPosition(source.getPosition());
            copy.setLearningObjective(source.getLearningObjective());
            copy = quizQuestionRepository.save(copy);

            for (QuizChoice sourceChoice : quizChoiceRepository.findByQuestionId(source.getId())) {
                QuizChoice choiceCopy = new QuizChoice();
                choiceCopy.setQuestion(copy);
                choiceCopy.setChoiceText(sourceChoice.getChoiceText());
                choiceCopy.setCorrect(sourceChoice.isCorrect());
                quizChoiceRepository.save(choiceCopy);
            }
        }
    }

    private QuizQuestionResponse toQuestionResponse(QuizQuestion q) {
        QuizQuestionResponse resp = new QuizQuestionResponse();
        resp.setId(q.getId());
        resp.setText(q.getQuestionText());
        resp.setPosition(q.getPosition());
        resp.setLearningObjective(q.getLearningObjective());
        return resp;
    }

    private QuizChoiceResponse toChoiceResponse(QuizChoice c) {
        QuizChoiceResponse resp = new QuizChoiceResponse();
        resp.setId(c.getId());
        resp.setText(c.getChoiceText());
        resp.setIsCorrect(c.isCorrect());
        return resp;
    }
}
