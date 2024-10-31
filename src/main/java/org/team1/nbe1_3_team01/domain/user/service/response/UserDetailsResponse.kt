package org.team1.nbe1_3_team01.domain.user.service.response
import org.team1.nbe1_3_team01.domain.user.entity.User

data class UserDetailsResponse(
    val id: Long,
    val username: String,
    val email: String,
    val name: String,
    val courseName: String?
){
    companion object {
        fun from(user: User): UserDetailsResponse {
            return UserDetailsResponse(
                id = user.id!!,
                username = user.username,
                email = user.email,
                name = user.name,
                courseName = user.course?.name
            )
        }
    }
}
