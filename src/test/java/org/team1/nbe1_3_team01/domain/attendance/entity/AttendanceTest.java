package org.team1.nbe1_3_team01.domain.attendance.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.team1.nbe1_3_team01.global.exception.AppException;

@SuppressWarnings("NonAsciiCharacters")
public class AttendanceTest {

    @Test
    void 출결_정보를_승인한다() {
        // given
        // 승인과 관련없는 정보라도, 객체 생성 시 validation이 필요한 데이터는 넣는다.
        Attendance attendance = Attendance.builder()
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .build();

        // when
        attendance.approve();

        // then
        assertThat(attendance.getApprovalState()).isEqualTo(ApprovalState.APPROVED);
    }

    @Test
    void 출결이_이미_승인되었다면_예외를_발생시킨다() {
        // given
        Attendance attendance = Attendance.builder()
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .build();
        attendance.approve();

        // when & then
        assertThatThrownBy(attendance::approve)
                .isInstanceOf(AppException.class);
    }

    @Test
    void 출결을_등록할_수_없다면_예외를_발생시킨다() {

    }
}
