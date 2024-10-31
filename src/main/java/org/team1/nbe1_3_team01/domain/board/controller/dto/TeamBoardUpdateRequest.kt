package org.team1.nbe1_3_team01.domain.board.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

class TeamBoardUpdateRequest(
    @NotBlank(message = "필수 파라미터가 누락되었습니다.")
    @Positive(message = "요청 파라미터의 형식이 잘못되었습니다.")
    val teamBoardId: Long,

    @NotBlank(message = "필수 파라미터가 누락되었습니다.")
    @Positive(message = "요청 파라미터의 형식이 잘못되었습니다.")
    var categoryId: Long,

    @NotBlank(message = "제목은 필수 입력값입니다.")
    var title: String,

    @NotBlank(message = "내용은 필수 입력값입니다.")
    var content: String
) {
}
