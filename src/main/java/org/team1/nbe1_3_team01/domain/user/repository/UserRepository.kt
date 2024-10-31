package org.team1.nbe1_3_team01.domain.user.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.team1.nbe1_3_team01.domain.user.entity.Course
import org.team1.nbe1_3_team01.domain.user.entity.User
import java.util.*

interface UserRepository : JpaRepository<User?, Long?>,
    CustomUserRepository {
    fun findByUsername(username: String?): Optional<User?>?

    fun findByEmail(email: String?): Optional<User?>?

    fun findByCourse(course: Course?): List<User?>?
}
