package org.team1.nbe1_3_team01.global.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceCreateRequest;

public class PeriodValidator implements ConstraintValidator<PeriodCheck, AttendanceCreateRequest> {

    @Override
    public void initialize(PeriodCheck constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(AttendanceCreateRequest attendanceCreateRequest, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime startAt = attendanceCreateRequest.startAt();
        LocalDateTime endAt = attendanceCreateRequest.endAt();

        return startAt.isBefore(endAt);
    }
}
