package org.team1.nbe1_3_team01.domain.user.service.response

import org.team1.nbe1_3_team01.domain.user.entity.User


data class UserIdResponse(val id: Long) {
    companion object {
        fun from(user: User): UserIdResponse {
            return UserIdResponse(id = user.id!!)
        }
    }
}
