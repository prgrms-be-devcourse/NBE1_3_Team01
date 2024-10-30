package org.team1.nbe1_3_team01.domain.group.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.group.controller.request.*;
import org.team1.nbe1_3_team01.domain.group.entity.Belonging;
import org.team1.nbe1_3_team01.domain.group.entity.Team;
import org.team1.nbe1_3_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_3_team01.domain.group.repository.TeamRepository;
import org.team1.nbe1_3_team01.domain.group.service.response.TeamResponse;
import org.team1.nbe1_3_team01.domain.group.service.validator.TeamValidator;
import org.team1.nbe1_3_team01.domain.user.entity.Course;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.domain.user.repository.CourseRepository;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_3_team01.domain.user.service.response.UserDetailsResponse;
import org.team1.nbe1_3_team01.domain.user.util.UserConverter;
import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.ErrorCode;
import org.team1.nbe1_3_team01.global.util.Message;
import org.team1.nbe1_3_team01.global.util.SecurityUtil;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamService {

    public final TeamRepository teamRepository;
    public final CourseRepository courseRepository;

    public final UserRepository userRepository;
    public final BelongingRepository belongingRepository;

    public Message teamCreate(TeamCreateRequest teamCreateRequest) {
        List<User> users = validateUserIds(teamCreateRequest.getUserIds());
        Course course = courseRepository.findById(teamCreateRequest.getCourseId()).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));
        boolean study = teamCreateRequest.getTeamType().equals("STUDY");
        List<Belonging> belongings = new ArrayList<>();

        if (!study) SecurityUtil.validateAdmin(userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        Team newTeam = teamCreateRequest.toTeamEntity(study, course);

        for (User u : users) {
            boolean isOwner = teamCreateRequest.getLeaderId().equals(u.getId());
            Belonging b = Belonging.createBelongingOf(isOwner, u);
            newTeam.assignBelonging(b);
            belongings.add(b);
        }

        teamRepository.save(newTeam);
        belongingRepository.saveAll(belongings);
        return new Message(newTeam.getId().toString());
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> creationWaitingStudyTeamList() {
        return teamRepository.findByCreationWaiting(true).stream().map(TeamResponse::from).toList();
    }

    public Message studyTeamCreationApprove(TeamApprovalUpdateRequest teamApprovalUpdateRequest) {
        Team team = teamRepository.findById(teamApprovalUpdateRequest.getTeamId()).orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_FOUND));
        if (!team.isCreationWaiting()) throw new AppException(ErrorCode.TEAM_NOT_WAITING);

        team.setCreationWaiting(false);

        return new Message(teamRepository.save(team).getId().toString());
    }

    public Message teamNameUpdate(TeamNameUpdateRequest teamNameUpdateRequest) {
        Team team = teamRepository.findByIdWithLeaderBelonging(teamNameUpdateRequest.getTeamId()).orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_FOUND));
        User curUser = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (team.getTeamType().name().equals("PROJECT")) SecurityUtil.validateAdmin(curUser);
        if (team.getTeamType().name().equals("STUDY")) TeamValidator.validateTeamLeader(team, curUser);

        team.setName(teamNameUpdateRequest.getName());
        return new Message(team.getId().toString());
    }

    public Message teamAddMember(TeamMemberAddRequest teamMemberAddRequest) {
        List<User> users = validateUserIds(teamMemberAddRequest.getUserIds());
        User curUser = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        List<Belonging> allBelongings = belongingRepository.findAllByTeamId_JoinTeamAndUser(teamMemberAddRequest.getTeamId());
        if (allBelongings.isEmpty()) throw new AppException(ErrorCode.TEAM_NOT_FOUND);

        Team team = allBelongings.get(0).getTeam();

        if (team.getTeamType().name().equals("PROJECT")) SecurityUtil.validateAdmin(curUser);
        if (team.getTeamType().name().equals("STUDY")) TeamValidator.validateTeamLeader(team, curUser);

        List<Long> existingUserIds = allBelongings.stream().map(Belonging::getUser).map(User::getId).toList();
        List<Belonging> newBelongings = new ArrayList<>();
        for (User u : users) {
            if (existingUserIds.contains(u.getId())) throw new AppException(ErrorCode.TEAM_EXISTING_MEMBER);

            Belonging b = Belonging.createBelongingOf(false, u);
            team.assignBelonging(b);
            newBelongings.add(b);
        }

        List<Long> ids = belongingRepository.saveAll(newBelongings).stream().map(Belonging::getId).toList();
        StringBuilder msg = new StringBuilder();
        for (Long id : ids) msg.append(id).append(" ");

        return new Message(msg.toString());
    }

    public void teamDeleteMember(TeamMemberDeleteRequest teamMemberDeleteRequest) {
        List<User> users = validateUserIds(teamMemberDeleteRequest.getUserIds());
        User curUser = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Long> userIds = users.stream().map(User::getId).toList();

        List<Belonging> allBelongings = belongingRepository.findAllByTeamId_JoinTeamAndUser(teamMemberDeleteRequest.getTeamId());
        if (allBelongings.isEmpty()) throw new AppException(ErrorCode.TEAM_NOT_FOUND);

        Long leaderId = allBelongings.stream()
                .filter(Belonging::isOwner)
                .findFirst()
                .orElseThrow(() -> new AppException(ErrorCode.LEADER_BELONGING_NOT_FOUND))
                .getUser().getId();

        Team team = allBelongings.get(0).getTeam();
        String teamType = team.getTeamType().name();
        if (teamType.equals("PROJECT")) SecurityUtil.validateAdmin(curUser);
        if (teamType.equals("STUDY")) TeamValidator.validateTeamLeader(team, curUser);
        if (userIds.contains(leaderId)) throw new AppException(ErrorCode.CANNOT_DELETE_LEADER);

        belongingRepository.deleteBelongings(teamMemberDeleteRequest.getTeamId(), userIds);
    }

    public void teamDelete(TeamDeleteRequest teamDeleteRequest) {
        User curUser = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Belonging ownerBelonging = belongingRepository.findByTeamIdAndIsOwner(teamDeleteRequest.getTeamId(), true);

        Team team = ownerBelonging.getTeam();
        String teamType = team.getTeamType().name();
        if (teamType.equals("PROJECT")) {
            SecurityUtil.validateAdmin(curUser);
            teamRepository.delete(team);
        }
        if (teamType.equals("STUDY")) {
            TeamValidator.validateTeamLeader(team, curUser);
            team.setDeletionWaiting(true);
        }
    }

    public void studyTeamDeletionApprove(TeamApprovalUpdateRequest teamApprovalUpdateRequest) {
        Team team = teamRepository.findById(teamApprovalUpdateRequest.getTeamId()).orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_FOUND));
        if (!team.isDeletionWaiting()) throw new AppException(ErrorCode.TEAM_NOT_WAITING);
        teamRepository.delete(team);
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> courseTeamList(Long courseId) {
        return teamRepository.findByCourseId(courseId).stream().map(TeamResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<UserDetailsResponse> teamMemberList(Long teamId) {
        List<User> users = belongingRepository.findUsersByTeamId(teamId);
        return users.stream().map((u) -> {
            if (u.getCourse() == null) {
                return new UserDetailsResponse(u.getId(), u.getUsername(), u.getEmail(), u.getName(), "관리자");
            }
            else return UserConverter.toUserDetailsResponse(u);
        }).toList();
    }

    @Transactional(readOnly = true)
    public List<TeamResponse> myTeamList() {
        User curUser = userRepository.findByUsername(SecurityUtil.getCurrentUsername()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        List<Belonging> belongings = belongingRepository.findByUser(curUser);
        List<Team> teams = belongings.stream().map(Belonging::getTeam).toList();
        return teams.stream().map(TeamResponse::from).toList();
    }

    /* ------------------------ */

    @Transactional(readOnly = true)
    public List<User> validateUserIds(List<Long> userIds) {
        List<User> users = userRepository.findAllUsersByIdList(userIds);

        List<Long> foundUserIds = users.stream().map(User::getId).toList();
        for (Long userId : userIds) if (!foundUserIds.contains(userId)) throw new AppException(ErrorCode.USER_NOT_FOUND);

        return users;
    }

}
