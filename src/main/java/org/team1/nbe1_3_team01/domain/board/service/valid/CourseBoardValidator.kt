package org.team1.nbe1_3_team01.domain.board.service.valid

import lombok.AccessLevel
import lombok.NoArgsConstructor
import org.team1.nbe1_3_team01.domain.board.entity.CourseBoard
import org.team1.nbe1_3_team01.domain.user.entity.Role
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import org.team1.nbe1_3_team01.global.util.SecurityUtil

@NoArgsConstructor(access = AccessLevel.PRIVATE)
object CourseBoardValidator {
    fun validateWriter(board: CourseBoard, currentUser: User) {
        val writer = board.user
        val isWriter = writer.username == currentUser.username
        val isUser = currentUser.role == Role.USER

        if (!isWriter && isUser) {
            throw AppException(ErrorCode.CANNOT_MANIPULATE_OTHERS_BOARD)
        }
    }

    fun validateAdminForNotice(user: User?, isNotice: Boolean) {
        if (isNotice) {
            SecurityUtil.validateAdmin(user)
        }
    }
}
