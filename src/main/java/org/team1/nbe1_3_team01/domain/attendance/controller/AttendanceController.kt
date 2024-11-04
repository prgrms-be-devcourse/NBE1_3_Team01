package org.team1.nbe1_3_team01.domain.attendance.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team1.nbe1_3_team01.domain.attendance.service.AttendanceCommandService
import org.team1.nbe1_3_team01.domain.attendance.service.AttendanceQueryService
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceCreateRequest
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceIdRequest
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceUpdateRequest
import org.team1.nbe1_3_team01.domain.attendance.service.response.AttendanceIdResponse
import org.team1.nbe1_3_team01.domain.attendance.controller.response.AttendanceResponse
import org.team1.nbe1_3_team01.global.util.Response
import org.team1.nbe1_3_team01.global.util.SecurityUtil
import java.net.URI
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/attendances")
class AttendanceController(
    private val attendanceCommandService: AttendanceCommandService,
    private val attendanceQueryService: AttendanceQueryService
) {

    /**
     * 자신의 출결 요청 보기
     */
    @GetMapping
    fun getMyAttendances(): ResponseEntity<Response<List<AttendanceResponse>>> {
        val currentUsername = SecurityUtil.getCurrentUsername()

        val attendances = attendanceQueryService.getMyAttendances(currentUsername)
        return ResponseEntity.ok(Response.success(attendances))
    }

    /**
     * 자신의 출결 요청 상세 보기
     */
    @GetMapping("/{id}")
    fun getMyAttendance(
        @PathVariable("id") attendanceId: Long
    ): ResponseEntity<Response<AttendanceResponse>> {
        val currentUsername = SecurityUtil.getCurrentUsername()

        val attendance = attendanceQueryService.getByIdAndUserId(attendanceId, currentUsername)
        return ResponseEntity.ok(Response.success(attendance))
    }

    /**
     * 출결 요청 등록
     */
    @PostMapping
    fun registAttendance(
        @RequestBody @Valid attendanceCreateRequest: AttendanceCreateRequest
    ): ResponseEntity<Response<AttendanceIdResponse>> {
        val registerUsername = SecurityUtil.getCurrentUsername()

        val attendanceIdResponse = attendanceCommandService.register(registerUsername, attendanceCreateRequest)

        val location = URI.create("/api/attendances/${attendanceIdResponse.attendanceId}")
        return ResponseEntity.created(location).body(Response.success(attendanceIdResponse))
    }

    /**
     * 출결 요청 수정
     */
    @PatchMapping
    fun updateAttendance(
        @RequestBody @Valid attendanceUpdateRequest: AttendanceUpdateRequest
    ): ResponseEntity<Response<AttendanceIdResponse>> {
        val currentUsername = SecurityUtil.getCurrentUsername()

        val updatedAttendance = attendanceCommandService.update(currentUsername, attendanceUpdateRequest)
        return ResponseEntity.ok(Response.success(updatedAttendance))
    }

    /**
     * 출결 요청 삭제
     */
    @DeleteMapping
    fun deleteAttendance(
        @RequestBody attendanceIdRequest: AttendanceIdRequest
    ): ResponseEntity<Void> {
        val currentUsername = SecurityUtil.getCurrentUsername()

        attendanceCommandService.delete(currentUsername, attendanceIdRequest.id)
        return ResponseEntity.noContent().build()
    }
}
