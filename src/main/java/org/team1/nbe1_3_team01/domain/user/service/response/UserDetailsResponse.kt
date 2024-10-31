package org.team1.nbe1_3_team01.domain.user.service.response


data class UserDetailsResponse(
    val id: Long,
    val username: String,
    val email: String,
    val name: String,
    val courseName: String
)
