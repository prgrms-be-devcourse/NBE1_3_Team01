package org.team1.nbe1_3_team01.domain.board.comment.repository

import org.team1.nbe1_3_team01.domain.board.comment.service.response.CommentResponse

interface CustomCommentRepository {
    fun getCommentsByBoardId(boardId: Long?, pageable: Long?): List<CommentResponse>
}
