package org.team1.nbe1_3_team01.domain.attendance.entity

import jakarta.persistence.Embeddable
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode.ATTENDANCE_TIME_END_BEFORE_START
import org.team1.nbe1_3_team01.global.util.ErrorCode.ATTENDANCE_TIME_OUT_OF_RANGE

@Embeddable
class Duration(
    val startAt: LocalDateTime,
    val endAt: LocalDateTime
) {

    init {
        validateDuration(startAt, endAt)
        validateTime(startAt)
        validateTime(endAt)
    }

    private fun validateDuration(startAt: LocalDateTime, endAt: LocalDateTime) {
        if (startAt.isAfter(endAt)) {
            throw AppException(ATTENDANCE_TIME_END_BEFORE_START)
        }
    }

    private fun validateTime(time: LocalDateTime) {
        val localTime = time.toLocalTime()
        if (localTime.isBefore(LocalTime.of(9, 0, 0)) || localTime.isAfter(LocalTime.of(18, 0, 1))) {
            throw AppException(ATTENDANCE_TIME_OUT_OF_RANGE)
        }
    }

    fun isRegisteredToday(): Boolean {
        val today = LocalDate.now()
        return startAt.toLocalDate() == today || endAt.toLocalDate() == today
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Duration) return false

        if (startAt != other.startAt) return false
        if (endAt != other.endAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = startAt.hashCode()
        result = 31 * result + endAt.hashCode()
        return result
    }
}
