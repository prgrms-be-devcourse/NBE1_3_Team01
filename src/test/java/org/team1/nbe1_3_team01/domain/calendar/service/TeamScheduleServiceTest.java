package org.team1.nbe1_3_team01.domain.calendar.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.team1.nbe1_3_team01.domain.calendar.fixture.ScheduleRequestFixture.create_TEAM_SCHEDULE_CREATE_REQUEST;
import static org.team1.nbe1_3_team01.domain.group.fixture.TeamFixture.create_PROJECT_TEAM_1;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.team1.nbe1_3_team01.domain.calendar.application.TeamScheduleService;
import org.team1.nbe1_3_team01.domain.calendar.application.port.TeamScheduleRepository;
import org.team1.nbe1_3_team01.domain.group.entity.Team;
import org.team1.nbe1_3_team01.domain.group.repository.TeamRepository;
import org.team1.nbe1_3_team01.global.exception.AppException;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class TeamScheduleServiceTest {

    @Mock
    private TeamScheduleRepository teamScheduleRepository;
    @Mock
    private TeamRepository teamRepository;
    @InjectMocks
    private TeamScheduleService teamScheduleService;

    Team team;

    @BeforeEach
    void setUp() {
        team = Mockito.spy(create_PROJECT_TEAM_1());
        lenient().when(team.getId()).thenReturn(1L);
    }

    @Test
    void 팀_일정_등록() {
        // given
        var createRequest = create_TEAM_SCHEDULE_CREATE_REQUEST();
        var teamSchedule = createRequest.toTeamSchedule(team);
        given(teamRepository.findById(any())).willReturn(Optional.of(team));
        given(teamScheduleRepository.save(any())).willReturn(teamSchedule);
        given(teamSchedule.getId()).willReturn(1L);

        // when
        var scheduleIdResponse = teamScheduleService.registSchedule(team.getId(), createRequest);

        // then
        assertThat(scheduleIdResponse.scheduleId()).isNotNull();
    }

    @Test
    void 팀_일정_등록_시_팀_정보가_없다면_예외를_발생시킨다() {
        // given
        var createRequest = create_TEAM_SCHEDULE_CREATE_REQUEST();

        // when
        when(teamRepository.findById(any())).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> teamScheduleService.registSchedule(team.getId(), createRequest))
                .isInstanceOf(AppException.class);
    }

    @Test
    void 팀_일정_수정() {

    }

    @Test
    void 팀_일정_삭제() {

    }
}
