package org.team1.nbe1_3_team01.domain.group.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_3_team01.domain.group.controller.request.*;
import org.team1.nbe1_3_team01.domain.group.service.TeamService;
import org.team1.nbe1_3_team01.domain.group.service.response.TeamResponse;
import org.team1.nbe1_3_team01.domain.user.service.response.UserDetailsResponse;
import org.team1.nbe1_3_team01.global.util.Message;
import org.team1.nbe1_3_team01.global.util.Response;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    public ResponseEntity<Response<Message>> createTeam(@RequestBody @Valid TeamCreateRequest teamCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Response.success(teamService.teamCreate(teamCreateRequest)));
    }

    @GetMapping("/admin/waiting")
    public ResponseEntity<Response<List<TeamResponse>>> getCreationWaitingStudyTeams() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(teamService.creationWaitingStudyTeamList()));
    }

    @PostMapping("/admin/approval")
    public ResponseEntity<Response<Message>> approveStudyTeamCreation(@RequestBody @Valid TeamApprovalUpdateRequest teamApprovalUpdateRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(teamService.studyTeamCreationApprove(teamApprovalUpdateRequest)));
    }

    @PatchMapping("/name")
    public ResponseEntity<Response<Message>> updateTeamName(@RequestBody @Valid TeamNameUpdateRequest teamNameUpdateRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(teamService.teamNameUpdate(teamNameUpdateRequest)));
    }

    @PostMapping("/member")
    public ResponseEntity<Response<Message>> addTeamMember(@RequestBody @Valid TeamMemberAddRequest teamMemberAddRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(teamService.teamAddMember(teamMemberAddRequest)));
    }

    @DeleteMapping("/member")
    public ResponseEntity<Void> deleteTeamMember(@RequestBody @Valid TeamMemberDeleteRequest teamMemberDeleteRequest) {
        teamService.teamDeleteMember(teamMemberDeleteRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteTeam(@RequestBody @Valid TeamDeleteRequest teamDeleteRequest) {
        teamService.teamDelete(teamDeleteRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/admin/approval")
    public ResponseEntity<Void> approveStudyTeamDeletion(@RequestBody @Valid TeamApprovalUpdateRequest teamApprovalUpdateRequest) {
        teamService.studyTeamDeletionApprove(teamApprovalUpdateRequest);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .build();
    }

    @GetMapping("/members/{teamId}")
    public ResponseEntity<Response<List<UserDetailsResponse>>> getTeamMembers(@PathVariable Long teamId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(teamService.teamMemberList(teamId)));
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<Response<List<TeamResponse>>> courseTeamList(@PathVariable Long courseId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(teamService.courseTeamList(courseId)));
    }

    @GetMapping("/my")
    public ResponseEntity<Response<List<TeamResponse>>> myTeamList() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(Response.success(teamService.myTeamList()));
    }

}