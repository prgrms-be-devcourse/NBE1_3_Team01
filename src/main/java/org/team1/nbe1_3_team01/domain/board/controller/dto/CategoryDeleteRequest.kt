package org.team1.nbe1_3_team01.domain.board.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CategoryDeleteRequest(
        @NotBlank(message = "요청 파라미터가 누락되었습니다.")
        @Positive(message = "요청 파라미터 형식이 잘못되었습니다.")
        Long teamId,

        @NotBlank(message = "요청 파라미터가 누락되었습니다.")
        @Positive(message = "요청 파라미터 형식이 잘못되었습니다.")
        Long categoryId
) {
}
