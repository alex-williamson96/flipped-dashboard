package com.flippeddashboard.repository;

import com.flippeddashboard.model.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Long> {
    List<SurveyQuestion> findByLessonItemIdOrderByPositionAsc(Long lessonItemId);
    List<SurveyQuestion> findByLessonItemIdInOrderByPositionAsc(Collection<Long> lessonItemIds);
}
