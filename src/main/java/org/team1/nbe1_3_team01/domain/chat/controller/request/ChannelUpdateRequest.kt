package org.team1.nbe1_3_team01.domain.chat.controller.request

data class ChannelUpdateRequest(
    val userId: Long,
    val channelId: Long,
    val channelName: String
)