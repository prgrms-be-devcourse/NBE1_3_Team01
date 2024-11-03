package org.team1.nbe1_3_team01.domain.attendance.entity;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.ATTENDANCE_TIME_END_BEFORE_START;
import static org.team1.nbe1_3_team01.global.util.ErrorCode.ATTENDANCE_TIME_OUT_OF_RANGE;

import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.team1.nbe1_3_team01.global.exception.AppException;

@Embeddable
@EqualsAndHashCode
@Getter
public class Duration {

    private final LocalDateTime startAt;
    private final LocalDateTime endAt;

    // for JPA
    protected Duration() {
        startAt = null;
        endAt = null;
    }

    public Duration(LocalDateTime startAt, LocalDateTime endAt) {
        validateDuration(startAt, endAt);
        validateTime(startAt);
        validateTime(endAt);

        this.startAt = startAt;
        this.endAt = endAt;
    }

    private void validateDuration(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt.isAfter(endAt)) {
            throw new AppException(ATTENDANCE_TIME_END_BEFORE_START);
        }
    }

    private void validateTime(LocalDateTime time) {
        LocalTime date = time.toLocalTime();

        if (date.isBefore(LocalTime.of(9, 0, 0))
                || date.isAfter(LocalTime.of(18, 0, 1))) {
            throw new AppException(ATTENDANCE_TIME_OUT_OF_RANGE);
        }
    }

    protected boolean isRegisteredToday() {
        LocalDate startAtLocalDate = startAt.toLocalDate();
        LocalDate endAtLocalDate = endAt.toLocalDate();
        return startAtLocalDate.isEqual(LocalDate.now()) || endAtLocalDate.isEqual(LocalDate.now());
    }
}
