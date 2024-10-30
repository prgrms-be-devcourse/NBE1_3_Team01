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
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final CourseScheduleQueryService courseScheduleQueryService;
    private final CourseScheduleService courseScheduleService;
    private final TeamScheduleQueryService teamScheduleQueryService;
    private final TeamScheduleService teamScheduleService;

    /**
     * 팀 내 일정 조회
     */
    @GroupAuth(role = Role.TEAM)
    @GetMapping("/teams")
    public ResponseEntity<Response<List<ScheduleResponse>>> getTeamSchedules(
            @RequestParam Long teamId
    ) {
        return ResponseEntity.ok(
                Response.success(teamScheduleQueryService.getTeamSchedules(teamId)));
    }

    /**
     * 팀 내 일정 상세 조회
     */
    @GroupAuth(role = Role.TEAM)
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
    @GroupAuth(role = Role.COURSE)
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
    @GroupAuth(role = Role.COURSE)
    @GetMapping("/commons/{id}")
    public ResponseEntity<Response<ScheduleResponse>> getNoticeSchedule(
            @PathVariable("id") Long courseScheduleId
    ) {
        return ResponseEntity.ok(
                Response.success(teamScheduleQueryService.getTeamSchedule(courseScheduleId)));
    }

    /**
     * 일정 등록
     */
    @GroupAuth(role = Role.TEAM)
    @PostMapping
    public ResponseEntity<Response<ScheduleIdResponse>> registSchedule(
            @RequestParam(name = "team-id") Long teamId,
            @RequestBody ScheduleCreateRequest scheduleCreateRequest
    ) {
        var scheduleIdResponse = teamScheduleService.registSchedule(teamId, scheduleCreateRequest);

        return ResponseEntity
                .created(URI.create(MessageFormat.format("/api/schedules/{0}", scheduleIdResponse.scheduleId())))
                .body(Response.success(scheduleIdResponse));
    }

    /**
     * 일정 수정
     */
    @GroupAuth(role = Role.TEAM)
    @PatchMapping
    public ResponseEntity<Response<ScheduleIdResponse>> updateSchedule(
            @RequestBody ScheduleUpdateRequest scheduleUpdateRequest
    ) {
        return ResponseEntity.ok(
                Response.success(teamScheduleService.updateSchedule(scheduleUpdateRequest)));
    }

    /**
     * 일정 삭제
     */
    @GroupAuth(role = Role.TEAM)
    @DeleteMapping
    public ResponseEntity<Void> deleteSchedule(
            @RequestBody ScheduleDeleteRequest scheduleDeleteRequest
    ) {
        teamScheduleService.deleteSchedule(scheduleDeleteRequest.id());

        return ResponseEntity.noContent()
                .build();
    }
}
