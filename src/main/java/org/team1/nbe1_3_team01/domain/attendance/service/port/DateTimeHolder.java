package org.team1.nbe1_3_team01.domain.attendance.service.port;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public interface DateTimeHolder {

    LocalDateTime getDateTime();

    LocalDate getDate();

    LocalTime getTime();
}
