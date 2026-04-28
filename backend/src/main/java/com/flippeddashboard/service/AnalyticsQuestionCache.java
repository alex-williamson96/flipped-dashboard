package com.flippeddashboard.service;

import com.flippeddashboard.model.*;
import com.flippeddashboard.repository.QuizQuestionRepository;
import com.flippeddashboard.repository.SurveyQuestionRepository;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
class AnalyticsQuestionCache {

    private final QuizQuestionRepository quizQuestionRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;

    AnalyticsQuestionCache(QuizQuestionRepository quizQuestionRepository,
                           SurveyQuestionRepository surveyQuestionRepository) {
        this.quizQuestionRepository = quizQuestionRepository;
        this.surveyQuestionRepository = surveyQuestionRepository;
    }

    Snapshot forItems(List<LessonItem> lessonItems) {
        List<Long> quizItemIds = lessonItems.stream()
                .filter(i -> i.getType() == LessonItemType.QUIZ)
                .map(LessonItem::getId)
                .toList();
        List<Long> surveyItemIds = lessonItems.stream()
                .filter(i -> i.getType() == LessonItemType.SURVEY)
                .map(LessonItem::getId)
                .toList();

        List<QuizQuestion> quizQuestions = quizItemIds.isEmpty()
                ? List.of()
                : quizQuestionRepository.findByLessonItemIdInOrderByPositionAsc(quizItemIds);
        List<SurveyQuestion> surveyQuestions = surveyItemIds.isEmpty()
                ? List.of()
                : surveyQuestionRepository.findByLessonItemIdInOrderByPositionAsc(surveyItemIds);

        Map<Long, List<QuizQuestion>> quizByItem = quizQuestions.stream()
                .collect(Collectors.groupingBy(q -> q.getLessonItem().getId(),
                        LinkedHashMap::new, Collectors.toList()));
        Map<Long, List<SurveyQuestion>> surveyByItem = surveyQuestions.stream()
                .collect(Collectors.groupingBy(q -> q.getLessonItem().getId(),
                        LinkedHashMap::new, Collectors.toList()));

        return new Snapshot(quizByItem, surveyByItem);
    }

    record Snapshot(
            Map<Long, List<QuizQuestion>> quizByItem,
            Map<Long, List<SurveyQuestion>> surveyByItem
    ) {}
}
