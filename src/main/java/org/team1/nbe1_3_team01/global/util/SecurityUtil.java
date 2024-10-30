package org.team1.nbe1_3_team01.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.team1.nbe1_3_team01.domain.user.entity.Role;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.global.exception.AppException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class SecurityUtil {
    public static String getCurrentUsername(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDetails.getUsername();
    }

    public static void validateAdmin(User user) {
        if(user.getRole().equals(Role.USER)) {
            throw new AppException(ErrorCode.NOT_ADMIN_USER);
        }
    }
}
