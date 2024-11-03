package org.team1.nbe1_3_team01.domain.attendance.service

import org.springframework.stereotype.Component
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode

@Component
class AttendanceDeleter(
    private val attendanceRepository: AttendanceRepository
) {

    fun delete(attendance: Attendance) {
        if (attendance.id == 0L) {
            throw AppException(ErrorCode.ATTENDANCE_NOT_FOUND)
        }

        attendanceRepository.deleteById(attendance.id)
    }
}
