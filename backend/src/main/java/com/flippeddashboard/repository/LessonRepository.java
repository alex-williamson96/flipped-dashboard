package com.flippeddashboard.repository;

import com.flippeddashboard.model.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByCourseIdOrderByPositionAsc(Long courseId);
    Optional<Lesson> findByCourseIdAndIsActiveTrue(Long courseId);
}
