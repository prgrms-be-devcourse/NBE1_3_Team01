package org.team1.nbe1_3_team01.domain.attendance.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType
import org.team1.nbe1_3_team01.domain.attendance.fake.AttendanceFakeRepository
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Suppress("NonAsciiCharacters")
class AttendanceDeleterTest {

    private val attendanceRepository: AttendanceRepository = AttendanceFakeRepository()
    private val attendanceDeleter: AttendanceDeleter = AttendanceDeleter(attendanceRepository)

    @BeforeEach
    fun setUp() {
        val attendance = Attendance(
            registrantId = 1L,
            issueType = IssueType.ABSENT,
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)),
            description = "국취제로 인한 외출"
        )
        attendanceRepository.save(attendance)
    }

    @Test
    fun `출결 요청을 성공적으로 삭제한다`() {
        // given
        val attendance = attendanceRepository.findById(1L)!!

        // when
        attendanceDeleter.delete(attendance)

        // then
        val deletedAttendance = attendanceRepository.findById(1L)
        assertThat(deletedAttendance).isNull()
    }
}
