package org.team1.nbe1_3_team01.domain.calendar.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_3_team01.domain.calendar.application.CourseScheduleQueryService;
import org.team1.nbe1_3_team01.domain.calendar.application.CourseScheduleService;
import org.team1.nbe1_3_team01.domain.calendar.application.TeamScheduleQueryService;
import org.team1.nbe1_3_team01.domain.calendar.application.TeamScheduleService;
import org.team1.nbe1_3_team01.domain.calendar.application.response.ScheduleIdResponse;
import org.team1.nbe1_3_team01.domain.calendar.application.response.ScheduleResponse;
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleCreateRequest;
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleDeleteRequest;
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleUpdateRequest;
import org.team1.nbe1_3_team01.global.auth.interceptor.GroupAuth;
import org.team1.nbe1_3_team01.global.auth.interceptor.GroupAuth.Role;
import org.team1.nbe1_3_team01.global.util.Response;

@RestController
@RequestMapping("/api/admin/schedules")
@RequiredArgsConstructor
public class ScheduleAdminController {

    private final CourseScheduleQueryService courseScheduleQueryService;
    private final CourseScheduleService courseScheduleService;
    private final TeamScheduleQueryService teamScheduleQueryService;
    private final TeamScheduleService teamScheduleService;

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
    @GroupAuth(role = Role.ADMIN)
    @GetMapping("/teams")
    public ResponseEntity<Response<List<ScheduleResponse>>> getTeamSchedules(
            @RequestParam(name = "team-id") Long teamId
    ) {
        return ResponseEntity.ok(
                Response.success(teamScheduleQueryService.getTeamSchedules(teamId)));
    }

    /**
     * 팀 일정 상세 조회
     */
    @GroupAuth(role = Role.ADMIN)
    @GetMapping("/teams/{id}")
    public ResponseEntity<Response<ScheduleResponse>> getTeamSchedule(
            @PathVariable("id") Long teamScheduleId
    ) {
        return ResponseEntity.ok(
                Response.success(teamScheduleQueryService.getTeamSchedule(teamScheduleId)));
    }

    /**
     * 공지 일정 조회
     */
    @GroupAuth(role = Role.ADMIN)
    @GetMapping("/commons")
    public ResponseEntity<Response<List<ScheduleResponse>>> getNoticeSchedules(
            @RequestParam(name = "course-id") Long courseId
    ) {
        return ResponseEntity.ok(
                Response.success(courseScheduleQueryService.getCourseSchedules(courseId)));
    }

    /**
     * 공지 일정 상세 조회
     */
    @GroupAuth(role = Role.ADMIN)
    @GetMapping("/commons/{id}")
    public ResponseEntity<Response<ScheduleResponse>> getNoticeSchedule(
            @PathVariable("id") Long noticeScheduleId
    ) {
        return ResponseEntity.ok(
                Response.success(courseScheduleQueryService.getCourseSchedule(noticeScheduleId)));
    }

    /**
     * 공통(공지) 일정 등록
     */
    @PostMapping
    public ResponseEntity<Void> registSchedule(
            @RequestParam(name = "course-id") Long courseId,
            @RequestBody ScheduleCreateRequest scheduleCreateRequest
    ) {
        var scheduleIdResponse = courseScheduleService.registSchedule(courseId, scheduleCreateRequest);
        return ResponseEntity
                .created(URI.create(MessageFormat.format("/api/schedules/commons/{0}", scheduleIdResponse.scheduleId())))
                .build();
    }

    /**
     * 공통(공지) 일정 수정
     */
    @PatchMapping
    public ResponseEntity<Response<ScheduleIdResponse>> updateSchedule(
            @RequestBody ScheduleUpdateRequest scheduleUpdateRequest
    ) {
        return ResponseEntity.ok(
                Response.success(courseScheduleService.updateSchedule(scheduleUpdateRequest)));
    }

    /**
     * 공통(공지) 일정 삭제
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteSchedule(
            @RequestBody ScheduleDeleteRequest scheduleDeleteRequest
    ) {
        courseScheduleService.deleteSchedule(scheduleDeleteRequest.id());
        return ResponseEntity.noContent()
                .build();
    }
}
