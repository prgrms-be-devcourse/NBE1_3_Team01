package org.team1.nbe1_3_team01.global.util

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.team1.nbe1_3_team01.domain.user.entity.Role
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.global.exception.AppException


class SecurityUtil private constructor() {
    companion object {
        fun getCurrentUsername(): String {
            val userDetails =
                SecurityContextHolder.getContext().authentication.principal as UserDetails
            return userDetails.username
        }

        fun validateAdmin(user: User) {
            if (user.role == Role.USER) {
                throw AppException(ErrorCode.NOT_ADMIN_USER)
            }
        }
    }
}
