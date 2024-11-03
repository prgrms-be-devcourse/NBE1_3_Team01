package org.team1.nbe1_3_team01.domain.attendance.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType;
import org.team1.nbe1_3_team01.domain.attendance.fake.AttendanceFakeRepository;
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository;

@SuppressWarnings("NonAsciiCharacters")
public class AttendanceDeleterTest {

    private final AttendanceRepository attendanceRepository;
    private final AttendanceDeleter attendanceDeleter;

    public AttendanceDeleterTest() {
        this.attendanceRepository = new AttendanceFakeRepository();
        this.attendanceDeleter = new AttendanceDeleter(attendanceRepository);
    }

    @BeforeEach
    void setUp() {
        Attendance attendance = Attendance.builder()
                .registrantId(1L)
                .issueType(IssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출")
                .build();
        attendanceRepository.save(attendance);
    }

    @Test
    void 출결_요청을_성공적으로_삭제한다() {
        // given
        Attendance attendance = attendanceRepository.findById(1L).orElseThrow();

        // when
        attendanceDeleter.delete(attendance);

        // then
        Assertions.assertThat(attendanceRepository.findById(1L).isEmpty())
                .isTrue();
    }

    @Test
    void 출결_요청을_삭제할_때_id값이_유효하지_않다면_예외를_발생시킨다() {}

    @Test
    void 출결_요청을_삭제할_때_삭제할_데이터가_없다면_예외를_발생시킨다() {}
}
