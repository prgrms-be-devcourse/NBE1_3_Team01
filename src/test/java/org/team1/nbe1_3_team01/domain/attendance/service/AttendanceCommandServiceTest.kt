package org.team1.nbe1_3_team01.domain.attendance.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.lenient
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.test.util.ReflectionTestUtils
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType
import org.team1.nbe1_3_team01.domain.attendance.fake.AttendanceFakeRepository
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceCreateRequest
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceUpdateRequest
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository
import org.team1.nbe1_3_team01.domain.attendance.service.response.AttendanceIdResponse
import org.team1.nbe1_3_team01.domain.user.entity.Course
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.validation.DurationRequest
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@ExtendWith(MockitoExtension::class)
@Suppress("NonAsciiCharacters")
class AttendanceCommandServiceTest {

    private val attendanceRepository: AttendanceRepository = AttendanceFakeRepository()
    private val attendanceReader: AttendanceReader = AttendanceReader(attendanceRepository)
    private val attendanceRegistrar: AttendanceRegistrar = AttendanceRegistrar(attendanceRepository)
    private val attendanceUpdater: AttendanceUpdater = AttendanceUpdater(attendanceRepository)
    private val attendanceDeleter: AttendanceDeleter = AttendanceDeleter(attendanceRepository)

    @Mock
    lateinit var userRepository: UserRepository

    lateinit var user: User

    @BeforeEach
    fun setUp() {
        user = User.ofUser(
            username = "user",
            password = "1234",
            email = "user@gmail.com",
            name = "김철수",
            course = Mockito.mock(Course::class.java)
        )
        ReflectionTestUtils.setField(user, "id", 1L)

        // Mockito lenient stubbing
        lenient().`when`(userRepository.findByUsername(user.username)).thenReturn(user)
    }

    @Test
    fun `출결 요청을 성공적으로 등록한다`() {
        // given
        val registrantName = user.username
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
        val attendanceCommandService = AttendanceCommandService(
            attendanceRegistrar = attendanceRegistrar,
            attendanceReader = attendanceReader,
            attendanceUpdater = attendanceUpdater,
            attendanceDeleter = attendanceDeleter,
            userRepository = userRepository
        )
        val response: AttendanceIdResponse = attendanceCommandService.register(registrantName, attendanceCreateRequest)

        // then
        assertThat(response.attendanceId).isEqualTo(1L)
    }

    @Test
    fun `출결 요청 등록 시 오늘 이미 등록했다면 예외를 발생시킨다`() {
        // given
        val attendance = Attendance(
            registrantId = user.id!!,
            issueType = IssueType.ABSENT,
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)),
            description = "국취제로 인한 외출"
        )
        attendanceRepository.save(attendance)

        val registrantName = user.username
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
        val attendanceCommandService = AttendanceCommandService(
            attendanceRegistrar = attendanceRegistrar,
            attendanceReader = attendanceReader,
            attendanceUpdater = attendanceUpdater,
            attendanceDeleter = attendanceDeleter,
            userRepository = userRepository
        )

        // then
        assertThatThrownBy { attendanceCommandService.register(registrantName, attendanceCreateRequest) }
            .isInstanceOf(AppException::class.java)
    }

    @Test
    fun `출결 요청을 성공적으로 수정한다`() {
        // given
        val attendance = Attendance(
            registrantId = user.id!!,
            issueType = IssueType.ABSENT,
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)),
            description = "국취제로 인한 외출"
        )
        attendanceRepository.save(attendance)

        val attendanceId = 1L
        val registrantName = user.username
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

        // when
        val attendanceCommandService = AttendanceCommandService(
            attendanceRegistrar = attendanceRegistrar,
            attendanceReader = attendanceReader,
            attendanceUpdater = attendanceUpdater,
            attendanceDeleter = attendanceDeleter,
            userRepository = userRepository
        )
        val attendanceIdResponse: AttendanceIdResponse = attendanceCommandService.update(registrantName, attendanceUpdateRequest)

        // then
        assertThat(attendanceIdResponse.attendanceId).isEqualTo(attendanceId)

        val updatedAttendance: Attendance = attendanceReader.get(attendanceId)
        assertThat(updatedAttendance.duration.startAt).isEqualTo(LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0, 0)))
        assertThat(updatedAttendance.description).isEqualTo("국취제로 인한 외출입니다.")
    }

    @Test
    fun `출결 요청 수정 시 등록자가 아니라면 예외를 발생시킨다`() {
        // given
        val attendance = Attendance(
            registrantId = 2L, // 다른 사용자 ID
            issueType = IssueType.ABSENT,
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)),
            description = "국취제로 인한 외출"
        )
        attendanceRepository.save(attendance)

        val attendanceId = 1L
        val registrantName = user.username
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

        // when
        val attendanceCommandService = AttendanceCommandService(
            attendanceRegistrar = attendanceRegistrar,
            attendanceReader = attendanceReader,
            attendanceUpdater = attendanceUpdater,
            attendanceDeleter = attendanceDeleter,
            userRepository = userRepository
        )

        // then
        assertThatThrownBy { attendanceCommandService.update(registrantName, attendanceUpdateRequest) }
            .isInstanceOf(AppException::class.java)
    }

    @Test
    fun `출결 요청을 성공적으로 삭제한다`() {
        // given
        val attendance = Attendance(
            registrantId = user.id!!,
            issueType = IssueType.ABSENT,
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)),
            description = "국취제로 인한 외출"
        )
        attendanceRepository.save(attendance)

        val registrantName = user.username
        val attendanceId = 1L

        // when
        val attendanceCommandService = AttendanceCommandService(
            attendanceRegistrar = attendanceRegistrar,
            attendanceReader = attendanceReader,
            attendanceUpdater = attendanceUpdater,
            attendanceDeleter = attendanceDeleter,
            userRepository = userRepository
        )
        attendanceCommandService.delete(registrantName, attendanceId)

        // then
        assertThat(attendanceRepository.findById(attendanceId)).isNull()
    }

    @Test
    fun `출결 요청 삭제 시 등록자가 아니라면 삭제할 수 없다`() {
        // given
        val attendance = Attendance(
            registrantId = 2L, // 다른 사용자 ID
            issueType = IssueType.ABSENT,
            startAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)),
            endAt = LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)),
            description = "국취제로 인한 외출"
        )
        attendanceRepository.save(attendance)

        val registrantName = user.username
        val attendanceId = 1L

        // when
        val attendanceCommandService = AttendanceCommandService(
            attendanceRegistrar = attendanceRegistrar,
            attendanceReader = attendanceReader,
            attendanceUpdater = attendanceUpdater,
            attendanceDeleter = attendanceDeleter,
            userRepository = userRepository
        )

        // then
        assertThatThrownBy { attendanceCommandService.delete(registrantName, attendanceId) }
            .isInstanceOf(AppException::class.java)
    }
}
