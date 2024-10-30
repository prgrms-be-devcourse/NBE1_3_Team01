package org.team1.nbe1_3_team01.domain.chat.service.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageResponse {
    private Long channelId;
    private Long userId;
    private String content;
    private LocalDateTime createdAt;

}
