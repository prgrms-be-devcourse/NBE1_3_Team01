package org.team1.nbe1_2_team01.domain.attendance.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType
import org.team1.nbe1_3_team01.domain.attendance.fake.AttendanceFakeRepository
import org.team1.nbe1_3_team01.domain.attendance.service.AttendanceReader
import org.team1.nbe1_3_team01.domain.attendance.service.AttendanceRegistrar
import org.team1.nbe1_3_team01.domain.attendance.service.AttendanceUpdater
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceCreateRequest
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceUpdateRequest
import org.team1.nbe1_3_team01.global.validation.DurationRequest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Suppress("NonAsciiCharacters")
class AttendanceUpdaterTest {

    private val attendanceRegistrar: AttendanceRegistrar
    private val attendanceReader: AttendanceReader
    private val attendanceUpdater: AttendanceUpdater

    init {
        val attendanceFakeRepository = AttendanceFakeRepository()
        this.attendanceRegistrar = AttendanceRegistrar(attendanceFakeRepository)
        this.attendanceReader = AttendanceReader(attendanceFakeRepository)
        this.attendanceUpdater = AttendanceUpdater(attendanceFakeRepository)
    }

    @BeforeEach
    fun setUp() {
        val registrantId = 1L
        val durationRequest = DurationRequest(
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)),
        )
        val attendanceCreateRequest = AttendanceCreateRequest(
            issueType = IssueType.ABSENT,
            durationRequest = durationRequest,
            description = "국취제로 인한 외출입니다."
        )
        attendanceRegistrar.register(registrantId, attendanceCreateRequest)
    }

    @Test
    fun `출결 요청을 성공적으로 수정한다`() {
        // given
        val attendanceId = 1L
        val attendance: Attendance = attendanceReader.get(attendanceId)
        val attendanceUpdateRequest = AttendanceUpdateRequest(
            id = attendanceId,
            issueType = IssueType.ABSENT,
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0, 0)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)),
            description = "국취제로 인한 외출입니다."
        )

        // when
        attendance.update(attendanceUpdateRequest)
        val updatedAttendanceId = attendanceUpdater.update(attendance)

        // then
        assertThat(updatedAttendanceId).isEqualTo(attendanceId)

        // 추가 검증: 수정된 값이 올바르게 반영되었는지 확인
        val updatedAttendance: Attendance = attendanceReader.get(attendanceId)
        assertThat(updatedAttendance.startAt).isEqualTo(LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0, 0)))
        assertThat(updatedAttendance.description).isEqualTo("국취제로 인한 외출입니다.")
    }
}
