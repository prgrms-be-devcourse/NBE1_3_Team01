package org.team1.nbe1_3_team01.domain.calendar.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team1.nbe1_3_team01.domain.calendar.application.CourseScheduleQueryService
import org.team1.nbe1_3_team01.domain.calendar.application.TeamScheduleCommandService
import org.team1.nbe1_3_team01.domain.calendar.application.TeamScheduleQueryService
import org.team1.nbe1_3_team01.domain.calendar.application.response.ScheduleIdResponse
import org.team1.nbe1_3_team01.domain.calendar.application.response.ScheduleResponse
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleCreateRequest
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleDeleteRequest
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleUpdateRequest
import org.team1.nbe1_3_team01.global.auth.interceptor.GroupAuth
import org.team1.nbe1_3_team01.global.util.Response
import java.net.URI

@RestController
@RequestMapping("/api/schedules")
class ScheduleController(
    private val courseScheduleQueryService: CourseScheduleQueryService,
    private val teamScheduleQueryService: TeamScheduleQueryService,
    private val teamScheduleCommandService: TeamScheduleCommandService
) {

    /**
     * 팀 내 일정 조회
     */
    @GroupAuth(role = GroupAuth.Role.TEAM)
    @GetMapping("/teams")
    fun getTeamSchedules(
        @RequestParam teamId: Long
    ): ResponseEntity<Response<List<ScheduleResponse>>> {
        val schedules = teamScheduleQueryService.getTeamSchedules(teamId)
        return ResponseEntity.ok(Response.success(schedules))
    }

    /**
     * 팀 내 일정 상세 조회
     */
    @GroupAuth(role = GroupAuth.Role.TEAM)
    @GetMapping("/teams/{id}")
    fun getTeamSchedule(
        @PathVariable("id") teamScheduleId: Long
    ): ResponseEntity<Response<ScheduleResponse>> {
        val schedule = teamScheduleQueryService.getTeamSchedule(teamScheduleId)
        return ResponseEntity.ok(Response.success(schedule))
    }

    /**
     * 공지 일정 조회
     */
    @GroupAuth(role = GroupAuth.Role.COURSE)
    @GetMapping("/commons")
    fun getNoticeSchedules(
        @RequestParam courseId: Long
    ): ResponseEntity<Response<List<ScheduleResponse>>> {
        val schedules = courseScheduleQueryService.getCourseSchedules(courseId)
        return ResponseEntity.ok(Response.success(schedules))
    }

    /**
     * 공지 일정 상세 조회
     */
    @GroupAuth(role = GroupAuth.Role.COURSE)
    @GetMapping("/commons/{id}")
    fun getNoticeSchedule(
        @PathVariable("id") courseScheduleId: Long
    ): ResponseEntity<Response<ScheduleResponse>> {
        val schedule = teamScheduleQueryService.getTeamSchedule(courseScheduleId)
        return ResponseEntity.ok(Response.success(schedule))
    }

    /**
     * 일정 등록
     */
    @GroupAuth(role = GroupAuth.Role.TEAM)
    @PostMapping
    fun registerSchedule(
        @RequestParam teamId: Long,
        @RequestBody scheduleCreateRequest: ScheduleCreateRequest
    ): ResponseEntity<Response<ScheduleIdResponse>> {
        val scheduleIdResponse = teamScheduleCommandService.registSchedule(teamId, scheduleCreateRequest)
        val location = URI.create("/api/schedules/${scheduleIdResponse.scheduleId}")
        return ResponseEntity.created(location).body(Response.success(scheduleIdResponse))
    }

    /**
     * 일정 수정
     */
    @GroupAuth(role = GroupAuth.Role.TEAM)
    @PatchMapping
    fun updateSchedule(
        @RequestBody scheduleUpdateRequest: ScheduleUpdateRequest
    ): ResponseEntity<Response<ScheduleIdResponse>> {
        val response = teamScheduleCommandService.updateSchedule(scheduleUpdateRequest)
        return ResponseEntity.ok(Response.success(response))
    }

    /**
     * 일정 삭제
     */
    @GroupAuth(role = GroupAuth.Role.TEAM)
    @DeleteMapping
    fun deleteSchedule(
        @RequestBody scheduleDeleteRequest: ScheduleDeleteRequest
    ): ResponseEntity<Void> {
        teamScheduleCommandService.deleteSchedule(scheduleDeleteRequest.id)
        return ResponseEntity.noContent().build()
    }
}
