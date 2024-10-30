package org.team1.nbe1_3_team01.domain.board.comment.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.team1.nbe1_3_team01.domain.board.entity.TeamBoard;
import org.team1.nbe1_3_team01.domain.board.entity.Comment;
import org.team1.nbe1_3_team01.domain.user.entity.User;

public record CommentRequest(
        @NotBlank(message = "필수 파라미터가 누락되었습니다.")
        @Positive(message = "요청 파라미터의 형식이 잘못되었습니다.")
        Long teamBoardId,

        @NotBlank(message = "내용을 입력해주세요.")
        String content
) {
    public Comment toEntity(User user, TeamBoard board) {
        return Comment.builder()
                .user(user)
                .board(board)
                .content(content)
                .build();
    }
}
