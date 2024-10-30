package org.team1.nbe1_3_team01.domain.chat.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChannelUpdateRequest {
    private Long userId;
    private Long channelId;
    private String channelName;
}
