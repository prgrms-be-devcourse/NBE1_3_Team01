package org.team1.nbe1_3_team01.domain.chat.controller.request;

import lombok.*;
import org.team1.nbe1_3_team01.domain.chat.entity.ChatActionType;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class ChatMessageRequest {
        private Long channelId;
        private Long userId;
        private String content;
        private LocalDateTime createdAt;
        private ChatActionType actionType;  // 입장, 퇴장 등의 행동 타입 추가
}

