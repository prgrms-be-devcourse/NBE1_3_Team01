package org.team1.nbe1_3_team01.domain.calendar.application.port;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.nbe1_3_team01.domain.calendar.entity.CourseSchedule;

public interface CourseScheduleRepository extends JpaRepository<CourseSchedule, Long> {

    List<CourseSchedule> findByCourseId(Long courseId);
}
