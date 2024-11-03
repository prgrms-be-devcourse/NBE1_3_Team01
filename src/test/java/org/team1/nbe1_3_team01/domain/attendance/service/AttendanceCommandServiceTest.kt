package org.team1.nbe1_3_team01.domain.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.lenient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceCreateRequest;
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceUpdateRequest;
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType;
import org.team1.nbe1_3_team01.domain.attendance.fake.AttendanceFakeRepository;
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository;
import org.team1.nbe1_3_team01.domain.attendance.service.response.AttendanceIdResponse;
import org.team1.nbe1_3_team01.domain.user.entity.Role;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_3_team01.global.exception.AppException;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings("NonAsciiCharacters")
public class AttendanceCommandServiceTest {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceReader attendanceReader;
    private final AttendanceRegistrar attendanceRegistrar;
    private final AttendanceUpdater attendanceUpdater;
    private final AttendanceDeleter attendanceDeleter;
    @Mock
    private UserRepository userRepository;

    public AttendanceCommandServiceTest() {
        this.attendanceRepository = new AttendanceFakeRepository();
        this.attendanceReader = new AttendanceReader(attendanceRepository);
        this.attendanceRegistrar = new AttendanceRegistrar(attendanceRepository);
        this.attendanceUpdater = new AttendanceUpdater(attendanceRepository);
        this.attendanceDeleter = new AttendanceDeleter(attendanceRepository);
    }

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("user")
                .password("1234")
                .email("user@gmail.com")
                .name("김철수")
                .role(Role.USER)
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);
        // stub
        lenient().when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
    }

    @Test
    void 출결_요청을_성공적으로_등록한다() {
        // given
        String registrantName = user.getUsername();
        AttendanceCreateRequest attendanceCreateRequest = AttendanceCreateRequest.builder()
                .issueType(IssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출입니다.")
                .build();

        // when
        AttendanceCommandService attendanceCommandService = new AttendanceCommandService(
                attendanceRegistrar, attendanceReader, null, null, userRepository
        );
        AttendanceIdResponse response = attendanceCommandService.register(registrantName, attendanceCreateRequest);

        // then
        assertThat(response.attendanceId()).isEqualTo(1L);
    }

    @Test
    void 출결_요청_등록_시_오늘_이미_등록했다면_예외를_발생시킨다() {
        // given
        Attendance attendance = Attendance.builder()
                .registrantId(user.getId())
                .issueType(IssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출")
                .build();
        attendanceRepository.save(attendance);

        String registrantName = user.getUsername();
        AttendanceCreateRequest attendanceCreateRequest = AttendanceCreateRequest.builder()
                .issueType(IssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출입니다.")
                .build();

        // when
        AttendanceCommandService attendanceCommandService = new AttendanceCommandService(
                attendanceRegistrar, attendanceReader, null, null, userRepository
        );

        // then
        assertThatThrownBy(() -> attendanceCommandService.register(registrantName, attendanceCreateRequest))
                .isInstanceOf(AppException.class);
    }

    @Test
    void 출결_요청을_성공적으로_수정한다() {
        // given
        Attendance attendance = Attendance.builder()
                .registrantId(user.getId())
                .issueType(IssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출")
                .build();
        attendanceRepository.save(attendance);

        Long attendanceId = 1L;
        String registrantName = user.getUsername();
        AttendanceUpdateRequest attendanceUpdateRequest = AttendanceUpdateRequest.builder()
                .id(attendanceId)
                .issueType(IssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출입니다.")
                .build();

        // when
        AttendanceCommandService attendanceCommandService = new AttendanceCommandService(
                null, attendanceReader, attendanceUpdater, null, userRepository
        );
        AttendanceIdResponse attendanceIdResponse = attendanceCommandService.update(registrantName, attendanceUpdateRequest);

        // then
        assertThat(attendanceIdResponse.attendanceId()).isEqualTo(attendanceId);
    }

    @Test
    void 출결_요청_수정_시_등록자가_아니라면_예외를_발생시킨다() {
        // given
        Attendance attendance = Attendance.builder()
                .registrantId(2L)
                .issueType(IssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출")
                .build();
        attendanceRepository.save(attendance);

        Long attendanceId = 1L;
        String registrantName = user.getUsername();
        AttendanceUpdateRequest attendanceUpdateRequest = AttendanceUpdateRequest.builder()
                .id(attendanceId)
                .issueType(IssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출입니다.")
                .build();

        // when
        AttendanceCommandService attendanceCommandService = new AttendanceCommandService(
                null, attendanceReader, attendanceUpdater, null, userRepository
        );

        // then
        assertThatThrownBy(() -> attendanceCommandService.update(registrantName, attendanceUpdateRequest))
                .isInstanceOf(AppException.class);
    }

    @Test
    void 출결_요청을_성공적으로_삭제한다() {
        // given
        Attendance attendance = Attendance.builder()
                .registrantId(user.getId())
                .issueType(IssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출")
                .build();
        attendanceRepository.save(attendance);

        String registrantName = user.getUsername();
        Long attendanceId = 1L;

        // when
        AttendanceCommandService attendanceCommandService = new AttendanceCommandService(
                null, attendanceReader, null, attendanceDeleter, userRepository
        );
        attendanceCommandService.delete(registrantName, attendanceId);

        // then
        assertThat(attendanceRepository.findById(1L).isEmpty())
                .isTrue();
    }

    @Test
    void 출결_요청_삭제_시_등록자가_아니라면_삭제할_수_없다() {
        // given
        Attendance attendance = Attendance.builder()
                .registrantId(2L)
                .issueType(IssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출")
                .build();
        attendanceRepository.save(attendance);

        String registrantName = user.getUsername();
        Long attendanceId = 1L;

        // when
        AttendanceCommandService attendanceCommandService = new AttendanceCommandService(
                null, attendanceReader, null, attendanceDeleter, userRepository
        );

        // then
        assertThatThrownBy(() -> attendanceCommandService.delete(registrantName, attendanceId))
                .isInstanceOf(AppException.class);
    }
}
