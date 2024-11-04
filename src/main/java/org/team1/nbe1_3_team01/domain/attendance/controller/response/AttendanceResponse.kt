package org.team1.nbe1_3_team01.domain.attendance.controller.response

import org.team1.nbe1_3_team01.domain.attendance.entity.ApprovalState
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType
import java.time.LocalDateTime

data class AttendanceResponse(
    val attendanceId: Long,
    val userId: Long,
    val username: String,
    val state: ApprovalState,
    val issueType: IssueType,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime,
    val description: String
)
