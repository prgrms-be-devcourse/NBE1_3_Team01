package org.team1.nbe1_3_team01.domain.attendance.entity

import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.team1.nbe1_3_team01.global.exception.AppException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Suppress("NonAsciiCharacters")
class DurationRequestTest {

    @Test
    fun `출결_시간이_9시_이전이면_예외를_발생시킨다`() {
        // given
        val registrantId = 1L

        val startTimeErr = LocalTime.of(8, 59)
        val endTimeErr = LocalTime.of(9, 30)

        // when & then
        assertThatThrownBy {
            Attendance(
                issueType = IssueType.ABSENT,
                description = "국취제로 인한 외출",
                startAt = LocalDateTime.of(LocalDate.now(), startTimeErr),
                endAt = LocalDateTime.of(LocalDate.now(), endTimeErr),
                registrantId = registrantId)
        }.isInstanceOf(AppException::class.java)
    }

    @Test
    fun `출결_시간이_18시를_넘어가면_예외를_발생시킨다`() {
        // given
        val registrantId = 1L
        val startTimeErr = LocalTime.of(9, 10)
        val endTimeErr = LocalTime.of(18, 30)

        assertThatThrownBy {
            Attendance(
                issueType = IssueType.ABSENT,
                description = "국취제로 인한 외출",
                startAt = LocalDateTime.of(LocalDate.now(), startTimeErr),
                endAt = LocalDateTime.of(LocalDate.now(), endTimeErr),
                registrantId = registrantId)
        }.isInstanceOf(AppException::class.java)
    }
}
