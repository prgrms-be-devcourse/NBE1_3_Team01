package org.team1.nbe1_3_team01.domain.calendar.controller

import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team1.nbe1_3_team01.domain.calendar.application.CourseScheduleQueryService
import org.team1.nbe1_3_team01.domain.calendar.application.TeamScheduleQueryService
import org.team1.nbe1_3_team01.domain.calendar.application.response.ScheduleIdResponse
import org.team1.nbe1_3_team01.domain.calendar.application.response.ScheduleResponse
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleCreateRequest
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleDeleteRequest
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleUpdateRequest
import org.team1.nbe1_3_team01.domain.calendar.service.CourseScheduleCommandService
import org.team1.nbe1_3_team01.global.auth.interceptor.GroupAuth
import org.team1.nbe1_3_team01.global.util.Response
import java.net.URI
import java.text.MessageFormat

@RestController
@RequestMapping("/api/admin/schedules")
@RequiredArgsConstructor
class ScheduleAdminController(
    private val courseScheduleQueryService: CourseScheduleQueryService,
    private val courseScheduleCommandService: CourseScheduleCommandService,
    private val teamScheduleQueryService: TeamScheduleQueryService
) {
    /**
     * 모든 일정 조회
     * (모든 일정을 가져올 수 있는 API 필요?)
     */
    /*@GetMapping
    public ResponseEntity<Response<List<ScheduleResponse>>> getAllSchedules() {
        return ResponseEntity.ok(
                Response.success(scheduleQueryService.getAllSchedules()));
    }*/

    /**
     * 팀 내 일정 조회
     */
    @GroupAuth(role = GroupAuth.Role.ADMIN)
    @GetMapping("/teams")
    fun getTeamSchedules(
        @RequestParam(name = "team-id") teamId: Long
    ): ResponseEntity<Response<List<ScheduleResponse>>> {
        return ResponseEntity.ok(
            Response.success(
                teamScheduleQueryService.getTeamSchedules(teamId)
            )
        )
    }

    /**
     * 팀 일정 상세 조회
     */
    @GroupAuth(role = GroupAuth.Role.ADMIN)
    @GetMapping("/teams/{id}")
    fun getTeamSchedule(
        @PathVariable("id") teamScheduleId: Long
    ): ResponseEntity<Response<ScheduleResponse>> {
        return ResponseEntity.ok(
            Response.success(
                teamScheduleQueryService.getTeamSchedule(teamScheduleId)
            )
        )
    }

    /**
     * 공지 일정 조회
     */
    @GroupAuth(role = GroupAuth.Role.ADMIN)
    @GetMapping("/commons")
    fun getNoticeSchedules(
        @RequestParam courseId: Long
    ): ResponseEntity<Response<List<ScheduleResponse>>> {
        return ResponseEntity.ok(
            Response.success(
                courseScheduleQueryService.getCourseSchedules(courseId)
            )
        )
    }

    /**
     * 공지 일정 상세 조회
     */
    @GroupAuth(role = GroupAuth.Role.ADMIN)
    @GetMapping("/commons/{id}")
    fun getNoticeSchedule(
        @PathVariable("id") noticeScheduleId: Long
    ): ResponseEntity<Response<ScheduleResponse>> {
        return ResponseEntity.ok(
            Response.success(
                courseScheduleQueryService.getCourseSchedule(noticeScheduleId)
            )
        )
    }

    /**
     * 공통(공지) 일정 등록
     */
    @PostMapping
    fun registSchedule(
        @RequestParam courseId: Long,
        @RequestBody scheduleCreateRequest: ScheduleCreateRequest
    ): ResponseEntity<Void> {
        val scheduleIdResponse =
            courseScheduleCommandService.registerSchedule(courseId, scheduleCreateRequest)
        return ResponseEntity.created(
                URI.create(
                    MessageFormat.format("/api/schedules/commons/{0}", scheduleIdResponse.scheduleId)
                ))
            .build()
    }

    /**
     * 공통(공지) 일정 수정
     */
    @PatchMapping
    fun updateSchedule(
        @RequestBody scheduleUpdateRequest: ScheduleUpdateRequest
    ): ResponseEntity<Response<ScheduleIdResponse>> {
        return ResponseEntity.ok(
            Response.success(courseScheduleCommandService.updateSchedule(scheduleUpdateRequest))
        )
    }

    /**
     * 공통(공지) 일정 삭제
     */
    @DeleteMapping
    fun deleteSchedule(
        @RequestBody scheduleDeleteRequest: ScheduleDeleteRequest
    ): ResponseEntity<Void> {
        courseScheduleCommandService.deleteSchedule(scheduleDeleteRequest.id)
        return ResponseEntity.noContent()
            .build()
    }
}
