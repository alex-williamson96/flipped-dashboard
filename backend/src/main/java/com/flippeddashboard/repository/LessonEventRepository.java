package com.flippeddashboard.repository;

import com.flippeddashboard.model.LessonEvent;
import com.flippeddashboard.model.LessonEventType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LessonEventRepository extends JpaRepository<LessonEvent, Long> {
    boolean existsByStudentIdAndLessonIdAndEventType(Long studentId, Long lessonId, LessonEventType eventType);
    List<LessonEvent> findByStudentIdInAndEventType(List<Long> studentIds, LessonEventType eventType);
}
