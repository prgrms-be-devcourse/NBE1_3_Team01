package org.team1.nbe1_3_team01.domain.chat.controller.request

data class ChannelCreateRequest(
    val creatorUserId: Long,
    val channelName: String
)
