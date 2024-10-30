package org.team1.nbe1_3_team01.domain.calendar.fixture;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.team1.nbe1_3_team01.domain.calendar.entity.ScheduleType;
import org.team1.nbe1_3_team01.domain.calendar.entity.TeamSchedule;
import org.team1.nbe1_3_team01.domain.group.entity.Team;

public final class ScheduleFixture {

    public static TeamSchedule create_TEAM_SCHEDULE_RBF(Team team) {
        return TeamSchedule.builder()
                .team(team)
                .name("1차 RBF")
                .scheduleType(ScheduleType.RBF)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(18, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(19, 0, 0)))
                .description("각자 이번 주차 때 공부한 내용 발표")
                .build();
    }
}
