package org.team1.nbe1_3_team01.domain.chat.service.response

import java.time.LocalDateTime

data class ChannelResponse(
    val channelId: Long,
    val name: String,
    val lastMessage: String,
    val lastMessageTime: LocalDateTime
)

