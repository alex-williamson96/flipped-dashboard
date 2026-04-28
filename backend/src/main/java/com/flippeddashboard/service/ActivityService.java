package com.flippeddashboard.service;

import com.flippeddashboard.dto.*;
import com.flippeddashboard.exception.CourseArchivedException;
import com.flippeddashboard.model.*;
import com.flippeddashboard.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.OffsetDateTime;
import java.util.Objects;

@Service
public class ActivityService {

    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final LessonEventRepository lessonEventRepository;
    private final LessonItemRepository lessonItemRepository;
    private final VideoWatchEventRepository videoWatchEventRepository;
    private final QuizService quizService;
    private final SurveyService surveyService;

    public ActivityService(StudentRepository studentRepository,
                           LessonRepository lessonRepository,
                           LessonEventRepository lessonEventRepository,
                           LessonItemRepository lessonItemRepository,
                           VideoWatchEventRepository videoWatchEventRepository,
                           QuizService quizService,
                           SurveyService surveyService) {
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
        this.lessonEventRepository = lessonEventRepository;
        this.lessonItemRepository = lessonItemRepository;
        this.videoWatchEventRepository = videoWatchEventRepository;
        this.quizService = quizService;
        this.surveyService = surveyService;
    }

    @Transactional
    public void recordLessonStart(Long headerStudentId, LessonEventRequest request) {
        recordLessonEvent(headerStudentId, request, LessonEventType.STARTED);
    }

    @Transactional
    public void recordLessonComplete(Long headerStudentId, LessonEventRequest request) {
        recordLessonEvent(headerStudentId, request, LessonEventType.COMPLETED);
    }

    private void recordLessonEvent(Long headerStudentId, LessonEventRequest request, LessonEventType eventType) {
        if (!Objects.equals(headerStudentId, request.getStudentId())) {
            throw new IllegalArgumentException("Student ID mismatch");
        }
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found"));
        if (lesson.getCourse().isArchived()) {
            throw new CourseArchivedException("Course is archived");
        }

        if (lessonEventRepository.existsByStudentIdAndLessonIdAndEventType(
                student.getId(), lesson.getId(), eventType)) {
            return;
        }

        LessonEvent event = new LessonEvent();
        event.setStudent(student);
        event.setLesson(lesson);
        event.setEventType(eventType);
        event.setOccurredAt(request.getOccurredAt() != null ? request.getOccurredAt() : OffsetDateTime.now());
        lessonEventRepository.save(event);
    }

    @Transactional
    public void recordQuizResponse(Long headerStudentId, QuizResponseRequest request) {
        quizService.recordResponse(headerStudentId, request);
    }

    @Transactional
    public void recordSurveyResponse(Long headerStudentId, SurveyResponseRequest request) {
        surveyService.recordResponse(headerStudentId, request);
    }

    @Transactional
    public void recordVideoWatched(Long headerStudentId, VideoWatchRequest request) {
        if (!Objects.equals(headerStudentId, request.getStudentId())) {
            throw new IllegalArgumentException("Student ID mismatch");
        }
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        LessonItem lessonItem = lessonItemRepository.findById(request.getLessonItemId())
                .filter(i -> i.getType() == LessonItemType.VIDEO)
                .orElseThrow(() -> new IllegalArgumentException("Lesson item not found or is not of type VIDEO"));
        if (lessonItem.getLesson().getCourse().isArchived()) {
            throw new CourseArchivedException("Course is archived");
        }
        if (request.getWatchPercent() < 0 || request.getWatchPercent() > 100) {
            throw new IllegalArgumentException("watchPercent must be between 0 and 100");
        }

        if (videoWatchEventRepository.existsByStudentIdAndLessonItem_Id(student.getId(), lessonItem.getId())) {
            return;
        }

        VideoWatchEvent event = new VideoWatchEvent();
        event.setStudent(student);
        event.setLessonItem(lessonItem);
        event.setWatchPercent(request.getWatchPercent());
        event.setMarkedAt(request.getMarkedAt() != null ? request.getMarkedAt() : OffsetDateTime.now());
        videoWatchEventRepository.save(event);
    }
}
