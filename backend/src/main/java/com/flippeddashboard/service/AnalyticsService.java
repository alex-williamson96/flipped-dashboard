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
public class AnalyticsService {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;
    private final LessonRepository lessonRepository;
    private final LessonItemRepository lessonItemRepository;
    private final LessonEventRepository lessonEventRepository;
    private final QuizResponseRepository quizResponseRepository;
    private final SurveyResponseRepository surveyResponseRepository;
    private final VideoWatchEventRepository videoWatchEventRepository;
    private final AnalyticsQuestionCache questionCache;

    public AnalyticsService(CourseRepository courseRepository,
                             StudentRepository studentRepository,
                             LessonRepository lessonRepository,
                             LessonItemRepository lessonItemRepository,
                             LessonEventRepository lessonEventRepository,
                             QuizResponseRepository quizResponseRepository,
                             SurveyResponseRepository surveyResponseRepository,
                             VideoWatchEventRepository videoWatchEventRepository,
                             AnalyticsQuestionCache questionCache) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
        this.lessonItemRepository = lessonItemRepository;
        this.lessonEventRepository = lessonEventRepository;
        this.quizResponseRepository = quizResponseRepository;
        this.surveyResponseRepository = surveyResponseRepository;
        this.videoWatchEventRepository = videoWatchEventRepository;
        this.questionCache = questionCache;
    }

    @Transactional(readOnly = true)
    public ClassOverviewResponse getClassOverview(Long courseId, Long sectionId, Long lessonId,
                                                   String completionStatus, Integer minQuizScore,
                                                   Integer maxQuizScore, Integer confidenceLevel) {
        if (courseId == null) throw new IllegalArgumentException("courseId is required");
        validateCompletionStatus(completionStatus);
        if (!courseRepository.existsById(courseId)) throw new EntityNotFoundException("Course not found");

        List<Student> students = resolveStudents(courseId, sectionId);
        List<Long> studentIds = students.stream().map(Student::getId).toList();

        List<Lesson> courseLessons = lessonRepository.findByCourseIdOrderByPositionAsc(courseId);
        List<Long> courseLessonIds = courseLessons.stream().map(Lesson::getId).toList();
        List<LessonItem> allLessonItems = lessonItemRepository.findByLessonIdInOrderByPositionAsc(courseLessonIds);

        List<LessonEvent> completionEvents = lessonEventRepository.findByStudentIdInAndEventType(studentIds, LessonEventType.COMPLETED);
        List<LessonEvent> startedEvents = lessonEventRepository.findByStudentIdInAndEventType(studentIds, LessonEventType.STARTED);
        List<QuizResponse> quizResponses = quizResponseRepository.findByStudentIdIn(studentIds);
        List<SurveyResponse> surveyResponses = surveyResponseRepository.findByStudentIdIn(studentIds);

        List<LessonItem> scopedItems;
        Set<Long> completedStudentIds;
        Set<Long> scopedLessonIds;

        if (lessonId != null) {
            Lesson lesson = lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
            scopedItems = allLessonItems.stream()
                    .filter(i -> i.getLesson().getId().equals(lesson.getId()))
                    .toList();
            completedStudentIds = completionEvents.stream()
                    .filter(e -> e.getLesson().getId().equals(lessonId))
                    .map(e -> e.getStudent().getId())
                    .collect(Collectors.toSet());
            scopedLessonIds = Set.of(lessonId);
        } else {
            scopedItems = allLessonItems;
            Set<Long> lessonIdSet = new HashSet<>(courseLessonIds);
            Map<Long, Set<Long>> completedByStudent = completionEvents.stream()
                    .filter(e -> lessonIdSet.contains(e.getLesson().getId()))
                    .collect(Collectors.groupingBy(
                            e -> e.getStudent().getId(),
                            Collectors.mapping(e -> e.getLesson().getId(), Collectors.toSet())));
            completedStudentIds = completedByStudent.entrySet().stream()
                    .filter(entry -> entry.getValue().containsAll(courseLessonIds))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toSet());
            scopedLessonIds = lessonIdSet;
        }

        AnalyticsQuestionCache.Snapshot cache = questionCache.forItems(allLessonItems);

        List<StudentOverviewRow> rows = new ArrayList<>();
        for (Student student : students) {
            Long sid = student.getId();
            boolean completed = completedStudentIds.contains(sid);
            Integer quizScore = quizScoreForStudent(sid, scopedItems, quizResponses, cache);
            Integer conf = confidenceForStudent(sid, scopedItems, surveyResponses, cache);
            Double totalGrade = totalGradeForStudent(sid, courseLessons, allLessonItems, quizResponses, cache);

            if (!passesCompletionFilter(completionStatus, completed)) continue;
            if (minQuizScore != null && (quizScore == null || quizScore < minQuizScore)) continue;
            if (maxQuizScore != null && (quizScore == null || quizScore > maxQuizScore)) continue;
            if (confidenceLevel != null && !confidenceLevel.equals(conf)) continue;

            StudentOverviewRow row = new StudentOverviewRow();
            row.setStudentId(sid);
            row.setName(student.getName());
            row.setSectionName(student.getSection().getName());
            row.setCompleted(completed);
            row.setQuizScore(quizScore);
            row.setConfidence(conf);
            row.setTotalGrade(totalGrade);
            rows.add(row);
        }

        int completedCount = (int) rows.stream().filter(StudentOverviewRow::isCompleted).count();
        double completionRate = rows.isEmpty() ? 0.0 : (double) completedCount / rows.size();
        OptionalDouble avgQuizOpt = rows.stream()
                .filter(r -> r.getQuizScore() != null)
                .mapToDouble(StudentOverviewRow::getQuizScore)
                .average();
        OptionalDouble avgConfOpt = rows.stream()
                .filter(r -> r.getConfidence() != null)
                .mapToDouble(StudentOverviewRow::getConfidence)
                .average();

        ClassOverviewResponse resp = new ClassOverviewResponse();
        resp.setTotalStudents(rows.size());
        resp.setEnrolledCount(students.size());
        resp.setCompletedCount(completedStudentIds.size());
        resp.setCompletionRate(completionRate);
        resp.setAvgQuizScore(avgQuizOpt.isPresent() ? avgQuizOpt.getAsDouble() : null);
        resp.setAvgConfidence(avgConfOpt.isPresent() ? avgConfOpt.getAsDouble() : null);
        resp.setStudents(rows);

        List<Long> filteredIds = rows.stream().map(StudentOverviewRow::getStudentId).toList();
        List<LoAccuracyDto> loAccuracy = computeLoAccuracy(scopedItems, quizResponses, filteredIds, cache);
        resp.setLoAccuracy(loAccuracy.isEmpty() ? null : loAccuracy);

        Set<Long> allStudentIdSet = new HashSet<>(studentIds);
        int startedCount = (int) startedEvents.stream()
                .filter(e -> scopedLessonIds.contains(e.getLesson().getId())
                        && allStudentIdSet.contains(e.getStudent().getId()))
                .map(e -> e.getStudent().getId())
                .distinct()
                .count();

        Set<Long> scopedSurveyQuestionIds = scopedItems.stream()
                .filter(i -> i.getType() == LessonItemType.SURVEY)
                .flatMap(i -> cache.surveyByItem().getOrDefault(i.getId(), List.of()).stream())
                .map(SurveyQuestion::getId)
                .collect(Collectors.toSet());
        int surveyedCount = (int) surveyResponses.stream()
                .filter(r -> allStudentIdSet.contains(r.getStudent().getId())
                        && scopedSurveyQuestionIds.contains(r.getQuestion().getId()))
                .map(r -> r.getStudent().getId())
                .distinct()
                .count();

        resp.setStartedCount(startedCount);
        resp.setSurveyedCount(surveyedCount);
        return resp;
    }

    @Transactional(readOnly = true)
    public LessonTimelineResponse getLessonTimeline(Long courseId, Long sectionId) {
        if (courseId == null) throw new IllegalArgumentException("courseId is required");
        if (!courseRepository.existsById(courseId)) throw new EntityNotFoundException("Course not found");

        List<Lesson> lessons = lessonRepository.findByCourseIdOrderByPositionAsc(courseId);
        List<Long> lessonIds = lessons.stream().map(Lesson::getId).toList();
        List<Student> students = resolveStudents(courseId, sectionId);
        List<Long> studentIds = students.stream().map(Student::getId).toList();

        List<LessonEvent> completionEvents = lessonEventRepository.findByStudentIdInAndEventType(studentIds, LessonEventType.COMPLETED);
        List<QuizResponse> quizResponses = quizResponseRepository.findByStudentIdIn(studentIds);

        List<LessonItem> allLessonItems = lessonItemRepository.findByLessonIdInOrderByPositionAsc(lessonIds);
        Map<Long, List<LessonItem>> itemsByLesson = allLessonItems.stream()
                .collect(Collectors.groupingBy(i -> i.getLesson().getId()));
        AnalyticsQuestionCache.Snapshot cache = questionCache.forItems(allLessonItems);

        List<LessonTimelineRow> rows = new ArrayList<>();
        for (Lesson lesson : lessons) {
            List<LessonItem> lessonItems = itemsByLesson.getOrDefault(lesson.getId(), List.of());

            long completedCount = completionEvents.stream()
                    .filter(e -> e.getLesson().getId().equals(lesson.getId()))
                    .map(e -> e.getStudent().getId())
                    .distinct()
                    .count();
            double completionRate = students.isEmpty() ? 0.0 : (double) completedCount / students.size();

            List<Integer> scores = new ArrayList<>();
            for (Student student : students) {
                Integer score = quizScoreForStudent(student.getId(), lessonItems, quizResponses, cache);
                if (score != null) scores.add(score);
            }
            Double avgQuizScore = scores.isEmpty() ? null
                    : scores.stream().mapToInt(Integer::intValue).average().getAsDouble();

            if (avgQuizScore != null) {
                avgQuizScore = Math.round(avgQuizScore * 10.0) / 10.0;
            }

            LessonTimelineRow row = new LessonTimelineRow();
            row.setLessonId(lesson.getId());
            row.setTitle(lesson.getTitle());
            row.setPosition(lesson.getPosition());
            row.setCompletionRate(completionRate);
            row.setAvgQuizScore(avgQuizScore);
            row.setPatternFlag(patternFlagForLesson(completionRate, avgQuizScore));
            rows.add(row);
        }

        LessonTimelineResponse resp = new LessonTimelineResponse();
        resp.setLessons(rows);
        return resp;
    }

    @Transactional(readOnly = true)
    public StudentDetailResponse getStudentDetail(Long studentId, Long courseId, Long lessonId) {
        if (courseId == null) throw new IllegalArgumentException("courseId is required");
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        if (!courseRepository.existsById(courseId)) throw new EntityNotFoundException("Course not found");

        Lesson lesson = resolveLesson(courseId, lessonId);

        List<Lesson> courseLessons = lessonRepository.findByCourseIdOrderByPositionAsc(courseId);
        List<Long> courseLessonIds = courseLessons.stream().map(Lesson::getId).toList();
        List<LessonItem> allCourseItems = lessonItemRepository.findByLessonIdInOrderByPositionAsc(courseLessonIds);
        Map<Long, List<LessonItem>> itemsByLesson = allCourseItems.stream()
                .collect(Collectors.groupingBy(i -> i.getLesson().getId()));
        AnalyticsQuestionCache.Snapshot cache = questionCache.forItems(allCourseItems);

        List<LessonItem> lessonItems = itemsByLesson.getOrDefault(lesson.getId(), List.of());
        List<QuizResponse> studentQuizResponses = quizResponseRepository.findByStudentId(studentId);
        List<SurveyResponse> studentSurveyResponses = surveyResponseRepository.findByStudentId(studentId);
        List<VideoWatchEvent> watchEvents = videoWatchEventRepository.findByStudentId(studentId);

        boolean completed = lessonEventRepository.existsByStudentIdAndLessonIdAndEventType(
                studentId, lesson.getId(), LessonEventType.COMPLETED);

        Set<Long> videoItemIds = lessonItems.stream()
                .filter(i -> i.getType() == LessonItemType.VIDEO)
                .map(LessonItem::getId)
                .collect(Collectors.toSet());
        Integer watchPercent = watchEvents.stream()
                .filter(e -> videoItemIds.contains(e.getLessonItem().getId()))
                .map(VideoWatchEvent::getWatchPercent)
                .max(Integer::compareTo)
                .orElse(null);

        List<QuizResponseDetail> quizDetails = buildQuizDetails(lessonItems, studentQuizResponses, cache);
        List<SurveyResponseDetail> surveyDetails = buildSurveyDetails(lessonItems, studentSurveyResponses, cache);

        StudentLessonDetail lessonDetail = new StudentLessonDetail();
        lessonDetail.setLessonId(lesson.getId());
        lessonDetail.setTitle(lesson.getTitle());
        lessonDetail.setCompleted(completed);
        lessonDetail.setWatchPercent(watchPercent);
        lessonDetail.setQuizResponses(quizDetails);
        lessonDetail.setSurveyResponses(surveyDetails);

        List<LessonScoreRow> allLessonRows = new ArrayList<>();
        for (Lesson l : courseLessons) {
            List<LessonItem> items = itemsByLesson.getOrDefault(l.getId(), List.of());
            boolean lCompleted = lessonEventRepository.existsByStudentIdAndLessonIdAndEventType(
                    studentId, l.getId(), LessonEventType.COMPLETED);
            Integer score = quizScoreForStudent(studentId, items, studentQuizResponses, cache);
            LessonScoreRow row = new LessonScoreRow();
            row.setLessonId(l.getId());
            row.setTitle(l.getTitle());
            row.setCompleted(lCompleted);
            row.setQuizScore(score);
            allLessonRows.add(row);
        }

        Double totalGrade = totalGradeForStudent(studentId, courseLessons, allCourseItems, studentQuizResponses, cache);

        StudentDetailResponse resp = new StudentDetailResponse();
        resp.setStudentId(studentId);
        resp.setName(student.getName());
        resp.setSectionName(student.getSection().getName());
        resp.setLesson(lessonDetail);
        resp.setAllLessons(allLessonRows);
        resp.setTotalGrade(totalGrade);
        return resp;
    }

    private Lesson resolveLesson(Long courseId, Long lessonId) {
        if (lessonId != null) {
            return lessonRepository.findById(lessonId)
                    .orElseThrow(() -> new EntityNotFoundException("Lesson not found"));
        }
        return lessonRepository.findByCourseIdAndIsActiveTrue(courseId)
                .orElseThrow(() -> new IllegalArgumentException("No active lesson found for course " + courseId));
    }

    private List<Student> resolveStudents(Long courseId, Long sectionId) {
        if (sectionId != null) {
            return studentRepository.findBySectionId(sectionId);
        }
        return studentRepository.findBySectionCourseId(courseId);
    }

    private Integer quizScoreForStudent(Long studentId, List<LessonItem> lessonItems,
                                         List<QuizResponse> allResponses, AnalyticsQuestionCache.Snapshot cache) {
        List<QuizQuestion> questions = lessonItems.stream()
                .filter(i -> i.getType() == LessonItemType.QUIZ)
                .flatMap(i -> cache.quizByItem().getOrDefault(i.getId(), List.of()).stream())
                .toList();
        if (questions.isEmpty()) return null;

        Set<Long> questionIds = questions.stream().map(QuizQuestion::getId).collect(Collectors.toSet());
        List<QuizResponse> studentResponses = allResponses.stream()
                .filter(r -> r.getStudent().getId().equals(studentId)
                        && questionIds.contains(r.getQuestion().getId()))
                .toList();
        if (studentResponses.isEmpty()) return null;

        long correct = studentResponses.stream().filter(r -> r.getChoice().isCorrect()).count();
        return (int) Math.round((double) correct / questions.size() * 100);
    }

    private Integer confidenceForStudent(Long studentId, List<LessonItem> lessonItems,
                                          List<SurveyResponse> allResponses, AnalyticsQuestionCache.Snapshot cache) {
        List<SurveyQuestion> questions = lessonItems.stream()
                .filter(i -> i.getType() == LessonItemType.SURVEY)
                .flatMap(i -> cache.surveyByItem().getOrDefault(i.getId(), List.of()).stream())
                .filter(q -> q.getType() == SurveyQuestionType.LIKERT)
                .toList();
        if (questions.isEmpty()) return null;

        Set<Long> questionIds = questions.stream().map(SurveyQuestion::getId).collect(Collectors.toSet());
        List<SurveyResponse> studentResponses = allResponses.stream()
                .filter(r -> r.getStudent().getId().equals(studentId)
                        && questionIds.contains(r.getQuestion().getId())
                        && r.getLikertValue() != null)
                .toList();
        if (studentResponses.isEmpty()) return null;

        OptionalDouble avg = studentResponses.stream()
                .mapToInt(SurveyResponse::getLikertValue)
                .average();
        return avg.isPresent() ? (int) Math.round(avg.getAsDouble()) : null;
    }

    private Double totalGradeForStudent(Long studentId, List<Lesson> courseLessons,
                                         List<LessonItem> allLessonItems, List<QuizResponse> allResponses,
                                         AnalyticsQuestionCache.Snapshot cache) {
        Map<Long, List<LessonItem>> itemsByLesson = allLessonItems.stream()
                .collect(Collectors.groupingBy(i -> i.getLesson().getId()));
        List<Integer> scores = new ArrayList<>();
        for (Lesson lesson : courseLessons) {
            List<LessonItem> items = itemsByLesson.getOrDefault(lesson.getId(), List.of());
            Integer score = quizScoreForStudent(studentId, items, allResponses, cache);
            if (score != null) scores.add(score);
        }
        if (scores.isEmpty()) return null;
        return scores.stream().mapToInt(Integer::intValue).average().getAsDouble();
    }

    String patternFlagForLesson(double completionRate, Double avgQuizScore) {
        if (avgQuizScore == null) return null;
        if (completionRate < 0.6 && avgQuizScore >= 75.0) return "LOW_COMPLETION_HIGH_SCORE";
        if (completionRate >= 0.75 && avgQuizScore < 60.0) return "HIGH_COMPLETION_LOW_SCORE";
        return null;
    }

    private void validateCompletionStatus(String completionStatus) {
        if (completionStatus == null) return;
        if (!completionStatus.equals("all") && !completionStatus.equals("completed")
                && !completionStatus.equals("not_completed")) {
            throw new IllegalArgumentException("Invalid completionStatus value");
        }
    }

    private boolean passesCompletionFilter(String completionStatus, boolean completed) {
        if (completionStatus == null || completionStatus.equals("all")) return true;
        if (completionStatus.equals("completed")) return completed;
        return !completed;
    }

    private List<QuizResponseDetail> buildQuizDetails(List<LessonItem> lessonItems,
                                                      List<QuizResponse> studentResponses,
                                                      AnalyticsQuestionCache.Snapshot cache) {
        List<QuizResponseDetail> details = new ArrayList<>();
        for (LessonItem item : lessonItems) {
            if (item.getType() != LessonItemType.QUIZ) continue;
            List<QuizQuestion> questions = cache.quizByItem().getOrDefault(item.getId(), List.of());
            Set<Long> questionIds = questions.stream().map(QuizQuestion::getId).collect(Collectors.toSet());
            Map<Long, QuizResponse> responseByQuestion = studentResponses.stream()
                    .filter(r -> questionIds.contains(r.getQuestion().getId()))
                    .collect(Collectors.toMap(r -> r.getQuestion().getId(), r -> r, (a, b) -> a));
            for (QuizQuestion question : questions) {
                QuizResponse resp = responseByQuestion.get(question.getId());
                if (resp == null) continue;
                QuizResponseDetail detail = new QuizResponseDetail();
                detail.setQuestionText(question.getQuestionText());
                detail.setChosenAnswer(resp.getChoice().getChoiceText());
                detail.setCorrect(resp.getChoice().isCorrect());
                details.add(detail);
            }
        }
        return details;
    }

    private List<LoAccuracyDto> computeLoAccuracy(List<LessonItem> lessonItems,
                                                    List<QuizResponse> allResponses,
                                                    List<Long> studentIds,
                                                    AnalyticsQuestionCache.Snapshot cache) {
        List<QuizQuestion> questions = lessonItems.stream()
                .filter(i -> i.getType() == LessonItemType.QUIZ)
                .flatMap(i -> cache.quizByItem().getOrDefault(i.getId(), List.of()).stream())
                .filter(q -> q.getLearningObjective() != null)
                .toList();
        if (questions.isEmpty()) return List.of();

        Set<Long> questionIds = questions.stream().map(QuizQuestion::getId).collect(Collectors.toSet());
        Map<Long, String> loByQuestionId = questions.stream()
                .collect(Collectors.toMap(QuizQuestion::getId, QuizQuestion::getLearningObjective));

        Set<Long> studentIdSet = new HashSet<>(studentIds);
        List<QuizResponse> relevant = allResponses.stream()
                .filter(r -> studentIdSet.contains(r.getStudent().getId())
                        && questionIds.contains(r.getQuestion().getId()))
                .toList();

        Map<String, long[]> loStats = new LinkedHashMap<>();
        for (QuizResponse r : relevant) {
            String lo = loByQuestionId.get(r.getQuestion().getId());
            loStats.computeIfAbsent(lo, k -> new long[]{0, 0});
            loStats.get(lo)[1]++;
            if (r.getChoice().isCorrect()) loStats.get(lo)[0]++;
        }

        return loStats.entrySet().stream()
                .map(e -> new LoAccuracyDto(e.getKey(), (int) e.getValue()[0], (int) e.getValue()[1]))
                .sorted(Comparator.comparingDouble(LoAccuracyDto::getAccuracy))
                .toList();
    }

    private List<SurveyResponseDetail> buildSurveyDetails(List<LessonItem> lessonItems,
                                                           List<SurveyResponse> studentResponses,
                                                           AnalyticsQuestionCache.Snapshot cache) {
        List<SurveyResponseDetail> details = new ArrayList<>();
        for (LessonItem item : lessonItems) {
            if (item.getType() != LessonItemType.SURVEY) continue;
            List<SurveyQuestion> questions = cache.surveyByItem().getOrDefault(item.getId(), List.of());
            Set<Long> questionIds = questions.stream().map(SurveyQuestion::getId).collect(Collectors.toSet());
            Map<Long, SurveyResponse> responseByQuestion = studentResponses.stream()
                    .filter(r -> questionIds.contains(r.getQuestion().getId()))
                    .collect(Collectors.toMap(r -> r.getQuestion().getId(), r -> r, (a, b) -> a));
            for (SurveyQuestion question : questions) {
                SurveyResponse resp = responseByQuestion.get(question.getId());
                if (resp == null) continue;
                SurveyResponseDetail detail = new SurveyResponseDetail();
                detail.setQuestionText(question.getQuestionText());
                detail.setType(question.getType().name());
                detail.setLikertValue(resp.getLikertValue());
                detail.setFreeText(resp.getFreeText());
                details.add(detail);
            }
        }
        return details;
    }

}
