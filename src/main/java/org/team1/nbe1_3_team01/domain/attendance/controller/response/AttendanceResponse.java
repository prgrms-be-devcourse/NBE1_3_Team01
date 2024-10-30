package org.team1.nbe1_3_team01.domain.attendance.controller.response;

import java.time.LocalDateTime;
import lombok.Builder;
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType;

@Builder
public record AttendanceResponse(
        Long attendanceId,
        Long userId,
        String username,
        IssueType issueType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {

}
