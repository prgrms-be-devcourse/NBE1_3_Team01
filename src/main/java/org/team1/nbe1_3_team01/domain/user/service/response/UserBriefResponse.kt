package org.team1.nbe1_3_team01.domain.user.service.response

import org.team1.nbe1_3_team01.domain.user.entity.User


data class UserBriefResponse(
    val id: Long,
    val username: String,
    val name: String
){
    companion object {
        fun from(user: User): UserBriefResponse {
            return UserBriefResponse(
                id = user.id!!,
                username = user.username,
                name = user.name,
            )
        }
    }
}
