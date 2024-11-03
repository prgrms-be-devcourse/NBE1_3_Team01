package org.team1.nbe1_3_team01.domain.attendance.service.port

import org.team1.nbe1_3_team01.domain.attendance.controller.response.AttendanceResponse

interface AttendancePersistence {

    fun findAll(): List<AttendanceResponse>

    fun findByUsername(username: String): List<AttendanceResponse>

    fun findById(id: Long): AttendanceResponse?

    fun findByIdAndUsername(id: Long, username: String): AttendanceResponse?
}
