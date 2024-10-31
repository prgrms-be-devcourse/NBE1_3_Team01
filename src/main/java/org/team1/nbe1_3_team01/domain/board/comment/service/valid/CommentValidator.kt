package org.team1.nbe1_3_team01.domain.board.comment.service.valid;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.team1.nbe1_3_team01.domain.board.entity.Comment;
import org.team1.nbe1_3_team01.domain.user.entity.Role;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.ErrorCode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentValidator {

    public static void validateCommenter(Comment comment, User user) {
        User commenter = comment.getUser();
        if(!commenter.getUsername().equals(user.getUsername())
                && user.getRole().equals(Role.USER)) {
            throw new AppException(ErrorCode.CANNOT_MANIPULATE_OTHERS_COMMENT);
        }
    }
}
