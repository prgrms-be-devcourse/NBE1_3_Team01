package org.team1.nbe1_3_team01.domain.board.service.valid

import lombok.AccessLevel
import lombok.NoArgsConstructor
import org.team1.nbe1_3_team01.domain.board.entity.TeamBoard
import org.team1.nbe1_3_team01.domain.user.entity.Role
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode

@NoArgsConstructor(access = AccessLevel.PRIVATE)
object TeamBoardValidator {
    fun validateWriter(board: TeamBoard, currentUser: User) {
        val writer = board.user
        val isWriter = writer.username == currentUser.username
        val isUser = currentUser.role == Role.USER

        if (!isWriter && isUser) {
            throw AppException(ErrorCode.CANNOT_MANIPULATE_OTHERS_BOARD)
        }
    }
}
