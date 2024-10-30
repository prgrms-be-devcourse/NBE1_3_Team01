package org.team1.nbe1_3_team01.domain.chat.controller.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmoticonMessageRequest {
    private Long userId;
    private String emoticonUrl;
}
