package org.team1.nbe1_3_team01.domain.board.comment.service

import org.team1.nbe1_3_team01.domain.board.comment.controller.dto.CommentRequest
import org.team1.nbe1_3_team01.domain.board.comment.service.response.CommentResponse
import org.team1.nbe1_3_team01.global.util.Message

interface CommentService {
    fun getReviewsByPage(boardId: Long?, lastCommentId: Long?): List<CommentResponse?>?

    fun deleteById(id: Long): Message

    fun addComment(commentRequest: CommentRequest): Message
}
