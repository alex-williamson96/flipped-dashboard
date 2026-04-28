package com.flippeddashboard.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flippeddashboard.dto.*;
import com.flippeddashboard.model.*;
import com.flippeddashboard.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;
    private final LessonItemRepository lessonItemRepository;
    private final LessonEventRepository lessonEventRepository;
    private final QuizService quizService;
    private final SurveyService surveyService;

    public StudentService(StudentRepository studentRepository,
                          CourseRepository courseRepository,
                          LessonRepository lessonRepository,
                          LessonItemRepository lessonItemRepository,
                          LessonEventRepository lessonEventRepository,
                          QuizService quizService,
                          SurveyService surveyService) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
        this.lessonRepository = lessonRepository;
        this.lessonItemRepository = lessonItemRepository;
        this.lessonEventRepository = lessonEventRepository;
        this.quizService = quizService;
        this.surveyService = surveyService;
    }

    @Transactional(readOnly = true)
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(s -> new StudentDto(s.getId(), s.getName(), s.getSection().getId()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<StudentCourseDto> getCoursesForStudent(Long headerStudentId, Long pathStudentId, String archived) {
        if (!java.util.Objects.equals(headerStudentId, pathStudentId)) {
            throw new IllegalArgumentException("Student ID mismatch");
        }
        Student student = studentRepository.findById(pathStudentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        Section section = student.getSection();
        Course course = section.getCourse();
        if ("false".equals(archived) && course.isArchived()) return List.of();
        if ("true".equals(archived) && !course.isArchived()) return List.of();
        return List.of(new StudentCourseDto(course.getId(), course.getName(), section.getId(), section.getName(), course.isArchived()));
    }

    @Transactional(readOnly = true)
    public List<LessonSummaryDto> getLessonsForStudentCourse(Long headerStudentId, Long pathStudentId, Long courseId) {
        if (!java.util.Objects.equals(headerStudentId, pathStudentId)) {
            throw new IllegalArgumentException("Student ID mismatch");
        }
        Student student = studentRepository.findById(pathStudentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));

        if (!student.getSection().getCourse().getId().equals(courseId)) {
            throw new EntityNotFoundException("Course not found");
        }

        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByPositionAsc(courseId);
        return lessons.stream().map(lesson -> {
            boolean completed = lessonEventRepository.existsByStudentIdAndLessonIdAndEventType(
                    pathStudentId, lesson.getId(), LessonEventType.COMPLETED);
            return new LessonSummaryDto(lesson.getId(), lesson.getTitle(), lesson.getPosition(), completed);
        }).toList();
    }

    @Transactional(readOnly = true)
    public LessonContentDto getLessonContent(Long headerStudentId, Long lessonId) {
        if (headerStudentId != null) {
            studentRepository.findById(headerStudentId)
                    .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        }

        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));

        List<LessonItem> items = lessonItemRepository.findByLessonIdOrderByPositionAsc(lessonId);
        List<LessonItemDto> itemDtos = items.stream().map(item -> toItemDto(item, headerStudentId)).toList();
        return new LessonContentDto(lesson.getId(), lesson.getTitle(), itemDtos);
    }

    private LessonItemDto toItemDto(LessonItem item, Long studentId) {
        LessonItemDto dto = new LessonItemDto();
        dto.setLessonItemId(item.getId());
        dto.setPosition(item.getPosition());
        dto.setType(item.getType().toString());

        switch (item.getType()) {
            case VIDEO -> dto.setVideoUrl(extractVideoUrl(item.getContent()));
            case TEXT -> dto.setBody(extractTextBody(item.getContent()));
            case QUIZ -> {
                dto.setQuestions(quizService.getStudentQuestions(item.getId()));
                if (studentId != null) {
                    dto.setCompleted(quizService.isQuizCompletedBy(studentId, item.getId()));
                }
            }
            case SURVEY -> {
                dto.setQuestions(surveyService.getStudentQuestions(item.getId()));
                if (studentId != null) {
                    dto.setCompleted(surveyService.isSurveyCompletedBy(studentId, item.getId()));
                }
            }
        }
        return dto;
    }

    private String extractVideoUrl(String content) {
        if (content == null) return null;
        try {
            JsonNode node = MAPPER.readTree(content);
            if (node.has("videoUrl")) return node.get("videoUrl").asText();
            if (node.has("url")) return node.get("url").asText();
        } catch (Exception e) {
            log.warn("Failed to parse video lesson item content as JSON: {}", e.getMessage());
        }
        return null;
    }

    private String extractTextBody(String content) {
        if (content == null) return null;
        try {
            JsonNode node = MAPPER.readTree(content);
            if (node.has("body")) return node.get("body").asText();
        } catch (Exception e) {
            log.warn("Failed to parse text lesson item content as JSON: {}", e.getMessage());
        }
        return null;
    }
}
