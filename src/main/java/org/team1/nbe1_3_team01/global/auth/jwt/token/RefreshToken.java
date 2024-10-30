package org.team1.nbe1_3_team01.global.auth.jwt.token;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

/**
 * value는 redis key 값으로 사용됨
 * redis 저장소의 key : value 형식으로  {value} : {@Id 값} 저장
 * 유효 시간 : 60 * 60 * 24 * 14 -> 2주
 */
@Getter
@RedisHash(value = "refreshToken", timeToLive = 60 * 60 * 24 * 14)
public class RefreshToken {
    @Id
    private String username;

    @Indexed
    private String token;

    @Builder
    private RefreshToken(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
