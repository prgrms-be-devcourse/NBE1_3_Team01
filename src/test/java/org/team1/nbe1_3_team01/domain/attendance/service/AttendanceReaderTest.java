package org.team1.nbe1_3_team01.domain.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType;
import org.team1.nbe1_3_team01.domain.attendance.fake.AttendanceFakeRepository;
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository;
import org.team1.nbe1_3_team01.global.exception.AppException;

@SuppressWarnings("NonAsciiCharacters")
public class AttendanceReaderTest {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceReader attendanceReader;

    public AttendanceReaderTest() {
        this.attendanceRepository = new AttendanceFakeRepository();
        this.attendanceReader = new AttendanceReader(attendanceRepository);
    }

    @BeforeEach
    void setUp() {
        Attendance attendance = Attendance.builder()
                .issueType(IssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출")
                .registrantId(1L)
                .build();
        attendanceRepository.save(attendance);
    }

    @AfterEach
    void clear() {
        attendanceRepository.deleteById(1L);
    }

    @Test
    void 출결_요청_데이터를_식별자로_조회했을_때_성공적으로_가져온다() {
        // given
        Long attendanceId = 1L;

        // when
        Attendance attendance = attendanceReader.get(attendanceId);

        // then
        assertThat(attendance.getId()).isEqualTo(attendanceId);
    }

    @Test
    void 출결_요청_데이터를_조회했을_때_데이터가_없다면_예외를_발생시킨다() {
        // given
        Long attendanceId = 2L;

        // when & then
        assertThatThrownBy(() -> attendanceReader.get(attendanceId))
                .isInstanceOf(AppException.class);
    }

    @Test
    void 출결_요청_데이터를_등록자_식별자로_조회했을_때_성공적으로_가져온다() {
        // given
        Long registrantId = 1L;

        // when
        List<Attendance> attendances = attendanceReader.getList(registrantId);

        // then
        assertThat(attendances.get(0).getRegistrant().getUserId())
                .isEqualTo(registrantId);
    }

    @Test
    void 출결_요청_데이터를_조회했을_때_데이터가_없다면_빈_리스트를_반환한다() {
        // given
        Long registrantId = 2L;

        // when
        List<Attendance> attendances = attendanceReader.getList(registrantId);

        // then
        assertThat(attendances.isEmpty()).isTrue();
    }
}
