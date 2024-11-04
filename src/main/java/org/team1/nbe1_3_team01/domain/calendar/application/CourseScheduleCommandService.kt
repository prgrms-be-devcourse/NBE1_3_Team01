package org.team1.nbe1_3_team01.domain.calendar.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.calendar.application.port.CourseScheduleRepository
import org.team1.nbe1_3_team01.domain.calendar.application.response.ScheduleIdResponse
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleCreateRequest
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleUpdateRequest
import org.team1.nbe1_3_team01.domain.calendar.entity.CourseSchedule
import org.team1.nbe1_3_team01.domain.user.entity.Course
import org.team1.nbe1_3_team01.domain.user.repository.CourseRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode.*

@Service
@Transactional
class CourseScheduleCommandService(
    private val courseScheduleRepository: CourseScheduleRepository,
    private val courseRepository: CourseRepository
) {

    fun registerSchedule(courseId: Long, scheduleCreateRequest: ScheduleCreateRequest): ScheduleIdResponse {
        val course: Course = courseRepository.findById(courseId)
            .orElseThrow { AppException(COURSE_NOT_FOUND) }

        val courseSchedule: CourseSchedule = scheduleCreateRequest.toCourseSchedule(course)

        val saved: CourseSchedule = courseScheduleRepository.save(courseSchedule)
        return ScheduleIdResponse.from(saved.id)
    }

    fun updateSchedule(scheduleUpdateRequest: ScheduleUpdateRequest): ScheduleIdResponse {
        val courseSchedule: CourseSchedule = courseScheduleRepository.findById(scheduleUpdateRequest.id)
            .orElseThrow { AppException(SCHEDULE_NOT_FOUND) }

        courseSchedule.update(
            name = scheduleUpdateRequest.name,
            startAt = scheduleUpdateRequest.startAt,
            endAt = scheduleUpdateRequest.endAt,
            description = scheduleUpdateRequest.description
        )

        return ScheduleIdResponse.from(courseSchedule.id)
    }

    fun deleteSchedule(scheduleId: Long) {
        val courseSchedule: CourseSchedule = courseScheduleRepository.findById(scheduleId)
            .orElseThrow { AppException(SCHEDULE_NOT_FOUND) }

        courseScheduleRepository.delete(courseSchedule)
    }
}
