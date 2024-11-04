package org.team1.nbe1_3_team01.global.auth.email.token

import lombok.Getter
import org.springframework.data.annotation.Id
import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed

@RedisHash(value = "emailToken", timeToLive = 60 * 60 * 24 * 7)
class EmailToken private constructor(
    @Id val email: String,
    @Indexed val code: String
) {

    companion object {
        fun of(
            email: String,
            code: String
        ): EmailToken = EmailToken(
            email = email,
            code = code
        )
    }
}
