package org.team1.nbe1_3_team01.global.validation

import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

@PeriodCheck
data class DurationRequest(
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    val startAt: LocalDateTime,
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    val endAt: LocalDateTime
)
