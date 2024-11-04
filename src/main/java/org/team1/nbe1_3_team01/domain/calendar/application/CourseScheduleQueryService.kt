package org.team1.nbe1_3_team01.domain.calendar.application

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.calendar.application.port.CourseScheduleRepository
import org.team1.nbe1_3_team01.domain.calendar.application.response.ScheduleResponse
import org.team1.nbe1_3_team01.domain.calendar.entity.CourseSchedule
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode

@Service
@Transactional(readOnly = true)
class CourseScheduleQueryService(
    private val courseScheduleRepository: CourseScheduleRepository
) {

    fun getCourseSchedules(courseId: Long): List<ScheduleResponse> {
        val courseSchedules: List<CourseSchedule> = courseScheduleRepository.findByCourseId(courseId)

        return courseSchedules
            .map { courseSchedule: CourseSchedule -> ScheduleResponse.from(courseSchedule) }
            .toList()
    }

    fun getCourseSchedule(scheduleId: Long): ScheduleResponse {
        val courseSchedule: CourseSchedule = courseScheduleRepository.findById(scheduleId)
            .orElseThrow { AppException(ErrorCode.SCHEDULE_NOT_FOUND) }

        return ScheduleResponse.from(courseSchedule)
    }
}
