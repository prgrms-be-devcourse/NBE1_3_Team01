package org.team1.nbe1_3_team01.domain.calendar.application

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.calendar.application.port.TeamScheduleRepository
import org.team1.nbe1_3_team01.domain.calendar.application.response.ScheduleResponse
import org.team1.nbe1_3_team01.domain.calendar.entity.TeamSchedule
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode

@Service
@Transactional(readOnly = true)
class TeamScheduleQueryService(
    private val teamScheduleRepository: TeamScheduleRepository
) {

    fun getTeamSchedules(teamId: Long): List<ScheduleResponse> {
        val teamSchedules: List<TeamSchedule> = teamScheduleRepository.findByTeamId(teamId)

        return teamSchedules
            .map { teamSchedule: TeamSchedule -> ScheduleResponse.from(teamSchedule) }
            .toList()
    }

    fun getTeamSchedule(scheduleId: Long): ScheduleResponse {
        val teamSchedule: TeamSchedule = teamScheduleRepository!!.findById(scheduleId)
            .orElseThrow { AppException(ErrorCode.SCHEDULE_NOT_FOUND) }

        return ScheduleResponse.from(teamSchedule)
    }
}
