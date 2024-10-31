package org.team1.nbe1_3_team01.domain.board.controller.dto;

public record TeamBoardListRequest(
        Long teamId,
        Long categoryId,
        Long boardId
) {
}
