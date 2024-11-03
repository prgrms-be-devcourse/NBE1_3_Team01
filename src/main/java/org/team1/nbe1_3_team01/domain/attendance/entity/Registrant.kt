package org.team1.nbe1_3_team01.domain.attendance.entity;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.ATTENDANCE_ACCESS_DENIED;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.team1.nbe1_3_team01.global.exception.AppException;

@Embeddable
@EqualsAndHashCode
@Getter
public class Registrant {

    private final Long userId;

    // for JPA
    protected Registrant() {
        userId = null;
    }

    public Registrant(Long userId) {
        this.userId = userId;
    }

    protected void validateRegistrant(Long currentUserId) {
        if (!currentUserId.equals(userId)) {
            throw new AppException(ATTENDANCE_ACCESS_DENIED);
        }
    }
}
