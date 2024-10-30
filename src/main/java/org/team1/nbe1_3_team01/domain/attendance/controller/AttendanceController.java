package org.team1.nbe1_3_team01.domain.attendance.controller;

import jakarta.validation.Valid;
import java.net.URI;
import java.text.MessageFormat;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceCreateRequest;
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceIdRequest;
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceUpdateRequest;
import org.team1.nbe1_3_team01.domain.attendance.service.AttendanceQueryService;
import org.team1.nbe1_3_team01.domain.attendance.service.AttendanceCommandService;
import org.team1.nbe1_3_team01.domain.attendance.service.response.AttendanceIdResponse;
import org.team1.nbe1_3_team01.domain.attendance.controller.response.AttendanceResponse;
import org.team1.nbe1_3_team01.global.util.Response;
import org.team1.nbe1_3_team01.global.util.SecurityUtil;

@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceCommandService attendanceCommandService;
    private final AttendanceQueryService attendanceQueryService;

    /**
     * 자신의 출결 요청 보기
     */
    @GetMapping
    public ResponseEntity<Response<List<AttendanceResponse>>> getMyAttendances() {
        var currentUsername = SecurityUtil.getCurrentUsername();

        return ResponseEntity.ok(
                Response.success(attendanceQueryService.getMyAttendances(currentUsername)));
    }

    /**
     * 자신의 출결 요청 상세 보기
     */
    @GetMapping("/{id}")
    public ResponseEntity<Response<AttendanceResponse>> getMyAttendance(
            @PathVariable("id") Long attendanceId
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        return ResponseEntity.ok(
                Response.success(attendanceQueryService.getByIdAndUserId(attendanceId, currentUsername)));
    }

    /**
     * 출결 요청 등록
     */
    @PostMapping
    public ResponseEntity<Response<AttendanceIdResponse>> registAttendance(
            @RequestBody @Valid AttendanceCreateRequest attendanceCreateRequest
    ) {
        var registerUsername = SecurityUtil.getCurrentUsername();

        var attendanceIdResponse = attendanceCommandService.register(registerUsername, attendanceCreateRequest);
        return ResponseEntity
                .created(URI.create(MessageFormat.format("/api/attendances/{0}", attendanceIdResponse.attendanceId())))
                .body(Response.success(attendanceIdResponse));
    }

    /**
     * 출결 요청 수정
     */
    @PatchMapping
    public ResponseEntity<Response<AttendanceIdResponse>> updateAttendance(
            @RequestBody @Valid AttendanceUpdateRequest attendanceUpdateRequest
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        return ResponseEntity.ok(
                Response.success(attendanceCommandService.update(currentUsername, attendanceUpdateRequest)));
    }

    /**
     * 출결 요청 삭제
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAttendance(
            @RequestBody AttendanceIdRequest attendanceIdRequest
    ) {
        var currentUsername = SecurityUtil.getCurrentUsername();

        attendanceCommandService.delete(currentUsername, attendanceIdRequest.id());
        return ResponseEntity.noContent()
                .build();
    }
}
