package org.team1.nbe1_3_team01.domain.calendar.application.port

import org.springframework.data.jpa.repository.JpaRepository
import org.team1.nbe1_3_team01.domain.calendar.entity.TeamSchedule

interface TeamScheduleRepository : JpaRepository<TeamSchedule, Long> {

    fun findByTeamId(teamId: Long): List<TeamSchedule>
}
