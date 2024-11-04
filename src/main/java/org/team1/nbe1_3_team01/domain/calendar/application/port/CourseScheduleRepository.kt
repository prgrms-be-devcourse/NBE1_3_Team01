package org.team1.nbe1_3_team01.domain.calendar.application.port

import org.springframework.data.jpa.repository.JpaRepository
import org.team1.nbe1_3_team01.domain.calendar.entity.CourseSchedule

interface CourseScheduleRepository : JpaRepository<CourseSchedule, Long> {

    fun findByCourseId(courseId: Long): List<CourseSchedule>
}
