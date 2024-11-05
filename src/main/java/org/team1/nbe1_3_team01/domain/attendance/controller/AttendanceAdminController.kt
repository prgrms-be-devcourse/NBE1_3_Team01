package org.team1.nbe1_3_team01.domain.attendance.controller

import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team1.nbe1_3_team01.domain.attendance.controller.response.AttendancePage
import org.team1.nbe1_3_team01.domain.attendance.service.AttendanceApprover
import org.team1.nbe1_3_team01.domain.attendance.service.AttendanceQueryService
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceIdRequest
import org.team1.nbe1_3_team01.domain.attendance.service.response.AttendanceIdResponse
import org.team1.nbe1_3_team01.domain.attendance.controller.response.AttendanceResponse
import org.team1.nbe1_3_team01.global.auth.interceptor.GroupAuth
import org.team1.nbe1_3_team01.global.util.Response

@RestController
@RequestMapping("/api/admin/attendances")
class AttendanceAdminController(
    private val attendanceApprover: AttendanceApprover,
    private val attendanceQueryService: AttendanceQueryService
) {

    /**
     * 관리자 - 모든 출결 요청 보기
     */
    @GroupAuth(GroupAuth.Role.ADMIN)
    @GetMapping
    fun findAll(
        @PageableDefault pageable: Pageable
    ): ResponseEntity<Response<AttendancePage>> =
        ResponseEntity.ok(
            Response.success(attendanceQueryService.getAll(pageable))
        )

    /**
     * 관리자 - 수강생 출결 요청 모두 보기
     */
    @GroupAuth(GroupAuth.Role.ADMIN)
    @GetMapping("/students")
    fun findStudentAttendances(
        @PageableDefault pageable: Pageable
    ): ResponseEntity<Response<AttendancePage>> =
        ResponseEntity.ok(
            Response.success(attendanceQueryService.getStudentAttendances(pageable))
        )

    /**
     * 관리자 - 출결 요청 상세 내역 보기
     */
    @GroupAuth(GroupAuth.Role.ADMIN)
    @GetMapping("/{id}")
    fun getAttendanceById(
        @PathVariable("id") attendanceId: Long
    ): ResponseEntity<Response<AttendanceResponse>> =
        ResponseEntity.ok(
            Response.success(attendanceQueryService.getById(attendanceId))
        )

    /**
     * 관리자 - 출결 요청 승인
     */
    @GroupAuth(GroupAuth.Role.ADMIN)
    @PostMapping("/approve")
    fun approveAttendance(
        @RequestBody attendanceIdRequest: AttendanceIdRequest
    ): ResponseEntity<Response<AttendanceIdResponse>> =
        ResponseEntity.ok(
            Response.success(attendanceApprover.approve(attendanceIdRequest.id))
        )

    /**
     * 관리자 - 출결 요청 반려
     */
    @GroupAuth(GroupAuth.Role.ADMIN)
    @PostMapping("/reject")
    fun rejectAttendance(
        @RequestBody attendanceIdRequest: AttendanceIdRequest
    ): ResponseEntity<Void> {
        attendanceApprover.reject(attendanceIdRequest.id)
        return ResponseEntity.noContent().build()
    }
}
