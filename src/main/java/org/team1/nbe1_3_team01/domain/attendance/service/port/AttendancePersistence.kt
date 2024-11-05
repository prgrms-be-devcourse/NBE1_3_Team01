package org.team1.nbe1_3_team01.domain.attendance.service.port

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.team1.nbe1_3_team01.domain.attendance.controller.response.AttendanceResponse

interface AttendancePersistence {

    fun findAll(pageable: Pageable): Page<AttendanceResponse>

    fun findStudentAttendances(pageable: Pageable): Page<AttendanceResponse>

    fun findByUsername(pageable: Pageable, username: String): Page<AttendanceResponse>

    fun findById(id: Long): AttendanceResponse?

    fun findByIdAndUsername(id: Long, username: String): AttendanceResponse?
}
