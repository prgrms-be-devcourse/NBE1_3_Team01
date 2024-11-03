package org.team1.nbe1_3_team01.domain.attendance.entity

import jakarta.persistence.*
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode.*
import java.time.LocalDateTime

@Entity
@Table(name = "attendance")
class Attendance private constructor(

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    var issueType: IssueType,

    @Column(name = "description")
    var description: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_state")
    var approvalState: ApprovalState = ApprovalState.PENDING,

    @Embedded
    var duration: Duration,

    @Embedded
    @AttributeOverride(name = "userId", column = Column(name = "user_id"))
    var registrant: Registrant
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
        protected set

    constructor(
        issueType: IssueType,
        startAt: LocalDateTime,
        endAt: LocalDateTime,
        description: String,
        registrantId: Long
    ) : this(
        issueType = issueType,
        description = description,
        approvalState = ApprovalState.PENDING,
        duration = Duration(startAt, endAt),
        registrant = Registrant(registrantId)
    )

    fun update(
        issueType: IssueType,
        startAt: LocalDateTime,
        endAt: LocalDateTime,
        description: String
    ) {
        validatePending()
        this.issueType = issueType
        this.duration = Duration(startAt, endAt)
        this.description = description
    }

    fun approve() {
        if (approvalState.isApproved()) {
            throw AppException(REQUEST_ALREADY_APPROVED)
        }
        approvalState = ApprovalState.APPROVED
    }

    fun reject() {
        if (approvalState.isRejected()) {
            throw AppException(REQUEST_ALREADY_REJECTED)
        }
        approvalState = ApprovalState.REJECTED
    }

    fun validateRegistrant(currentUserId: Long) {
        registrant.validateRegistrant(currentUserId)
    }

    fun validateCanRegister() {
        if (duration.isRegisteredToday() && approvalState.isPending()) {
            throw AppException(REQUEST_ALREADY_EXISTS)
        }
    }

    fun validatePending() {
        if (!approvalState.isPending()) {
            throw AppException(REQUEST_ALREADY_APPROVED)
        }
    }
}
