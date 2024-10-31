package org.team1.nbe1_3_team01.domain.chat.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import lombok.Getter
import java.io.Serializable

@Embeddable
@Getter
class ParticipantPK : Serializable {
    @Column(name = "user_id")
    var userId: Long? = null

    @Column(name = "channel_id")
     var channelId: Long? = null

    constructor()

    constructor(userId: Long?, channelId: Long?) {
        this.userId = userId
        this.channelId = channelId
    }

    override fun toString(): String {
        return "ParticipantPK{" +
                "userId=" + userId +
                ", channelId=" + channelId +
                '}'
    }
}