package org.team1.nbe1_3_team01.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer

@Configuration
@EnableWebSocketMessageBroker
class WebSocketConfig : WebSocketMessageBrokerConfigurer {
    override fun configureMessageBroker(config: MessageBrokerRegistry) {
        config.enableSimpleBroker("/topic") // 메세지의 브로커를 설정(수신)
        config.setApplicationDestinationPrefixes("/app") // 애플리케이션의 목적지 접두사(발행)
    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/stomp-endpoint") // 웹소켓 엔드포인트 설정
            .setAllowedOriginPatterns("*") // 일단 모든 오리진 허용
    }
}
