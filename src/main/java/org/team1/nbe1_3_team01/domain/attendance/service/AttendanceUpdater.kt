package org.team1.nbe1_3_team01.domain.attendance.service

import org.springframework.stereotype.Component
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository

@Component
class AttendanceUpdater(
    private val attendanceRepository: AttendanceRepository
) {

    fun update(attendance: Attendance): Long {
        val saved = attendanceRepository.save(attendance)

        return saved.id
    }
}
