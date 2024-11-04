package org.team1.nbe1_3_team01.domain.calendar.controller.dto

import org.team1.nbe1_3_team01.domain.calendar.entity.ScheduleType
import java.time.LocalDateTime

data class ScheduleUpdateRequest(
    val id: Long,
    val name: String,
    val scheduleType: ScheduleType,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val description: String
)
