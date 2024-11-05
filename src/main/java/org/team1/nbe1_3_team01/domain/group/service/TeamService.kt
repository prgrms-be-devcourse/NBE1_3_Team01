package org.team1.nbe1_3_team01.domain.group.service

import lombok.RequiredArgsConstructor
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.group.controller.request.*
import org.team1.nbe1_3_team01.domain.group.entity.Belonging
import org.team1.nbe1_3_team01.domain.group.entity.TeamType
import org.team1.nbe1_3_team01.domain.group.repository.BelongingRepository
import org.team1.nbe1_3_team01.domain.group.repository.TeamRepository
import org.team1.nbe1_3_team01.domain.group.service.response.TeamResponse
import org.team1.nbe1_3_team01.domain.group.service.validator.TeamValidator
import org.team1.nbe1_3_team01.domain.user.entity.Course
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.CourseRepository
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.domain.user.service.response.UserDetailsResponse
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import org.team1.nbe1_3_team01.global.util.Message
import org.team1.nbe1_3_team01.global.util.SecurityUtil
import java.util.function.Supplier

@Service
@Transactional
class TeamService(
    val teamRepository: TeamRepository,
    val courseRepository: CourseRepository,
    val userRepository: UserRepository,
    val belongingRepository: BelongingRepository
) {

    fun teamCreate(teamCreateRequest: TeamCreateRequest): Message {
        val users = validateUserIds(teamCreateRequest.userIds)
        val course = courseRepository.findByIdOrNull(teamCreateRequest.courseId)
            ?: throw AppException(ErrorCode.COURSE_NOT_FOUND)
        val study = teamCreateRequest.teamType == "STUDY"
        val belongings = mutableListOf<Belonging>()

        if (!study)
            SecurityUtil.validateAdmin(userRepository.findByUsername(SecurityUtil.getCurrentUsername())
                ?: throw AppException(ErrorCode.USER_NOT_FOUND))
        val newTeam = teamCreateRequest.toTeamEntity(study, course)

        for (u in users) {
            val isOwner = teamCreateRequest.leaderId == u.id
            val b = Belonging(isOwner = isOwner, user = u)
            newTeam.assignBelonging(b)
            belongings.add(b)
        }

        teamRepository.save(newTeam)
        belongingRepository.saveAll(belongings)
        return Message(newTeam.id.toString())
    }

    @Transactional(readOnly = true)
    fun creationWaitingStudyTeamList(): List<TeamResponse> {
        return teamRepository.findByCreationWaiting(true).map { TeamResponse.from(it) }.toList()
    }

    fun studyTeamCreationApprove(teamApprovalUpdateRequest: TeamApprovalUpdateRequest): Message {
        val team = teamRepository.findByIdOrNull(teamApprovalUpdateRequest.teamId)
            ?: throw AppException(ErrorCode.TEAM_NOT_FOUND)
        if (!team.creationWaiting) throw AppException(ErrorCode.TEAM_NOT_WAITING)
        team.creationWaiting = false
        return Message(teamRepository.save(team).id.toString())
    }

    fun studyTeamCreationReject(teamApprovalUpdateRequest: TeamApprovalUpdateRequest): Message {
        val team = teamRepository.findByIdOrNull(teamApprovalUpdateRequest.teamId)
            ?: throw AppException(ErrorCode.TEAM_NOT_FOUND)
        if (!team.creationWaiting) throw AppException(ErrorCode.TEAM_NOT_WAITING)
        teamRepository.delete(team)
        return Message("거절 및 삭제 완료")
    }

    fun teamNameUpdate(teamNameUpdateRequest: TeamNameUpdateRequest): Message {
        val team = teamRepository.findByIdWithLeaderBelonging(teamNameUpdateRequest.teamId)
            ?: throw AppException(ErrorCode.TEAM_NOT_FOUND)
        val curUser = userRepository.findByUsername(SecurityUtil.getCurrentUsername())
            ?: throw AppException(ErrorCode.USER_NOT_FOUND)

        if (team.teamType.name == "PROJECT") SecurityUtil.validateAdmin(curUser)
        if (team.teamType.name == "STUDY") TeamValidator.validateTeamLeader(team, curUser)

        team.name = teamNameUpdateRequest.name
        return Message(team.id.toString())
    }

    fun teamLeaderUpdate(teamLeaderUpdateRequest: TeamLeaderUpdateRequest, teamType: TeamType): Message {
        val curUser = userRepository.findByUsername(SecurityUtil.getCurrentUsername())
            ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        val leaderBelonging = belongingRepository.findByTeamIdAndIsOwner(teamLeaderUpdateRequest.teamId, true)
            ?: throw AppException(ErrorCode.TEAM_NOT_FOUND)
        val newLeader = userRepository.findByIdOrNull(teamLeaderUpdateRequest.newLeaderId)
            ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        leaderBelonging.team?.let {
            if (it.teamType != teamType) throw AppException(ErrorCode.INVALID_TEAM_ID)
            if (teamType == TeamType.STUDY) TeamValidator.validateTeamLeader(it, curUser)
            val newLeaderBelonging = belongingRepository.findByTeamAndUser(it, newLeader)
                ?: throw AppException(ErrorCode.BELONGING_NOT_FOUND)
            leaderBelonging.isOwner = false
            newLeaderBelonging.isOwner = true

            return Message(newLeaderBelonging.id.toString())
        }

        return Message("오류를 반환해야 합니다.")
    }

    fun teamAddMember(teamMemberAddRequest: TeamMemberAddRequest): Message {
        val users = validateUserIds(teamMemberAddRequest.userIds)
        val curUser = userRepository.findByUsername(SecurityUtil.getCurrentUsername())
            ?: throw AppException(ErrorCode.USER_NOT_FOUND)

        val allBelongings = belongingRepository.findAllByTeamId_JoinTeamAndUser(teamMemberAddRequest.teamId)
            .ifEmpty { throw AppException(ErrorCode.TEAM_NOT_FOUND) }

        val team = allBelongings.first().team!!
        if (team.teamType.name == "PROJECT") SecurityUtil.validateAdmin(curUser)
        if (team.teamType.name == "STUDY") TeamValidator.validateTeamLeader(team, curUser)

        val existingUserIds = allBelongings.map { it.user.id!! }.toList()
        val newBelongings= mutableListOf<Belonging>()
        for (u in users) {
            if (existingUserIds.contains(u.id)) throw AppException(ErrorCode.TEAM_EXISTING_MEMBER)
            val b = Belonging(isOwner = false, user = u)
            team.assignBelonging(b)
            newBelongings.add(b)
        }

        val ids: List<Long> = belongingRepository.saveAll(newBelongings).map { it.id!! }.toList()
        return Message(ids.joinToString(", "))
    }

    fun teamDeleteMember(teamMemberDeleteRequest: TeamMemberDeleteRequest) {
        val users = validateUserIds(teamMemberDeleteRequest.userIds)
        val curUser: User = userRepository.findByUsername(SecurityUtil.getCurrentUsername())
            ?: throw AppException(ErrorCode.USER_NOT_FOUND)

        val userIds = users.map { it.id!! }.toList()

        val allBelongings = belongingRepository.findAllByTeamId_JoinTeamAndUser(teamMemberDeleteRequest.teamId)
            .ifEmpty { throw  AppException(ErrorCode.TEAM_NOT_FOUND)}

        val leaderId = allBelongings.firstOrNull { it.isOwner }?.user?.id
            ?: throw AppException(ErrorCode.LEADER_BELONGING_NOT_FOUND)

        val team = allBelongings.first().team!!
        val teamType = team.teamType.name
        if (teamType == "PROJECT") SecurityUtil.validateAdmin(curUser)
        if (teamType == "STUDY") TeamValidator.validateTeamLeader(team, curUser)
        if (userIds.contains(leaderId)) throw AppException(ErrorCode.CANNOT_DELETE_LEADER)

        belongingRepository.deleteBelongings(teamMemberDeleteRequest.teamId, userIds)
    }

    fun teamDelete(teamDeleteRequest: TeamDeleteRequest) {
        val curUser= userRepository.findByUsername(SecurityUtil.getCurrentUsername())
            ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        val ownerBelonging = belongingRepository.findByTeamIdAndIsOwner(teamDeleteRequest.teamId, true)
            ?: throw AppException(ErrorCode.TEAM_NOT_FOUND)

        val team = ownerBelonging.team!!
        val teamType = team.teamType.name
        if (teamType == "PROJECT") {
            SecurityUtil.validateAdmin(curUser)
            teamRepository.delete(team)
        }
        if (teamType == "STUDY") {
            TeamValidator.validateTeamLeader(team, curUser)
            team.deletionWaiting = true
        }
    }

    fun studyTeamDeletionApprove(teamApprovalUpdateRequest: TeamApprovalUpdateRequest) {
        val team = teamRepository.findByIdOrNull(teamApprovalUpdateRequest.teamId)
            ?: throw AppException(ErrorCode.TEAM_NOT_FOUND)
        if (!team.deletionWaiting) throw AppException(ErrorCode.TEAM_NOT_WAITING)
        teamRepository.delete(team)
    }

    @Transactional(readOnly = true)
    fun courseTeamList(courseId: Long): List<TeamResponse> {
        return teamRepository.findByCourseId(courseId).map { TeamResponse.from(it) }.toList()
    }

    @Transactional(readOnly = true)
    fun teamMemberList(teamId: Long): List<UserDetailsResponse> {
        val users = belongingRepository.findUsersByTeamId(teamId)
        return users.map { u ->
            if (u.course == null) UserDetailsResponse(id = u.id!!, username = u.username, email = u.email, name = u.name, courseName = "관리자")
            else UserDetailsResponse.from(u)
        }.toList()
    }

    @Transactional(readOnly = true)
    fun myTeamList(): List<TeamResponse> {
        val curUser = userRepository.findByUsername(SecurityUtil.getCurrentUsername())
            ?: throw AppException(ErrorCode.USER_NOT_FOUND)

        val belongings = belongingRepository.findByUser(curUser)
        val teams = belongings.map { it.team!! }.toList()

        return teams.map { TeamResponse.from(it) }.toList()
    }

    /* ------------------------ */

    @Transactional(readOnly = true)
    fun validateUserIds(userIds: List<Long>): List<User> {
        val users = userRepository.findAllUsersByIdList(userIds)

        val foundUserIds = users.map { it.id!! }.toList()
        for (userId in userIds) if (!foundUserIds.contains(userId)) throw AppException(ErrorCode.USER_NOT_FOUND)

        return users
    }
}
