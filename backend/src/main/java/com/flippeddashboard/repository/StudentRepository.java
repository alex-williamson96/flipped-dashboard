package com.flippeddashboard.repository;

import com.flippeddashboard.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findBySectionCourseId(Long courseId);
    List<Student> findBySectionId(Long sectionId);
}
