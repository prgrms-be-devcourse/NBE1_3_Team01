package org.team1.nbe1_3_team01.domain.attendance.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.team1.nbe1_3_team01.global.exception.AppException;

@SuppressWarnings("NonAsciiCharacters")
public class DurationTest {

    @Test
    void 출결을_생성할_때_출결_시간_모두_9시부터_18시_사이가_아니라면_예외를_발생시킨다() {
        // given
        LocalTime startTimeErr1 = LocalTime.of(8, 59);
        LocalTime endTimeErr1 = LocalTime.of(9, 30);

        LocalTime startTimeErr2 = LocalTime.of(9, 10);
        LocalTime endTimeErr2 = LocalTime.of(18, 30);

        // when & then
        assertSoftly(softly -> {
            assertThatThrownBy(() -> Attendance.builder()
                    .startAt(LocalDateTime.of(LocalDate.now(), startTimeErr1))
                    .endAt(LocalDateTime.of(LocalDate.now(), endTimeErr1))
                    .build())
                    .isInstanceOf(AppException.class);
            assertThatThrownBy(() -> Attendance.builder()
                    .startAt(LocalDateTime.of(LocalDate.now(), startTimeErr2))
                    .endAt(LocalDateTime.of(LocalDate.now(), endTimeErr2))
                    .build())
                    .isInstanceOf(AppException.class);
        });
    }
}
