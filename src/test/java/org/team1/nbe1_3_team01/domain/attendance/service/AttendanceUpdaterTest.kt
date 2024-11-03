package org.team1.nbe1_3_team01.domain.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceCreateRequest;
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceUpdateRequest;
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType;
import org.team1.nbe1_3_team01.domain.attendance.fake.AttendanceFakeRepository;

@SuppressWarnings("NonAsciiCharacters")
public class AttendanceUpdaterTest {

    private final AttendanceRegistrar attendanceRegistrar;
    private final AttendanceReader attendanceReader;
    private final AttendanceUpdater attendanceUpdater;

    public AttendanceUpdaterTest() {
        AttendanceFakeRepository attendanceFakeRepository = new AttendanceFakeRepository();
        this.attendanceRegistrar = new AttendanceRegistrar(attendanceFakeRepository);
        this.attendanceReader = new AttendanceReader(attendanceFakeRepository);
        this.attendanceUpdater = new AttendanceUpdater(attendanceFakeRepository);
    }

    @BeforeEach
    void setUp() {
        Long registrantId = 1L;
        AttendanceCreateRequest attendanceCreateRequest = AttendanceCreateRequest.builder()
                .issueType(IssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출입니다.")
                .build();
        attendanceRegistrar.register(registrantId, attendanceCreateRequest);
    }

    @Test
    void 출결_요청을_성공적으로_수정한다() {
        // given
        Long attendanceId = 1L;
        Attendance attendance = attendanceReader.get(attendanceId);
        AttendanceUpdateRequest attendanceUpdateRequest = AttendanceUpdateRequest.builder()
                .id(attendanceId)
                .issueType(IssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(13, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출입니다.")
                .build();

        // when
        Long updatedAttendanceId = attendanceUpdater.update(attendance, attendanceUpdateRequest);

        // then 수정된 값 직접 비교할 수 있다면 더 좋음
        assertThat(updatedAttendanceId).isEqualTo(attendanceId);
    }
}
