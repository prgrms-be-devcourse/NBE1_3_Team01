package org.team1.nbe1_3_team01.domain.attendance.service

import org.springframework.stereotype.Component
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceCreateRequest
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository

@Component
class AttendanceRegistrar(
    private val attendanceRepository: AttendanceRepository
) {

    fun register(
        registrantId: Long,
        attendanceCreateRequest: AttendanceCreateRequest
    ): Long {
        val attendance = attendanceCreateRequest.toEntity(registrantId)
        val savedAttendance = attendanceRepository.save(attendance)

        return savedAttendance.id
    }
}
