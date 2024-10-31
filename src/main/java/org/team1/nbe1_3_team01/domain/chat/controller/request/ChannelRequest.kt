package org.team1.nbe1_3_team01.domain.chat.controller.request

import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

data class ChannelRequest(
    val userId: Long,
    val channelId: Long
)
