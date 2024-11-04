package org.team1.nbe1_3_team01.domain.calendar.application.response

data class ScheduleIdResponse(
    val scheduleId: Long
) {
    companion object {
        fun from(scheduleId: Long): ScheduleIdResponse {
            return ScheduleIdResponse(
                scheduleId = scheduleId
            )
        }
    }
}
