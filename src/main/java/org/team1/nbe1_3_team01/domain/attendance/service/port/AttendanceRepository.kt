package org.team1.nbe1_3_team01.domain.attendance.service.port

import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance

interface AttendanceRepository {

    fun findById(id: Long): Attendance?

    fun findByUserId(userId: Long): List<Attendance>

    fun findAll(): List<Attendance>

    fun save(attendance: Attendance): Attendance

    fun deleteById(id: Long)
}