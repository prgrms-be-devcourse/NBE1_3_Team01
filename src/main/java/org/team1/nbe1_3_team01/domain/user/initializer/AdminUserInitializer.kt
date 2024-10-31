package org.team1.nbe1_3_team01.domain.user.initializer

import org.springframework.boot.CommandLineRunner
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository

@Component
class AdminUserInitializer(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
)  : CommandLineRunner{

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        val user: User = User.ofAdmin(
            username = "admin",
            password = "admin1234",
            email = "admin@naver.com",
            name = "관리자",
        )

        user.passwordEncode(passwordEncoder)
        userRepository.save(user)
    }
}
