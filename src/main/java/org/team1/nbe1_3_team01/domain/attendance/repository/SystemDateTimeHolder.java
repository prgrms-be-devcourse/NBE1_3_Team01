package org.team1.nbe1_3_team01.domain.attendance.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.springframework.stereotype.Component;
import org.team1.nbe1_3_team01.domain.attendance.service.port.DateTimeHolder;

@Component
public class SystemDateTimeHolder implements DateTimeHolder {

    @Override
    public LocalDateTime getDateTime() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDate getDate() {
        return LocalDate.now();
    }

    @Override
    public LocalTime getTime() {
        return LocalTime.now();
    }
}
