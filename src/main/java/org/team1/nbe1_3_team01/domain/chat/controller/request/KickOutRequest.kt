package org.team1.nbe1_3_team01.domain.chat.controller.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class KickOutRequest {

    private Long channelId;
    private Long participantIdToRemove;
    private Long creatorId;
}
