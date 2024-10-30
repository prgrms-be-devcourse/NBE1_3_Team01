package org.team1.nbe1_3_team01.global.auth.email.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record EmailsRequest(
        @NotNull(message = "코스 아이디는 필수 입력 값입니다")
        Long courseId,
        List<@Email(message = "유효하지 않은 이메일 형식입니다.") String> emails
) {
}
