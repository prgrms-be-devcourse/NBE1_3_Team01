package org.team1.nbe1_3_team01.domain.board.comment.repository;

import org.team1.nbe1_3_team01.domain.board.comment.service.response.CommentResponse;

import java.util.List;

public interface CustomCommentRepository {

    List<CommentResponse> getCommentsByBoardId(Long boardId, Long pageable);
}
