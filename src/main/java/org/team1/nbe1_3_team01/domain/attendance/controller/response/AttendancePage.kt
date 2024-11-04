package org.team1.nbe1_3_team01.domain.attendance.controller.response

import org.springframework.data.domain.Page

data class AttendancePage(
    val totalPages: Long,
    val totalElements: Long,
    val currentPage: Long,
    val content: List<AttendanceResponse>
) {
    companion object {
        fun from(attendanceSlice: Page<AttendanceResponse>): AttendancePage {
            return AttendancePage(
                totalPages = attendanceSlice.totalPages.toLong(),
                totalElements = attendanceSlice.totalElements,
                currentPage = attendanceSlice.number.toLong(),
                content = attendanceSlice.content
            )
        }
    }
}
