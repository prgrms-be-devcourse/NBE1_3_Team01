package org.team1.nbe1_3_team01.domain.group.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.IntegrationTestSupport;
import org.team1.nbe1_3_team01.domain.group.controller.request.TeamApprovalUpdateRequest;
import org.team1.nbe1_3_team01.domain.group.controller.request.TeamCreateRequest;
import org.team1.nbe1_3_team01.domain.group.entity.Belonging;
import org.team1.nbe1_3_team01.domain.group.entity.Team;
import org.team1.nbe1_3_team01.domain.group.entity.TeamType;
import org.team1.nbe1_3_team01.domain.group.repository.TeamRepository;
import org.team1.nbe1_3_team01.domain.group.service.response.TeamResponse;
import org.team1.nbe1_3_team01.domain.user.entity.Course;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.domain.user.fixture.UserFixture;
import org.team1.nbe1_3_team01.domain.user.repository.CourseRepository;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.ErrorCode;
import org.team1.nbe1_3_team01.global.util.Message;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SuppressWarnings("NonAsciiCharacters")
public class TeamServiceTest extends IntegrationTestSupport {

    private static final Logger log = LoggerFactory.getLogger(TeamServiceTest.class);
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    TeamService teamService;

    @BeforeEach
    void setup() {
        Course testCourse = Course.builder()
                .name("testCourse")
                .build();
        courseRepository.save(testCourse);
        User admin = UserFixture.createAdmin();
        User user = UserFixture.createUser();
        User user2 = UserFixture.createUser2();

        userRepository.save(admin);
        userRepository.save(user);
        userRepository.save(user2);
    }

    /* 프로젝트 팀 생성 관련 */
    @Test
    @WithMockUser(username = "root", roles = {"ADMIN"})
    void 프로젝트팀_생성_실패_회원PK_없음() {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(1L, "PROJECT", "projectTeam", List.of(3L, 4L, 5L), 3L);

        assertThat(
                assertThrows(AppException.class, () -> teamService.teamCreate(teamCreateRequest)).getErrorCode()
        ).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @WithMockUser(username = "root", roles = {"ADMIN"})
    void 프로젝트팀_생성_실패_코스_없음() {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(2L, "PROJECT", "projectTeam", List.of(3L), 3L);

        assertThat(
                assertThrows(AppException.class, () -> teamService.teamCreate(teamCreateRequest)).getErrorCode()
        ).isEqualTo(ErrorCode.COURSE_NOT_FOUND);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void 프로젝트팀_생성_실패_관리자만_가능() {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(1L, "PROJECT", "projectTeam", List.of(3L), 3L);
        assertThat(
                assertThrows(AppException.class, () -> teamService.teamCreate(teamCreateRequest)).getErrorCode()
        ).isEqualTo(ErrorCode.NOT_ADMIN_USER);
    }

    @Test
    @WithMockUser(username = "root", roles = {"ADMIN"})
    void 프로젝트팀_생성_성공() {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(1L, "PROJECT", "projectTeam", List.of(3L, 4L), 3L);
        Message message = teamService.teamCreate(teamCreateRequest);
        assertThat(message.getValue()).isEqualTo("1");
        Team createdTeam = teamRepository.findById(1L).get();
        assertThat(createdTeam.getName()).isEqualTo("projectTeam");
        assertThat(createdTeam.getBelongings().stream().map(Belonging::getUser).map(User::getId).toList()).contains(3L, 4L);
        assertThat(createdTeam.getTeamType()).isEqualTo(TeamType.PROJECT);
        assertThat(createdTeam.isCreationWaiting()).isEqualTo(false);
        assertThat(createdTeam.isDeletionWaiting()).isEqualTo(false);
        assertThat(createdTeam.getCourse().getId()).isEqualTo(1L);
    }

    /* 스터디 팀 생성 관련 */
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void 스터디팀_생성_실패_회원PK_없음() {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(1L, "STUDY", "studyTeam", List.of(3L, 4L, 5L), 3L);

        assertThat(
                assertThrows(AppException.class, () -> teamService.teamCreate(teamCreateRequest)).getErrorCode()
        ).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void 스터디팀_생성_실패_코스_없음() {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(2L, "STUDY", "studyTeam", List.of(3L), 3L);

        assertThat(
                assertThrows(AppException.class, () -> teamService.teamCreate(teamCreateRequest)).getErrorCode()
        ).isEqualTo(ErrorCode.COURSE_NOT_FOUND);
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void 스터디팀_생성_성공() {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(1L, "STUDY", "studyTeam", List.of(3L, 4L), 3L);
        Message message = teamService.teamCreate(teamCreateRequest);
        assertThat(message.getValue()).isEqualTo("1");
        Team createdTeam = teamRepository.findById(1L).get();
        assertThat(createdTeam.getName()).isEqualTo("studyTeam");
        assertThat(createdTeam.getBelongings().stream().map(Belonging::getUser).map(User::getId).toList()).contains(3L, 4L);
        assertThat(createdTeam.getTeamType()).isEqualTo(TeamType.STUDY);
        assertThat(createdTeam.isCreationWaiting()).isEqualTo(true);
        assertThat(createdTeam.isDeletionWaiting()).isEqualTo(false);
        assertThat(createdTeam.getCourse().getId()).isEqualTo(1L);
    }

    @Test
    @WithMockUser(username = "root", roles = {"ADMIN"})
    void 생성대기_스터디팀_목록조회_성공() {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(1L, "STUDY", "studyTeam", List.of(3L, 4L), 3L);
        teamService.teamCreate(teamCreateRequest);
        List<TeamResponse> teamResponses = teamService.creationWaitingStudyTeamList();
        assertThat(teamResponses.size()).isEqualTo(1);
        assertThat(teamResponses.get(0).getName()).isEqualTo("studyTeam");
    }

    @Test
    @WithMockUser(username = "root", roles = {"ADMIN"})
    void 스터디팀_생성승인_실패_팀없음() {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(1L, "STUDY", "studyTeam", List.of(3L, 4L), 3L);
        teamService.teamCreate(teamCreateRequest);
        assertThat(
                assertThrows(AppException.class, () -> teamService.studyTeamCreationApprove(new TeamApprovalUpdateRequest(2L))).getErrorCode()
        ).isEqualTo(ErrorCode.TEAM_NOT_FOUND);
    }

    @Test
    @WithMockUser(username = "root", roles = {"ADMIN"})
    void 스터디팀_생성승인_실패_대기중아님() {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(1L, "STUDY", "studyTeam", List.of(3L, 4L), 3L);
        teamService.teamCreate(teamCreateRequest);
        teamService.studyTeamCreationApprove(new TeamApprovalUpdateRequest(1L));
        assertThat(
                assertThrows(AppException.class, () -> teamService.studyTeamCreationApprove(new TeamApprovalUpdateRequest(1L))).getErrorCode()
        ).isEqualTo(ErrorCode.TEAM_NOT_WAITING);
    }

    @Test
    @WithMockUser(username = "root", roles = {"ADMIN"})
    void 스터디팀_생성승인_성공() {
        TeamCreateRequest teamCreateRequest = new TeamCreateRequest(1L, "STUDY", "studyTeam", List.of(3L, 4L), 3L);
        teamService.teamCreate(teamCreateRequest);
        assertThat(teamRepository.findById(1L).get().isCreationWaiting()).isEqualTo(true);
        teamService.studyTeamCreationApprove(new TeamApprovalUpdateRequest(1L));
        assertThat(teamRepository.findById(1L).get().isCreationWaiting()).isEqualTo(false);
    }

}
