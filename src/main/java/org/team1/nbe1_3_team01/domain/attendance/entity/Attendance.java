package org.team1.nbe1_3_team01.domain.attendance.entity;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.REQUEST_ALREADY_APPROVED;
import static org.team1.nbe1_3_team01.global.util.ErrorCode.REQUEST_ALREADY_EXISTS;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceUpdateRequest;
import org.team1.nbe1_3_team01.global.exception.AppException;

@Entity
@Getter
@Table(name = "attendance")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "userId", column = @Column(name = "user_id"))
    private Registrant registrant;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private IssueType issueType;

    private String description;

    @Enumerated(EnumType.STRING)
    private ApprovalState approvalState;

    @Embedded
    private Duration duration;

    @Builder
    public Attendance(
            IssueType issueType,
            LocalDateTime startAt,
            LocalDateTime endAt,
            String description,
            Long registrantId
    ) {
        this.issueType = issueType;
        this.description = description;
        this.approvalState = ApprovalState.PENDING;
        this.duration = new Duration(startAt, endAt);
        this.registrant = new Registrant(registrantId);
    }

    // @TODO : update 로직 분리
    public void update(AttendanceUpdateRequest attendanceUpdateRequest) {
        if (!isPending()) {
            throw new AppException(REQUEST_ALREADY_APPROVED);
        }
        this.issueType = attendanceUpdateRequest.issueType();
        this.duration = new Duration(attendanceUpdateRequest.startAt(), attendanceUpdateRequest.endAt());
        this.description = attendanceUpdateRequest.description();
    }

    private boolean isPending() {
        return approvalState.equals(ApprovalState.PENDING);
    }

    public void approve() {
        if (isApprove()) {
            throw new AppException(REQUEST_ALREADY_APPROVED);
        }
        approvalState = ApprovalState.APPROVED;
    }

    private boolean isApprove() {
        return approvalState.equals(ApprovalState.APPROVED);
    }

    public void validateRegistrant(Long currentUserId) {
        registrant.validateRegistrant(currentUserId);
    }

    public void validateCanRegister() {
        if (isPending() && duration.isRegisteredToday()) {
            throw new AppException(REQUEST_ALREADY_EXISTS);
        }
    }
}
