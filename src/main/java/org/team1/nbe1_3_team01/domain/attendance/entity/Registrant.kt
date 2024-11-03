package org.team1.nbe1_3_team01.domain.attendance.entity

import jakarta.persistence.Embeddable
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode.ATTENDANCE_ACCESS_DENIED

@Embeddable
class Registrant(
    val userId: Long
) {

    internal fun validateRegistrant(currentUserId: Long) {
        if (currentUserId != userId) {
            throw AppException(ATTENDANCE_ACCESS_DENIED)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Registrant) return false

        return userId == other.userId
    }

    override fun hashCode(): Int {
        return userId.hashCode()
    }
}
