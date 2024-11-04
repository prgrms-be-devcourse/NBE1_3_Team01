package org.team1.nbe1_3_team01.domain.attendance.service

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance
import org.team1.nbe1_3_team01.domain.attendance.service.response.AttendanceIdResponse


@Component
class AttendanceApprover(
    private val attendanceReader: AttendanceReader,
    private val attendanceUpdater: AttendanceUpdater
) {

    @Transactional
    fun approve(attendanceId: Long): AttendanceIdResponse {
        val attendance: Attendance = attendanceReader.get(attendanceId)

        attendance.approve()

        return AttendanceIdResponse.from(attendance.id)
    }

    @Transactional
    fun reject(attendanceId: Long) {
        val attendance: Attendance = attendanceReader.get(attendanceId)

        attendance.reject()

        attendanceUpdater.update(attendance)
    }
}
