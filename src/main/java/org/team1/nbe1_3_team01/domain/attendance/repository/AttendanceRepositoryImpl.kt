package org.team1.nbe1_3_team01.domain.attendance.repository

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository

@Repository
class AttendanceRepositoryImpl(
    private val attendanceJpaRepository: AttendanceJpaRepository
) : AttendanceRepository {

    override fun findById(id: Long): Attendance? {
        return attendanceJpaRepository.findByIdOrNull(id)
    }

    override fun findByUserId(userId: Long): List<Attendance> {
        return attendanceJpaRepository.findByRegistrant_UserId(userId)
    }

    override fun findAll(): List<Attendance> {
        return attendanceJpaRepository.findAll()
    }

    override fun save(attendance: Attendance): Attendance {
        return attendanceJpaRepository.save(attendance)
    }

    override fun deleteById(id: Long) {
        attendanceJpaRepository.deleteById(id)
    }
}
