package org.team1.nbe1_3_team01.domain.attendance.service

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode.ATTENDANCE_NOT_FOUND

@Component
class AttendanceReader(
    private val attendanceRepository: AttendanceRepository
) {

    @Transactional(readOnly = true)
    fun getList(userId: Long): List<Attendance> {
        return attendanceRepository.findByUserId(userId)
    }

    @Transactional(readOnly = true)
    fun get(id: Long): Attendance {
        return attendanceRepository.findById(id)
            ?: throw AppException(ATTENDANCE_NOT_FOUND)
    }
}
