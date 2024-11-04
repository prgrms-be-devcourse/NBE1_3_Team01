package org.team1.nbe1_3_team01.domain.calendar.controller.dto

import org.team1.nbe1_3_team01.domain.calendar.entity.CourseSchedule
import org.team1.nbe1_3_team01.domain.calendar.entity.ScheduleType
import org.team1.nbe1_3_team01.domain.calendar.entity.TeamSchedule
import org.team1.nbe1_3_team01.domain.group.entity.Team
import org.team1.nbe1_3_team01.domain.user.entity.Course
import java.time.LocalDateTime

data class ScheduleCreateRequest(
    val name: String,
    val scheduleType: ScheduleType,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val description: String
) {
    fun toCourseSchedule(course: Course): CourseSchedule {
        return CourseSchedule(
            name = name,
            startAt = startAt,
            endAt = endAt,
            description = description,
            course = course
        )
    }

    fun toTeamSchedule(team: Team): TeamSchedule {
        return TeamSchedule(
            name = name,
            scheduleType = scheduleType,
            startAt = startAt,
            endAt = endAt,
            description = description,
            team = team
        )
    }
}
