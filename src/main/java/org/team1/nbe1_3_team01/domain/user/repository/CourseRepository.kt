package org.team1.nbe1_3_team01.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.nbe1_3_team01.domain.user.entity.Course;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByName(String name);
}
