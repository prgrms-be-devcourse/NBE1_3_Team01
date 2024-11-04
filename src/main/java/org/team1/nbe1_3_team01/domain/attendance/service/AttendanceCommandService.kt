package org.team1.nbe1_3_team01.domain.attendance.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceCreateRequest
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceUpdateRequest
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance
import org.team1.nbe1_3_team01.domain.attendance.service.response.AttendanceIdResponse
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode.USER_NOT_FOUND

@Service
@Transactional
class AttendanceCommandService(
    private val attendanceRegistrar: AttendanceRegistrar,
    private val attendanceReader: AttendanceReader,
    private val attendanceUpdater: AttendanceUpdater,
    private val attendanceDeleter: AttendanceDeleter,
    private val userRepository: UserRepository
) {

    fun register(
        registrantName: String,
        attendanceCreateRequest: AttendanceCreateRequest
    ): AttendanceIdResponse {
        // validate
        val registrant: User = getCurrentUser(registrantName)
        attendanceReader.getList(registrant.id)
            .forEach { it.validateCanRegister() }

        // register
        val attendanceId: Long = attendanceRegistrar.register(registrant.id, attendanceCreateRequest)

        // parse to response
        return AttendanceIdResponse.from(attendanceId)
    }

    fun update(
        currentUsername: String,
        attendanceUpdateRequest: AttendanceUpdateRequest
    ): AttendanceIdResponse {
        // validate
        val currentUser: User = getCurrentUser(currentUsername)
        val attendance: Attendance = attendanceReader.get(attendanceUpdateRequest.id)
        attendance.validateRegistrant(currentUser.id)
        attendance.validatePending()

        // update
        attendance.update(
            attendanceUpdateRequest.issueType,
            attendanceUpdateRequest.durationRequest.startAt,
            attendanceUpdateRequest.durationRequest.endAt,
            attendanceUpdateRequest.description
        )
        val attendanceId: Long = attendanceUpdater.update(attendance)

        // parse to response
        return AttendanceIdResponse.from(attendanceId)
    }

    fun delete(
        currentUsername: String,
        attendanceId: Long
    ) {
        // validate
        val currentUser: User = getCurrentUser(currentUsername)

        val attendance: Attendance = attendanceReader.get(attendanceId)
        attendance.validateRegistrant(currentUser.id)
        attendance.validatePending()

        // delete
        attendanceDeleter.delete(attendance)
    }

    // 타 도메인 메서드
    private fun getCurrentUser(username: String): User {
        return userRepository.findByUsername(username)
            ?: throw AppException(USER_NOT_FOUND)
    }
}
