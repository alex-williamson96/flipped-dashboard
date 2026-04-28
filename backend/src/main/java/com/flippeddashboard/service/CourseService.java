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
public class CourseService {

    private final CourseRepository courseRepository;
    private final SectionRepository sectionRepository;
    private final LessonRepository lessonRepository;
    private final LessonItemRepository lessonItemRepository;
    private final QuizService quizService;
    private final SurveyService surveyService;

    public CourseService(CourseRepository courseRepository,
                         SectionRepository sectionRepository,
                         LessonRepository lessonRepository,
                         LessonItemRepository lessonItemRepository,
                         QuizService quizService,
                         SurveyService surveyService) {
        this.courseRepository = courseRepository;
        this.sectionRepository = sectionRepository;
        this.lessonRepository = lessonRepository;
        this.lessonItemRepository = lessonItemRepository;
        this.quizService = quizService;
        this.surveyService = surveyService;
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> listCourses(String archived) {
        List<Course> courses;
        if ("true".equals(archived)) {
            courses = courseRepository.findByIsArchivedTrue();
        } else if ("all".equals(archived)) {
            courses = courseRepository.findAll();
        } else {
            courses = courseRepository.findByIsArchivedFalse();
        }
        Map<Long, List<SectionResponse>> sectionsByCourse = sectionRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        s -> s.getCourse().getId(),
                        Collectors.mapping(s -> {
                            SectionResponse sr = new SectionResponse();
                            sr.setId(s.getId());
                            sr.setTitle(s.getName());
                            return sr;
                        }, Collectors.toList())
                ));
        return courses.stream().map(c -> {
            CourseResponse resp = toSimpleResponse(c);
            resp.setSections(sectionsByCourse.getOrDefault(c.getId(), Collections.emptyList()));
            return resp;
        }).collect(Collectors.toList());
    }

    @Transactional
    public CourseResponse createCourse(CourseRequest req) {
        if (req.getTitle() == null || req.getTitle().isBlank()) {
            throw new IllegalArgumentException("title is required");
        }
        Course course = new Course();
        course.setName(req.getTitle());
        course.setDescription(req.getDescription());
        if (req.getTerm() != null) course.setTerm(req.getTerm());
        return toSimpleResponse(courseRepository.save(course));
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseWithSections(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + id));

        List<Section> sections = sectionRepository.findByCourseId(id);

        List<SectionResponse> sectionResponses = sections.stream().map(s -> {
            SectionResponse sr = new SectionResponse();
            sr.setId(s.getId());
            sr.setTitle(s.getName());
            sr.setLessons(Collections.emptyList());
            return sr;
        }).collect(Collectors.toList());

        CourseResponse resp = toSimpleResponse(course);
        resp.setSections(sectionResponses);

        return resp;
    }

    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest req) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + id));
        if (req.getTitle() != null) course.setName(req.getTitle());
        if (req.getDescription() != null) course.setDescription(req.getDescription());
        if (req.getTerm() != null) course.setTerm(req.getTerm());
        if (req.getIsArchived() != null) course.setArchived(req.getIsArchived());
        return toSimpleResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse patchCourse(Long id, CourseRequest req) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + id));
        if (req.getTerm() != null) course.setTerm(req.getTerm());
        if (req.getIsArchived() != null) course.setArchived(req.getIsArchived());
        return toSimpleResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse cloneCourse(Long id, CourseCloneRequest req) {
        Course source = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Course not found: " + id));

        Course newCourse = new Course();
        newCourse.setName(source.getName());
        newCourse.setDescription(source.getDescription());
        newCourse.setTerm(req.getTerm());
        newCourse.setArchived(false);
        newCourse = courseRepository.save(newCourse);

        for (Lesson sourceLesson : lessonRepository.findByCourseIdOrderByPositionAsc(id)) {
            Lesson newLesson = new Lesson();
            newLesson.setCourse(newCourse);
            newLesson.setTitle(sourceLesson.getTitle());
            newLesson.setPosition(sourceLesson.getPosition());
            newLesson.setActive(false);
            newLesson = lessonRepository.save(newLesson);

            for (LessonItem sourceItem : lessonItemRepository.findByLessonIdOrderByPositionAsc(sourceLesson.getId())) {
                LessonItem newItem = new LessonItem();
                newItem.setLesson(newLesson);
                newItem.setType(sourceItem.getType());
                newItem.setContent(sourceItem.getContent());
                newItem.setPosition(sourceItem.getPosition());
                newItem = lessonItemRepository.save(newItem);

                switch (sourceItem.getType()) {
                    case QUIZ -> quizService.cloneQuestions(sourceItem.getId(), newItem);
                    case SURVEY -> surveyService.cloneQuestions(sourceItem.getId(), newItem);
                    default -> { }
                }
            }
        }

        CourseResponse resp = toSimpleResponse(newCourse);
        resp.setSections(Collections.emptyList());
        return resp;
    }

    @Transactional
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new EntityNotFoundException("Course not found: " + id);
        }
        courseRepository.deleteById(id);
    }

    private CourseResponse toSimpleResponse(Course course) {
        CourseResponse resp = new CourseResponse();
        resp.setId(course.getId());
        resp.setTitle(course.getName());
        resp.setDescription(course.getDescription());
        resp.setTerm(course.getTerm());
        resp.setArchived(course.isArchived());
        return resp;
    }
}
