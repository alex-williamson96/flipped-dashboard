package com.flippeddashboard.repository;

import com.flippeddashboard.model.VideoWatchEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VideoWatchEventRepository extends JpaRepository<VideoWatchEvent, Long> {
    List<VideoWatchEvent> findByStudentIdIn(List<Long> studentIds);
    List<VideoWatchEvent> findByStudentId(Long studentId);
    boolean existsByStudentIdAndLessonItem_Id(Long studentId, Long lessonItemId);
}
