package org.team1.nbe1_3_team01.domain.board.comment.service

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.board.comment.controller.dto.CommentRequest
import org.team1.nbe1_3_team01.domain.board.comment.repository.CommentRepository
import org.team1.nbe1_3_team01.domain.board.comment.service.response.CommentResponse
import org.team1.nbe1_3_team01.domain.board.comment.service.valid.CommentValidator
import org.team1.nbe1_3_team01.domain.board.constants.MessageContent
import org.team1.nbe1_3_team01.domain.board.repository.TeamBoardRepository
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import org.team1.nbe1_3_team01.global.util.Message
import org.team1.nbe1_3_team01.global.util.SecurityUtil

@Service
@Transactional
@RequiredArgsConstructor
class CommentServiceImpl(
    private val commentRepository: CommentRepository,
    private val teamBoardRepository: TeamBoardRepository,
    private val userRepository: UserRepository
) : CommentService {

    @Transactional(readOnly = true)
    override fun getReviewsByPage(boardId: Long?, lastCommentId: Long?): List<CommentResponse?>? {
        return commentRepository.getCommentsByBoardId(boardId, lastCommentId)
    }

    override fun deleteById(id: Long): Message {
        val user = user
        val comment = commentRepository.findById(id)
            .orElseThrow { AppException(ErrorCode.COMMENT_NOT_FOUND) }

        CommentValidator.validateCommenter(comment, user)
        commentRepository.delete(comment!!)
        val deleteCommentMessage: String = MessageContent.DELETE_COMMENT_COMPLETED.message
        return Message(deleteCommentMessage)
    }

    override fun addComment(commentRequest: CommentRequest): Message {
        val currentUser = user

        val boardId = commentRequest.teamBoardId
        val findBoard = teamBoardRepository.findById(boardId)
            .orElseThrow { AppException(ErrorCode.BOARD_NOT_FOUND) }!!

        val comment = commentRequest.toEntity(currentUser, findBoard)
        commentRepository.save(comment)

        val addCommentMessage: String = MessageContent.ADD_BOARD_COMPLETED.message
        return Message(addCommentMessage)
    }

    private val user: User
        get() {
            val currentUsername = SecurityUtil.getCurrentUsername()
            return userRepository.findByUsername(currentUsername)
                ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        }
}
