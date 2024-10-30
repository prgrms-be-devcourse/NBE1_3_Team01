package org.team1.nbe1_3_team01.domain.common.stub;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.team1.nbe1_3_team01.domain.attendance.service.port.DateTimeHolder;

public class FixedDateTimeHolder implements DateTimeHolder {

    private final LocalDateTime dateTime;
    private final LocalDate date;
    private final LocalTime time;

    public FixedDateTimeHolder(int year, int month, int day, int hour, int minute) {
        this.date = LocalDate.of(year, month, day);
        this.time = LocalTime.of(hour, minute);
        this.dateTime = LocalDateTime.of(date, time);
    }

    @Override
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    @Override
    public LocalDate getDate() {
        return date;
    }

    @Override
    public LocalTime getTime() {
        return time;
    }
}
