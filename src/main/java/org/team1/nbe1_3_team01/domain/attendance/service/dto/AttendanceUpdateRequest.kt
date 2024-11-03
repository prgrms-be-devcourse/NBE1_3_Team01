package org.team1.nbe1_3_team01.domain.attendance.service.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType;

@Builder
public record AttendanceUpdateRequest(
        Long id,
        IssueType issueType,
        LocalDateTime startAt,
        LocalDateTime endAt,
        String description
) {

}
