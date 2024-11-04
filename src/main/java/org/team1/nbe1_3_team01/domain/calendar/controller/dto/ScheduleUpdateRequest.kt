package org.team1.nbe1_3_team01.domain.calendar.controller.dto;

import java.time.LocalDateTime;
import org.team1.nbe1_3_team01.domain.calendar.entity.ScheduleType;

public record ScheduleUpdateRequest(
        Long id,
        String name,
        ScheduleType scheduleType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {
}
