package org.team1.nbe1_3_team01.domain.attendance.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.team1.nbe1_3_team01.domain.attendance.controller.response.AttendancePage
import org.team1.nbe1_3_team01.domain.attendance.controller.response.AttendanceResponse
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendancePersistence
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode.ATTENDANCE_NOT_FOUND

@Service
class AttendanceQueryService(
    private val attendancePersistence: AttendancePersistence
) {

    fun getAll(pageable: Pageable): AttendancePage {
        val responses: Page<AttendanceResponse> = attendancePersistence.findAll(pageable)
        return AttendancePage.from(responses)
    }

    fun getStudentAttendances(pageable: Pageable): AttendancePage {
        val responses: Page<AttendanceResponse> = attendancePersistence.findStudentAttendances(pageable);
        return AttendancePage.from(responses)
    }

    fun getMyAttendances(pageable: Pageable, currentUsername: String): AttendancePage {
        val responses: Page<AttendanceResponse> = attendancePersistence.findByUsername(pageable, currentUsername)
        return AttendancePage.from(responses)
    }

    fun getById(id: Long): AttendanceResponse =
        attendancePersistence.findById(id) ?: throw AppException(ATTENDANCE_NOT_FOUND)

    fun getByIdAndUserId(attendanceId: Long, currentUsername: String): AttendanceResponse =
        attendancePersistence.findByIdAndUsername(attendanceId, currentUsername)
            ?: throw AppException(ATTENDANCE_NOT_FOUND)
}