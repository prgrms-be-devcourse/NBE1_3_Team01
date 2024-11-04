package org.team1.nbe1_3_team01.domain.attendance.service.response

data class AttendanceIdResponse(
    val attendanceId: Long
) {
    companion object {
        fun from(attendanceId: Long): AttendanceIdResponse {
            return AttendanceIdResponse (
                attendanceId = attendanceId
            )
        }
    }
}
