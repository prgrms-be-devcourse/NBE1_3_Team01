package org.team1.nbe1_3_team01.domain.attendance.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType
import org.team1.nbe1_3_team01.domain.attendance.fake.AttendanceFakeRepository
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Suppress("NonAsciiCharacters")
class AttendanceReaderTest {

    private val attendanceRepository: AttendanceRepository = AttendanceFakeRepository()
    private val attendanceReader: AttendanceReader = AttendanceReader(attendanceRepository)

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

    @AfterEach
    fun clear() {
        attendanceRepository.deleteById(1L)
    }

    @Test
    fun `출결 요청 데이터를 식별자로 조회했을 때 성공적으로 가져온다`() {
        // given
        val attendanceId = 1L

        // when
        val attendance = attendanceReader.get(attendanceId)

        // then
        assertThat(attendance.id).isEqualTo(attendanceId)
    }

    @Test
    fun `출결 요청 데이터를 조회했을 때 데이터가 없다면 예외를 발생시킨다`() {
        // given
        val attendanceId = 2L

        // when & then
        assertThatThrownBy { attendanceReader.get(attendanceId) }
            .isInstanceOf(AppException::class.java)
    }

    @Test
    fun `출결 요청 데이터를 등록자 식별자로 조회했을 때 성공적으로 가져온다`() {
        // given
        val registrantId = 1L

        // when
        val attendances: List<Attendance> = attendanceReader.getList(registrantId)

        // then
        assertThat(attendances[0].registrant.userId).isEqualTo(registrantId)
    }

    @Test
    fun `출결 요청 데이터를 조회했을 때 데이터가 없다면 빈 리스트를 반환한다`() {
        // given
        val registrantId = 2L

        // when
        val attendances: List<Attendance> = attendanceReader.getList(registrantId)

        // then
        assertThat(attendances).isEmpty()
    }
}
