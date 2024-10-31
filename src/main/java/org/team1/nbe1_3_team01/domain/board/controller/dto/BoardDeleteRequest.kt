package org.team1.nbe1_3_team01.domain.board.controller.dto;

import jakarta.validation.constraints.Positive;

public record BoardDeleteRequest(

        @Positive(message = "요청 파라미터가 잘못되었습니다.")
        Long boardId,
        boolean isNotice
) {

}
