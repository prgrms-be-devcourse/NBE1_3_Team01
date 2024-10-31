package org.team1.nbe1_3_team01.domain.user.service.response

import org.team1.nbe1_3_team01.domain.user.entity.Role
import org.team1.nbe1_3_team01.domain.user.entity.User


data class UserAdminCheckResponse(val isAdmin: Boolean){
    companion object {
        fun from(user: User): UserAdminCheckResponse {
            return UserAdminCheckResponse(user.role==Role.ADMIN)
        }
    }
}
