package com.flippeddashboard.service;

import com.flippeddashboard.dto.*;
import com.flippeddashboard.model.*;
import com.flippeddashboard.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonItemRepository lessonItemRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final QuizChoiceRepository quizChoiceRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;

    public LessonService(LessonRepository lessonRepository,
                         CourseRepository courseRepository,
                         LessonItemRepository lessonItemRepository,
                         QuizQuestionRepository quizQuestionRepository,
                         QuizChoiceRepository quizChoiceRepository,
                         SurveyQuestionRepository surveyQuestionRepository) {
        this.lessonRepository = lessonRepository;
        this.courseRepository = courseRepository;
        this.lessonItemRepository = lessonItemRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.quizChoiceRepository = quizChoiceRepository;
        this.surveyQuestionRepository = surveyQuestionRepository;
    }

    @Transactional(readOnly = true)
    public List<LessonResponse> getLessonsByCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException("Course not found: " + courseId);
        }
        return lessonRepository.findByCourseIdOrderByPositionAsc(courseId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public LessonResponse createLesson(Long courseId, LessonRequest req) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + courseId));
        if (req.getTitle() == null || req.getTitle().isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setTitle(req.getTitle());
        if (req.getPosition() != null) lesson.setPosition(req.getPosition());
        if (req.getIsActive() != null) lesson.setActive(req.getIsActive());
        return toResponse(lessonRepository.save(lesson));
    }

    @Transactional(readOnly = true)
    public LessonDetailResponse getLessonDetail(Long id) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + id));
        List<LessonItem> items = lessonItemRepository.findByLessonIdOrderByPositionAsc(id);
        return toDetailResponse(lesson, items);
    }

    @Transactional
    public LessonResponse updateLesson(Long id, LessonRequest req) {
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Lesson not found: " + id));
        if (req.getTitle() != null) lesson.setTitle(req.getTitle());
        if (req.getPosition() != null) lesson.setPosition(req.getPosition());
        if (req.getIsActive() != null) lesson.setActive(req.getIsActive());
        return toResponse(lessonRepository.save(lesson));
    }

    @Transactional
    public List<ItemPositionResponse> reorderLessons(Long courseId, ReorderRequest req) {
        if (!courseRepository.existsById(courseId)) {
            throw new EntityNotFoundException("Course not found: " + courseId);
        }
        List<Long> lessonIds = req.getItemIds();
        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByPositionAsc(courseId);
        Set<Long> belongToCourse = lessons.stream().map(Lesson::getId).collect(Collectors.toSet());

        for (Long lessonId : lessonIds) {
            if (!belongToCourse.contains(lessonId)) {
                throw new IllegalArgumentException("Lesson " + lessonId + " does not belong to course " + courseId);
            }
        }

        Map<Long, Lesson> lessonMap = lessons.stream()
                .collect(Collectors.toMap(Lesson::getId, l -> l));

        List<ItemPositionResponse> result = new ArrayList<>();
        for (int i = 0; i < lessonIds.size(); i++) {
            Lesson lesson = lessonMap.get(lessonIds.get(i));
            lesson.setPosition(i + 1);
            lessonRepository.save(lesson);
            ItemPositionResponse pr = new ItemPositionResponse();
            pr.setId(lesson.getId());
            pr.setPosition(lesson.getPosition());
            result.add(pr);
        }
        return result;
    }

    @Transactional
    public void deleteLesson(Long id) {
        if (!lessonRepository.existsById(id)) {
            throw new EntityNotFoundException("Lesson not found: " + id);
        }
        lessonRepository.deleteById(id);
    }

    private LessonResponse toResponse(Lesson lesson) {
        LessonResponse resp = new LessonResponse();
        resp.setId(lesson.getId());
        resp.setTitle(lesson.getTitle());
        resp.setPosition(lesson.getPosition());
        resp.setIsActive(lesson.isActive());
        return resp;
    }

    private LessonDetailResponse toDetailResponse(Lesson lesson, List<LessonItem> items) {
        LessonDetailResponse resp = new LessonDetailResponse();
        resp.setId(lesson.getId());
        resp.setTitle(lesson.getTitle());
        resp.setPosition(lesson.getPosition());
        resp.setIsActive(lesson.isActive());
        resp.setCourseId(lesson.getCourse().getId());

        List<Long> quizItemIds = items.stream()
                .filter(i -> i.getType() == LessonItemType.QUIZ)
                .map(LessonItem::getId)
                .toList();
        List<Long> surveyItemIds = items.stream()
                .filter(i -> i.getType() == LessonItemType.SURVEY)
                .map(LessonItem::getId)
                .toList();

        Map<Long, List<QuizQuestion>> quizQuestionsByItem = quizItemIds.isEmpty()
                ? Map.of()
                : quizQuestionRepository.findByLessonItemIdInOrderByPositionAsc(quizItemIds).stream()
                        .collect(Collectors.groupingBy(q -> q.getLessonItem().getId()));
        Map<Long, List<SurveyQuestion>> surveyQuestionsByItem = surveyItemIds.isEmpty()
                ? Map.of()
                : surveyQuestionRepository.findByLessonItemIdInOrderByPositionAsc(surveyItemIds).stream()
                        .collect(Collectors.groupingBy(q -> q.getLessonItem().getId()));

        List<Long> allQuestionIds = quizQuestionsByItem.values().stream()
                .flatMap(List::stream)
                .map(QuizQuestion::getId)
                .toList();
        Map<Long, List<QuizChoice>> choicesByQuestion = allQuestionIds.isEmpty()
                ? Map.of()
                : quizChoiceRepository.findByQuestionIdIn(allQuestionIds).stream()
                        .collect(Collectors.groupingBy(c -> c.getQuestion().getId()));

        resp.setItems(items.stream()
                .map(item -> toItemResponse(item, quizQuestionsByItem, surveyQuestionsByItem, choicesByQuestion))
                .collect(Collectors.toList()));
        return resp;
    }

    private LessonItemResponse toItemResponse(LessonItem item,
                                              Map<Long, List<QuizQuestion>> quizQuestionsByItem,
                                              Map<Long, List<SurveyQuestion>> surveyQuestionsByItem,
                                              Map<Long, List<QuizChoice>> choicesByQuestion) {
        LessonItemResponse resp = new LessonItemResponse();
        resp.setId(item.getId());
        resp.setType(item.getType().name());
        resp.setPosition(item.getPosition());

        Map<String, Object> content = ContentSerializer.parse(item.getContent());

        switch (item.getType()) {
            case VIDEO -> {
                resp.setTitle((String) content.get("title"));
                String url = content.containsKey("videoUrl")
                        ? (String) content.get("videoUrl")
                        : (String) content.get("url");
                resp.setVideoUrl(url);
            }
            case TEXT -> {
                resp.setTitle((String) content.get("title"));
                resp.setBody((String) content.get("body"));
            }
            case QUIZ -> {
                resp.setTitle((String) content.get("title"));
                List<QuizQuestion> questions = quizQuestionsByItem.getOrDefault(item.getId(), List.of());
                resp.setQuestions(questions.stream().map(q -> {
                    QuizQuestionResponse qr = new QuizQuestionResponse();
                    qr.setId(q.getId());
                    qr.setText(q.getQuestionText());
                    qr.setPosition(q.getPosition());
                    List<QuizChoice> choices = choicesByQuestion.getOrDefault(q.getId(), List.of());
                    // isCorrect intentionally omitted: this endpoint has no auth, so the answer
                    // key would be visible to any student hitting GET /lessons/{id} directly.
                    qr.setChoices(choices.stream().map(c -> {
                        QuizChoiceResponse cr = new QuizChoiceResponse();
                        cr.setId(c.getId());
                        cr.setText(c.getChoiceText());
                        return cr;
                    }).collect(Collectors.toList()));
                    return qr;
                }).collect(Collectors.toList()));
            }
            case SURVEY -> {
                resp.setTitle((String) content.get("title"));
                List<SurveyQuestion> questions = surveyQuestionsByItem.getOrDefault(item.getId(), List.of());
                resp.setQuestions(questions.stream().map(q -> {
                    SurveyQuestionResponse sr = new SurveyQuestionResponse();
                    sr.setId(q.getId());
                    sr.setText(q.getQuestionText());
                    sr.setPosition(q.getPosition());
                    sr.setQuestionType(q.getType() != null ? q.getType().name() : null);
                    return sr;
                }).collect(Collectors.toList()));
            }
        }

        return resp;
    }

}
