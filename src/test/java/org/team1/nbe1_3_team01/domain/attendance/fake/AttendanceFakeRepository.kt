package org.team1.nbe1_3_team01.domain.attendance.fake

import org.springframework.test.util.ReflectionTestUtils.setField
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository

class AttendanceFakeRepository : AttendanceRepository {

    private var id: Int = 0
    private val attendanceStorage: MutableList<Attendance> = mutableListOf()

    override fun findById(id: Long): Attendance? {
        if (id == 0L) {
            return null
        }
        return try {
            attendanceStorage[id.toInt() - 1]
        } catch (e: IndexOutOfBoundsException) {
            null
        }
    }

    override fun findByUserId(userId: Long): List<Attendance> {
        return attendanceStorage.filter { it.registrant.userId == userId }
    }

    override fun findAll(): List<Attendance> {
        return attendanceStorage.toList()
    }

    override fun save(attendance: Attendance): Attendance {
        val currentId = attendance.id
        return if (currentId == 0L) {
            // INSERT
            setField(attendance, "id", (this.id + 1).toLong())
            attendanceStorage.add(attendance)
            this.id += 1
            attendance
        } else {
            // UPDATE
            attendanceStorage.removeIf { it.id == currentId }
            attendanceStorage.add((currentId - 1).toInt(), attendance)
            attendance
        }
    }

    override fun deleteById(id: Long) {
        attendanceStorage.removeIf { it.id == id }
    }
}
