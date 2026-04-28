package com.flippeddashboard.repository;

import com.flippeddashboard.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findByIsArchivedFalse();
    List<Course> findByIsArchivedTrue();
}
