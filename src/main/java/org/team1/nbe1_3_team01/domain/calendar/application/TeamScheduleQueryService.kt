package org.team1.nbe1_3_team01.domain.calendar.application;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.SCHEDULE_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.calendar.application.port.TeamScheduleRepository;
import org.team1.nbe1_3_team01.domain.calendar.entity.TeamSchedule;
import org.team1.nbe1_3_team01.domain.calendar.application.response.ScheduleResponse;
import org.team1.nbe1_3_team01.global.exception.AppException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamScheduleQueryService {

    private final TeamScheduleRepository teamScheduleRepository;

    public List<ScheduleResponse> getTeamSchedules(Long teamId) {
        List<TeamSchedule> teamSchedules = teamScheduleRepository.findByTeamId(teamId);

        return teamSchedules.stream()
                .map(ScheduleResponse::from)
                .toList();
    }

    public ScheduleResponse getTeamSchedule(Long scheduleId) {
        TeamSchedule teamSchedule = teamScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException(SCHEDULE_NOT_FOUND));

        return ScheduleResponse.from(teamSchedule);
    }
}
