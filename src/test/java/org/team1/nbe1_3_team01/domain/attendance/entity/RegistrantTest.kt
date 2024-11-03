package org.team1.nbe1_3_team01.domain.attendance.entity

import org.assertj.core.api.Assertions.assertThatCode
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.team1.nbe1_3_team01.global.exception.AppException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Suppress("NonAsciiCharacters")
class RegistrantTest {

    @Test
    fun `출결_요청을_등록한_사람인지_검증한다`() {
        // given
        val registrantId = 1L
        val attendance = Attendance(
            issueType = IssueType.ABSENT,
            description = "국취제로 인한 외출",
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 30)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 30)),
            registrantId = registrantId)

        // when
        val userId = 1L

        // then
        assertThatCode { attendance.validateRegistrant(userId) }
            .doesNotThrowAnyException()
    }

    @Test
    fun `출결_요청을_등록한_사람이_아닐_때_예외를_발생시킨다`() {
        // given
        val registrantId = 1L
        val attendance = Attendance(
            issueType = IssueType.ABSENT,
            description = "국취제로 인한 외출",
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(9, 30)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(11, 30)),
            registrantId = registrantId)

        // when
        val anotherUserId = 2L

        // then
        assertThatThrownBy { attendance.validateRegistrant(anotherUserId) }
            .isInstanceOf(AppException::class.java)
    }
}
