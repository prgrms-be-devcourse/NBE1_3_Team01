package org.team1.nbe1_3_team01.domain.user.service.response

import org.team1.nbe1_3_team01.domain.user.entity.Role


data class UserBriefWithRoleResponse(
    val id: Long,
    val username: String,
    val name: String,
    val role: Role
)
