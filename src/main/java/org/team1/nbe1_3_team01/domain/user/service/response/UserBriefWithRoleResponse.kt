package org.team1.nbe1_3_team01.domain.user.service.response

import org.team1.nbe1_3_team01.domain.user.entity.Role
import org.team1.nbe1_3_team01.domain.user.entity.User


data class UserBriefWithRoleResponse(
    val id: Long,
    val username: String,
    val name: String,
    val role: Role
){
    companion object {
        fun from(user: User): UserBriefWithRoleResponse {
            return UserBriefWithRoleResponse(
                id = user.id!!,
                username = user.username,
                name = user.name,
                role = user.role
            )
        }
    }
}
