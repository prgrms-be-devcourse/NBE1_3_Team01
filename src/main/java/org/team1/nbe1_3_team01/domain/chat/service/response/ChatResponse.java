package org.team1.nbe1_3_team01.domain.chat.service.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatResponse {
    private Long UserId;
    private String content;
    private String userName; // 이름
    private LocalDateTime createdAt;
}
