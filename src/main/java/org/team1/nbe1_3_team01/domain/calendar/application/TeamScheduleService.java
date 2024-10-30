package org.team1.nbe1_3_team01.domain.calendar.application;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.SCHEDULE_NOT_FOUND;
import static org.team1.nbe1_3_team01.global.util.ErrorCode.TEAM_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.calendar.application.port.TeamScheduleRepository;
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleCreateRequest;
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleUpdateRequest;
import org.team1.nbe1_3_team01.domain.calendar.entity.TeamSchedule;
import org.team1.nbe1_3_team01.domain.calendar.application.response.ScheduleIdResponse;
import org.team1.nbe1_3_team01.domain.group.entity.Team;
import org.team1.nbe1_3_team01.domain.group.repository.TeamRepository;
import org.team1.nbe1_3_team01.global.exception.AppException;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamScheduleService {

    private final TeamScheduleRepository teamScheduleRepository;
    private final TeamRepository teamRepository;

    public ScheduleIdResponse registSchedule(
            Long teamId,
            ScheduleCreateRequest scheduleCreateRequest
    ) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new AppException(TEAM_NOT_FOUND));

        TeamSchedule teamSchedule = scheduleCreateRequest.toTeamSchedule(team);

        TeamSchedule saved = teamScheduleRepository.save(teamSchedule);
        return ScheduleIdResponse.from(saved.getId());
    }

    public ScheduleIdResponse updateSchedule(
            ScheduleUpdateRequest scheduleUpdateRequest
    ) {
        TeamSchedule teamSchedule = teamScheduleRepository.findById(scheduleUpdateRequest.id())
                .orElseThrow(() -> new AppException(SCHEDULE_NOT_FOUND));

        teamSchedule.update(scheduleUpdateRequest);

        return ScheduleIdResponse.from(teamSchedule.getId());
    }

    public void deleteSchedule(
            Long scheduleId
    ) {
        TeamSchedule teamSchedule = teamScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException(SCHEDULE_NOT_FOUND));

        teamScheduleRepository.delete(teamSchedule);
    }
}
