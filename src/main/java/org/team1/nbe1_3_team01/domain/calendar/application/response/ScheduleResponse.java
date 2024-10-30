package org.team1.nbe1_3_team01.domain.calendar.application.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.team1.nbe1_3_team01.domain.calendar.entity.CourseSchedule;
import org.team1.nbe1_3_team01.domain.calendar.entity.ScheduleType;
import org.team1.nbe1_3_team01.domain.calendar.entity.TeamSchedule;

@Builder
public record ScheduleResponse(
        Long id,
        String name,
        ScheduleType scheduleType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {

    public static ScheduleResponse from(CourseSchedule courseSchedule) {
        return ScheduleResponse.builder()
                .id(courseSchedule.getId())
                .name(courseSchedule.getName())
                .startAt(courseSchedule.getStartAt())
                .endAt(courseSchedule.getEndAt())
                .description(courseSchedule.getDescription())
                .build();
    }

    public static ScheduleResponse from(TeamSchedule teamSchedule) {
        return ScheduleResponse.builder()
                .id(teamSchedule.getId())
                .scheduleType(teamSchedule.getScheduleType())
                .name(teamSchedule.getName())
                .startAt(teamSchedule.getStartAt())
                .endAt(teamSchedule.getEndAt())
                .description(teamSchedule.getDescription())
                .build();
    }
}
