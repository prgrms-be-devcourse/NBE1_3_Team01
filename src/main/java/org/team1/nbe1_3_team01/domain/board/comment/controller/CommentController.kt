package org.team1.nbe1_3_team01.domain.board.comment.controller

import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team1.nbe1_3_team01.domain.board.comment.controller.dto.CommentRequest
import org.team1.nbe1_3_team01.domain.board.comment.service.CommentService
import org.team1.nbe1_3_team01.domain.board.comment.service.response.CommentResponse
import org.team1.nbe1_3_team01.global.util.Message
import org.team1.nbe1_3_team01.global.util.Response


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
class CommentController(
    private val commentService: CommentService
) {
    /**
     * 댓글 목록 조회
     * @param commentId 마지막으로 조회된 댓긃 번호
     * @param boardId 게시글 번호
     * @return
     */
    @GetMapping
    fun getComments(
        @RequestParam(required = false) commentId: Long?,
        @RequestParam boardId: Long?
    ): ResponseEntity<Response<List<CommentResponse?>?>> {
        val reviews = commentService.getReviewsByPage(boardId, commentId)
        return ResponseEntity.ok()
            .body(Response.success(reviews))
    }

    /**
     * 댓글 삭제
     * @param commentId 댓글 번호
     * @return
     */
    @DeleteMapping
    fun deleteComment(
        @RequestBody commentId: Long
    ): ResponseEntity<Response<Message>> {
        val message = commentService.deleteById(commentId)
        return ResponseEntity.ok()
            .body(Response.success(message))
    }

    /**
     * 댓글 등록
     * @param commentRequest
     * @return
     */
    @PostMapping
    fun addComment(
        @RequestBody commentRequest: CommentRequest
    ): ResponseEntity<Response<Message>> {
        val message = commentService.addComment(commentRequest)
        return ResponseEntity.ok()
            .body(Response.success(message))
    }
}
