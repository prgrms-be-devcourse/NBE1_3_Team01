package org.team1.nbe1_3_team01.domain.calendar.application

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.calendar.application.port.TeamScheduleRepository
import org.team1.nbe1_3_team01.domain.calendar.application.response.ScheduleIdResponse
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleCreateRequest
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleUpdateRequest
import org.team1.nbe1_3_team01.domain.calendar.entity.TeamSchedule
import org.team1.nbe1_3_team01.domain.group.entity.Team
import org.team1.nbe1_3_team01.domain.group.repository.TeamRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode

@Service
@Transactional
class TeamScheduleCommandService(
    private val teamScheduleRepository: TeamScheduleRepository,
    private val teamRepository: TeamRepository
) {

    fun registSchedule(teamId: Long, scheduleCreateRequest: ScheduleCreateRequest): ScheduleIdResponse {
        val team: Team = teamRepository.findByIdOrNull(teamId)
            ?: throw AppException(ErrorCode.TEAM_NOT_FOUND)

        val teamSchedule = scheduleCreateRequest.toTeamSchedule(team)

        val saved = teamScheduleRepository.save(teamSchedule)
        return ScheduleIdResponse.from(saved.id)
    }

    fun updateSchedule(scheduleUpdateRequest: ScheduleUpdateRequest): ScheduleIdResponse {
        val teamSchedule: TeamSchedule = teamScheduleRepository.findByIdOrNull(scheduleUpdateRequest.id)
            ?: throw AppException(ErrorCode.SCHEDULE_NOT_FOUND)

        teamSchedule.update(
            name = scheduleUpdateRequest.name,
            scheduleType = scheduleUpdateRequest.scheduleType,
            startAt = scheduleUpdateRequest.startAt,
            endAt = scheduleUpdateRequest.endAt,
            description = scheduleUpdateRequest.description
        )

        return ScheduleIdResponse.from(teamSchedule.id)
    }

    fun deleteSchedule(scheduleId: Long) {
        val teamSchedule = teamScheduleRepository.findByIdOrNull(scheduleId)
            ?: throw AppException(ErrorCode.SCHEDULE_NOT_FOUND)

        teamScheduleRepository.delete(teamSchedule)
    }
}
