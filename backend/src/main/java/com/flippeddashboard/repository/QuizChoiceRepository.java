package com.flippeddashboard.repository;

import com.flippeddashboard.model.QuizChoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface QuizChoiceRepository extends JpaRepository<QuizChoice, Long> {
    List<QuizChoice> findByQuestionId(Long questionId);
    List<QuizChoice> findByQuestionIdIn(Collection<Long> questionIds);
}
