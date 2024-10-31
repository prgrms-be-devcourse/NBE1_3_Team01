package org.team1.nbe1_3_team01.domain.chat.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;

@Embeddable
@Getter
public class ParticipantPK implements Serializable {
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "channel_id")
    private Long channelId;

    public ParticipantPK() {
    }

    public ParticipantPK(Long userId, Long channelId) {
        this.userId = userId;
        this.channelId = channelId;
    }

    @Override
    public String toString() {
        return "ParticipantPK{" +
                "userId=" + userId +
                ", channelId=" + channelId +
                '}';
    }
}