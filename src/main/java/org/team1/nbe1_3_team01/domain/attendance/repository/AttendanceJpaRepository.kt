package org.team1.nbe1_3_team01.domain.attendance.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance

@Repository
interface AttendanceJpaRepository : JpaRepository<Attendance, Long> {

    fun findByRegistrant_UserId(userId: Long): List<Attendance>
}
