package com.flippeddashboard.repository;

import com.flippeddashboard.model.QuizResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface QuizResponseRepository extends JpaRepository<QuizResponse, Long> {
    List<QuizResponse> findByStudentId(Long studentId);
    List<QuizResponse> findByStudentIdIn(List<Long> studentIds);
    boolean existsByStudentIdAndQuestion_LessonItem_Id(Long studentId, Long lessonItemId);
    boolean existsByStudentIdAndQuestion_Id(Long studentId, Long questionId);
}
