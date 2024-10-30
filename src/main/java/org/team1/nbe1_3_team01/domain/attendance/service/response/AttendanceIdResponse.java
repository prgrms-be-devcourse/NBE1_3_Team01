package org.team1.nbe1_3_team01.domain.attendance.service.response;

import lombok.Builder;

@Builder
public record AttendanceIdResponse(
        Long attendanceId
) {

    public static AttendanceIdResponse from(Long attendanceId) {
        return AttendanceIdResponse.builder()
                .attendanceId(attendanceId)
                .build();
    }
}
