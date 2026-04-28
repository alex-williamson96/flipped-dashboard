package com.flippeddashboard.repository;

import com.flippeddashboard.model.QuizQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    List<QuizQuestion> findByLessonItemIdOrderByPositionAsc(Long lessonItemId);
    List<QuizQuestion> findByLessonItemIdInOrderByPositionAsc(Collection<Long> lessonItemIds);
}
