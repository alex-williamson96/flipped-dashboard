package com.flippeddashboard.repository;

import com.flippeddashboard.model.LessonItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Collection;
import java.util.List;

public interface LessonItemRepository extends JpaRepository<LessonItem, Long> {
    List<LessonItem> findByLessonIdOrderByPositionAsc(Long lessonId);
    List<LessonItem> findByLessonIdInOrderByPositionAsc(Collection<Long> lessonIds);
}
