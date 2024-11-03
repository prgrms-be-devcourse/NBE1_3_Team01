package org.team1.nbe1_3_team01.domain.attendance.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType
import org.team1.nbe1_3_team01.domain.attendance.fake.AttendanceFakeRepository
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceCreateRequest
import org.team1.nbe1_3_team01.global.validation.DurationRequest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Suppress("NonAsciiCharacters")
class AttendanceRegistrarTest {

    private val attendanceRegistrar: AttendanceRegistrar = AttendanceRegistrar(
        AttendanceFakeRepository()
    )

    @Test
    fun `출결 요청을 성공적으로 저장한다`() {
        // given
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

        // when
        val attendanceId: Long = attendanceRegistrar.register(registrantId, attendanceCreateRequest)

        // then
        assertThat(attendanceId).isEqualTo(1L)
    }
}
