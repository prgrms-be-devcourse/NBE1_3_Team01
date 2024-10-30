package org.team1.nbe1_3_team01.global.auth.email.token;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash(value = "emailToken", timeToLive = 60 * 60 * 24 * 7)
public class EmailToken {
    @Id
    private String email;

    @Indexed
    private String code;

    @Builder
    private EmailToken(String email,String code){
        this.email = email;
        this.code = code;
    }
}
