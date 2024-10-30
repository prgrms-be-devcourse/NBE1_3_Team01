package org.team1.nbe1_3_team01.domain.board.service.valid;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.team1.nbe1_3_team01.domain.board.entity.CourseBoard;
import org.team1.nbe1_3_team01.domain.user.entity.Role;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.ErrorCode;
import org.team1.nbe1_3_team01.global.util.SecurityUtil;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CourseBoardValidator {

    public static void validateWriter(CourseBoard board, User currentUser) {
        User writer = board.getUser();
        boolean isWriter = writer.getUsername().equals(currentUser.getUsername());
        boolean isUser = currentUser.getRole().equals(Role.USER);

        if(!isWriter && isUser) {
            throw new AppException(ErrorCode.CANNOT_MANIPULATE_OTHERS_BOARD);
        }
    }

    public static void validateAdminForNotice(User user, boolean isNotice) {
        if(isNotice) {
            SecurityUtil.validateAdmin(user);
        }
    }
}
