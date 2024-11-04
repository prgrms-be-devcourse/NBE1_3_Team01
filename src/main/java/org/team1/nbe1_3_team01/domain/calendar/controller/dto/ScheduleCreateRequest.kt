package org.team1.nbe1_3_team01.domain.calendar.controller.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.team1.nbe1_3_team01.domain.calendar.entity.CourseSchedule;
import org.team1.nbe1_3_team01.domain.calendar.entity.ScheduleType;
import org.team1.nbe1_3_team01.domain.calendar.entity.TeamSchedule;
import org.team1.nbe1_3_team01.domain.group.entity.Team;
import org.team1.nbe1_3_team01.domain.user.entity.Course;

@Builder
public record ScheduleCreateRequest(
        String name,
        ScheduleType scheduleType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {

    public CourseSchedule toCourseSchedule(Course course) {
        return CourseSchedule.builder()
                .name(name)
                .startAt(startAt)
                .endAt(endAt)
                .description(description)
                .course(course)
                .build();
    }

    public TeamSchedule toTeamSchedule(Team team) {
        return TeamSchedule.builder()
                .name(name)
                .scheduleType(scheduleType)
                .startAt(startAt)
                .endAt(endAt)
                .description(description)
                .team(team)
                .build();
    }
}
