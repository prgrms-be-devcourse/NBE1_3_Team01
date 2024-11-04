package org.team1.nbe1_3_team01.domain.group.controller

import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team1.nbe1_3_team01.domain.group.controller.request.*
import org.team1.nbe1_3_team01.domain.group.entity.TeamType
import org.team1.nbe1_3_team01.domain.group.service.TeamService
import org.team1.nbe1_3_team01.domain.group.service.response.TeamResponse
import org.team1.nbe1_3_team01.domain.user.service.response.UserDetailsResponse
import org.team1.nbe1_3_team01.global.util.Message
import org.team1.nbe1_3_team01.global.util.Response

@RestController
@RequestMapping("/api/teams")
class TeamController (
    val teamService: TeamService
) {

    @PostMapping
    fun createTeam(@RequestBody teamCreateRequest: @Valid TeamCreateRequest): ResponseEntity<Response<Message>> {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(Response.success(
                teamService.teamCreate(teamCreateRequest)
            ))
    }

    @get:GetMapping("/admin/waiting")
    val creationWaitingStudyTeams: ResponseEntity<Response<List<TeamResponse>>>
        get() = ResponseEntity.status(HttpStatus.OK)
            .body(
                Response.success(
                    teamService.creationWaitingStudyTeamList()
                ))

    @PostMapping("/admin/approval")
    fun approveStudyTeamCreation(@RequestBody teamApprovalUpdateRequest: @Valid TeamApprovalUpdateRequest): ResponseEntity<Response<Message>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                Response.success(
                    teamService.studyTeamCreationApprove(teamApprovalUpdateRequest)
                ))
    }

    @PatchMapping("/name")
    fun updateTeamName(@RequestBody teamNameUpdateRequest: @Valid TeamNameUpdateRequest): ResponseEntity<Response<Message>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                Response.success(
                    teamService.teamNameUpdate(teamNameUpdateRequest)
                ))
    }

    @PatchMapping("/admin/leader")
    fun updateProjectTeamLeader(@RequestBody teamLeaderUpdateRequest: @Valid TeamLeaderUpdateRequest): ResponseEntity<Response<Message>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                Response.success(
                    teamService.teamLeaderUpdate(teamLeaderUpdateRequest, TeamType.PROJECT)
                ))
    }

    @PatchMapping("/leader")
    fun updateStudyTeamLeader(@RequestBody teamLeaderUpdateRequest: @Valid TeamLeaderUpdateRequest): ResponseEntity<Response<Message>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                Response.success(
                    teamService.teamLeaderUpdate(teamLeaderUpdateRequest, TeamType.STUDY)
                ))
    }

    @PostMapping("/member")
    fun addTeamMember(@RequestBody teamMemberAddRequest: @Valid TeamMemberAddRequest): ResponseEntity<Response<Message>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                Response.success(
                    teamService.teamAddMember(teamMemberAddRequest)
                ))
    }

    @DeleteMapping("/member")
    fun deleteTeamMember(@RequestBody teamMemberDeleteRequest: @Valid TeamMemberDeleteRequest): ResponseEntity<Void> {
        teamService.teamDeleteMember(teamMemberDeleteRequest)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping
    fun deleteTeam(@RequestBody teamDeleteRequest: @Valid TeamDeleteRequest): ResponseEntity<Void> {
        teamService.teamDelete(teamDeleteRequest)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @DeleteMapping("/admin/approval")
    fun approveStudyTeamDeletion(@RequestBody teamApprovalUpdateRequest: @Valid TeamApprovalUpdateRequest): ResponseEntity<Void> {
        teamService.studyTeamDeletionApprove(teamApprovalUpdateRequest)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @GetMapping("/members/{teamId}")
    fun getTeamMembers(@PathVariable teamId: Long): ResponseEntity<Response<List<UserDetailsResponse>>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                Response.success(
                    teamService.teamMemberList(teamId)
                ))
    }

    @GetMapping("/course/{courseId}")
    fun courseTeamList(@PathVariable courseId: Long): ResponseEntity<Response<List<TeamResponse>>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                Response.success(
                    teamService.courseTeamList(courseId)
                ))
    }

    @GetMapping("/my")
    fun myTeamList(): ResponseEntity<Response<List<TeamResponse>>> {
        return ResponseEntity.status(HttpStatus.OK)
            .body(
                Response.success(
                    teamService.myTeamList()
                ))
    }
}