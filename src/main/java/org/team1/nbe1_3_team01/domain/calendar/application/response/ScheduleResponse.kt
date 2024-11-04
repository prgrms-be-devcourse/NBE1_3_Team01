package org.team1.nbe1_3_team01.domain.calendar.application.response

import org.team1.nbe1_3_team01.domain.calendar.entity.CourseSchedule
import org.team1.nbe1_3_team01.domain.calendar.entity.ScheduleType
import org.team1.nbe1_3_team01.domain.calendar.entity.TeamSchedule
import java.time.LocalDateTime

data class ScheduleResponse(
    val id: Long,
    val name: String,
    val scheduleType: ScheduleType?,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val description: String
) {
    companion object {
        fun from(courseSchedule: CourseSchedule): ScheduleResponse {
            return ScheduleResponse(
                id = courseSchedule.id,
                name = courseSchedule.name,
                scheduleType = null,
                startAt = courseSchedule.startAt,
                endAt = courseSchedule.endAt,
                description = courseSchedule.description
            )
        }

        fun from(teamSchedule: TeamSchedule): ScheduleResponse {
            return ScheduleResponse(
                id = teamSchedule.id,
                name = teamSchedule.name,
                scheduleType = teamSchedule.scheduleType,
                startAt = teamSchedule.startAt,
                endAt = teamSchedule.endAt,
                description = teamSchedule.description
            )
        }
    }
}
