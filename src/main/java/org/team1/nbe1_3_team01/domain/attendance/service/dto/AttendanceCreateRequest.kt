package org.team1.nbe1_3_team01.domain.attendance.service.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType;
import org.team1.nbe1_3_team01.global.validation.PeriodCheck;

@Builder
@PeriodCheck
public record AttendanceCreateRequest(
        @NotNull(message = "출결 상태를 선택 해야 합니다.")
        IssueType issueType,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startAt,
        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime endAt,
        String description
) {

    public Attendance toEntity(Long registrantId) {
        return Attendance.builder()
                .issueType(issueType)
                .startAt(startAt)
                .endAt(endAt)
                .description(description)
                .registrantId(registrantId)
                .build();
    }
}
