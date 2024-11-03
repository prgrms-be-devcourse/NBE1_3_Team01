package org.team1.nbe1_3_team01.domain.attendance.entity

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.springframework.test.util.ReflectionTestUtils
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceUpdateRequest
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.validation.DurationRequest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Suppress("NonAsciiCharacters")
class AttendanceTest {

    @Test
    fun `출결을_등록할_때_오늘_등록하지_않았다면_예외를_발생시킨다`() {
        // given
        val registrantId = 1L
        val attendance = Attendance(
            issueType = IssueType.ABSENT,
            description = "국취제로 인한 외출",
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0)),
            registrantId = registrantId)

        // when & then
        assertThatThrownBy { attendance.validateCanRegister() }
            .isInstanceOf(AppException::class.java)
    }

    @Test
    fun `출결_정보를_승인한다`() {
        // given
        val registrantId = 1L
        val attendance = Attendance(
            issueType = IssueType.ABSENT,
            description = "국취제로 인한 외출",
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0)),
            registrantId = registrantId)

        // when
        attendance.approve()

        // then
        assertThat(attendance.approvalState).isEqualTo(ApprovalState.APPROVED)
    }

    @Test
    fun `출결이_이미_승인되었다면_예외를_발생시킨다`() {
        // given
        val registrantId = 1L
        val attendance = Attendance(
            issueType = IssueType.ABSENT,
            description = "국취제로 인한 외출",
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0)),
            registrantId = registrantId)
        attendance.approve()

        // when & then
        assertThatThrownBy { attendance.approve() }
            .isInstanceOf(AppException::class.java)
    }

    @Test
    fun `출결이_이미_반려되었다면_예외를_발생시킨다`() {
        // given
        val registrantId = 1L
        val attendance = Attendance(
            issueType = IssueType.ABSENT,
            description = "국취제로 인한 외출",
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0)),
            registrantId = registrantId)
        attendance.reject()

        // when & then
        assertThatThrownBy { attendance.reject() }
            .isInstanceOf(AppException::class.java)
    }

    @Test
    fun `출결_상태가_Pending_상태가_아니라면_예외를_발생시킨다`() {
        // given
        val registrantId = 1L
        val attendance = Attendance(
            issueType = IssueType.ABSENT,
            description = "국취제로 인한 외출",
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0)),
            registrantId = registrantId)
        attendance.approve()

        // when & then
        assertThatThrownBy { attendance.validatePending() }
            .isInstanceOf(AppException::class.java)
    }

    @Test
    fun `출결_요청을_수정할_때_Pending_상태가_아니라면_예외를_발생시킨다`() {
        // given
        val attendanceId = 1L
        val registrantId = 1L
        val attendance = Attendance(
            issueType = IssueType.ABSENT,
            description = "국취제로 인한 외출",
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0)),
            registrantId = registrantId)
        ReflectionTestUtils.setField(attendance, "id", attendanceId)
        attendance.approve()

        // when
        val durationRequest = DurationRequest(
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)),
        )
        val attendanceUpdateRequest = AttendanceUpdateRequest(
            id = attendanceId,
            issueType = IssueType.ABSENT,
            durationRequest = durationRequest,
            description = "국취제로 인한 외출입니다."
        )

        // then
        assertThatThrownBy { attendance.update(
            attendanceUpdateRequest.issueType,
            attendanceUpdateRequest.durationRequest.startAt,
            attendanceUpdateRequest.durationRequest.endAt,
            attendanceUpdateRequest.description)
        }
            .isInstanceOf(AppException::class.java)
    }
}
