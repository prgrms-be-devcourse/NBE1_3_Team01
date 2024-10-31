package org.team1.nbe1_3_team01.domain.board.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.team1.nbe1_3_team01.domain.board.entity.TeamBoard
import org.team1.nbe1_3_team01.domain.group.entity.Team
import org.team1.nbe1_3_team01.domain.user.entity.User

class TeamBoardRequest(
    @NotNull(message = "필수 파라미터 누락")
    @Positive(message = "필수 파라미터 형식 오류")
    var teamId: Long,

    @NotNull(message = "필수 파라미터 누락")
    @Positive(message = "필수 파라미터 형식 오류")
    var categoryId: Long,

    @NotBlank(message = "제목은 필수 입력값입니다.")
    var title: String,

    @NotBlank(message = "내용은 필수 입력값입니다.")
    var content: String,

    @NotNull(message = "필수 파라미터 누락")
    var isNotice: Boolean
) {
    fun toEntity(user: User, team: Team): TeamBoard
        = TeamBoard(
            title = title,
            content = content,
            categoryId = categoryId,
            team = team,
            user = user
        )
}
