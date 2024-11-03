package org.team1.nbe1_3_team01.domain.attendance.entity;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.team1.nbe1_3_team01.global.exception.AppException;

@SuppressWarnings("NonAsciiCharacters")
public class RegistrantTest {

    @Test
    void 출결_요청을_등록한_사람인지_검증한다() {
        // given
        Long registrantId = 1L;
        Attendance attendance = Attendance.builder()
                .registrantId(registrantId)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .build();

        // when
        Long userId = 1L;

        // then
        assertThatCode(() -> attendance.validateRegistrant(userId))
                .doesNotThrowAnyException();
    }

    @Test
    void 출결_요청을_등록한_사람이_아닐_때_예외를_발생시킨다() {
        // given
        Long registrantId = 1L;
        Attendance attendance = Attendance.builder()
                .registrantId(registrantId)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .build();

        // when
        var anotherUserId = 2L;

        // then
        assertThatThrownBy(() -> attendance.validateRegistrant(anotherUserId))
                .isInstanceOf(AppException.class);
    }
}
