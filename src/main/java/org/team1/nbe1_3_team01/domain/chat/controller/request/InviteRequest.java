package org.team1.nbe1_3_team01.domain.chat.controller.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InviteRequest {
    private Long inviteUserId; // 초대자
    private Long participantId; // 참여자
    private Long channelId;

    public InviteRequest(Long inviteUserId, Long participantId, Long channelId) {
        this.inviteUserId = inviteUserId;
        this.participantId = participantId;
        this.channelId = channelId;
    }

    public InviteRequest() {
    }
}
