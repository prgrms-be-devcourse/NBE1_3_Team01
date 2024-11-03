package org.team1.nbe1_3_team01.global.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class PeriodValidator : ConstraintValidator<PeriodCheck, DurationRequest> {

    override fun initialize(constraintAnnotation: PeriodCheck) {
    }

    override fun isValid(durationRequest: DurationRequest?, context: ConstraintValidatorContext): Boolean {
        if (durationRequest == null) {
            return true
        }

        val startAt: LocalDateTime = durationRequest.startAt
        val endAt: LocalDateTime = durationRequest.endAt

        if (startAt.isAfter(endAt)) {
            context.disableDefaultConstraintViolation()
            context.buildConstraintViolationWithTemplate("시작 시간이 끝 시간보다 늦을 수 없습니다.")
                .addConstraintViolation()
            return false
        }

        return true
    }
}

