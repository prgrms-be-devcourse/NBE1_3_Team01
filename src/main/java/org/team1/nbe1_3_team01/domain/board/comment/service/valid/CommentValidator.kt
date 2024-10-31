package org.team1.nbe1_3_team01.domain.board.comment.service.valid

import lombok.AccessLevel
import lombok.NoArgsConstructor
import org.team1.nbe1_3_team01.domain.board.entity.Comment
import org.team1.nbe1_3_team01.domain.user.entity.Role
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode

@NoArgsConstructor(access = AccessLevel.PRIVATE)
object CommentValidator {
    fun validateCommenter(comment: Comment?, user: User) {
        val commenter: User = comment!!.user;
        if (commenter.username != user.username && user.role == Role.USER) {
            throw AppException(ErrorCode.CANNOT_MANIPULATE_OTHERS_COMMENT)
        }
    }
}
