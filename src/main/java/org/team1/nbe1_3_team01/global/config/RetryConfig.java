package org.team1.nbe1_3_team01.global.config;

import jakarta.mail.MessagingException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class RetryConfig {
    @Bean
    public RetryTemplate retryTemplate(){
        return RetryTemplate.builder()
                .maxAttempts(3) // 최대 재시도 횟수
                .fixedBackoff(2000) // 재시도 간격
                .retryOn(MessagingException.class) // 재시도 예외 지정
                .build();
    }
}
