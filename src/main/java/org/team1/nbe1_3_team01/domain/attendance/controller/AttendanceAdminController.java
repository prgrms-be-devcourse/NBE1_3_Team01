package org.team1.nbe1_3_team01.domain.attendance.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceIdRequest;
import org.team1.nbe1_3_team01.domain.attendance.service.AttendanceApprover;
import org.team1.nbe1_3_team01.domain.attendance.service.AttendanceQueryService;
import org.team1.nbe1_3_team01.domain.attendance.service.response.AttendanceIdResponse;
import org.team1.nbe1_3_team01.domain.attendance.controller.response.AttendanceResponse;
import org.team1.nbe1_3_team01.global.util.Response;

@RestController
@RequestMapping("/api/admin/attendances")
@RequiredArgsConstructor
public class AttendanceAdminController {

    private final AttendanceApprover attendanceApprover;
    private final AttendanceQueryService attendanceQueryService;

    /**
     * 관리자 - 모든 출결 요청 보기
     */
    @GetMapping
    public ResponseEntity<Response<List<AttendanceResponse>>> findAll() {
        return ResponseEntity.ok(
                Response.success(attendanceQueryService.getAll()));
    }

    /**
     * 관리자 - 출결 요청 상세 내역 보기
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response<AttendanceResponse>> getAttendanceById(
            @PathVariable("id") Long attendanceId
    ) {
        return ResponseEntity.ok(
                Response.success(attendanceQueryService.getById(attendanceId)));
    }

    /**
     * 관리자 - 출결 요청 승인
     */
    @PostMapping("/approve")
    public ResponseEntity<Response<AttendanceIdResponse>> approveAttendance(
            @RequestBody AttendanceIdRequest attendanceIdRequest
    ) {
        return ResponseEntity.ok(
                Response.success(attendanceApprover.approve(attendanceIdRequest.id())));
    }

    /**
     * 관리자 - 출결 요청 반려
     */
    @PostMapping("/reject")
    public ResponseEntity<Void> rejectAttendance(
            @RequestBody AttendanceIdRequest attendanceIdRequest
    ) {
        attendanceApprover.reject(attendanceIdRequest.id());
        return ResponseEntity.noContent()
                .build();
    }
}
