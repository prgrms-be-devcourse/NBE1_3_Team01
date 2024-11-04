package org.team1.nbe1_3_team01.domain.attendance.entity

enum class ApprovalState {
    PENDING, APPROVED, REJECTED;

    internal fun isApproved(): Boolean = this == APPROVED

    internal fun isPending(): Boolean = this == PENDING

    internal fun isRejected(): Boolean = this == REJECTED
}
