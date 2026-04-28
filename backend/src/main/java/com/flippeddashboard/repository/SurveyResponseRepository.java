package com.flippeddashboard.repository;

import com.flippeddashboard.model.SurveyResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SurveyResponseRepository extends JpaRepository<SurveyResponse, Long> {
    List<SurveyResponse> findByStudentId(Long studentId);
    List<SurveyResponse> findByStudentIdIn(List<Long> studentIds);
    boolean existsByStudentIdAndQuestion_LessonItem_Id(Long studentId, Long lessonItemId);
    boolean existsByStudentIdAndQuestion_Id(Long studentId, Long questionId);
}
