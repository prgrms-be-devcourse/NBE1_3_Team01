package org.team1.nbe1_3_team01.domain.calendar.fixture;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleCreateRequest;
import org.team1.nbe1_3_team01.domain.calendar.entity.ScheduleType;

public final class ScheduleRequestFixture {

    // team schedule
    public static ScheduleCreateRequest create_TEAM_SCHEDULE_CREATE_REQUEST() {
        return ScheduleCreateRequest.builder()
                .name("1차 RBF")
                .scheduleType(ScheduleType.RBF)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 0, 0)))
                .description("각자 이번 주차 때 공부한 내용 발표")
                .build();
    }
}
