package org.team1.nbe1_3_team01.domain.calendar.application.response;

import lombok.Builder;

@Builder
public record ScheduleIdResponse(
        Long scheduleId
) {

    public static ScheduleIdResponse from(Long scheduleId) {
        return ScheduleIdResponse.builder()
                .scheduleId(scheduleId)
                .build();
    }
}
