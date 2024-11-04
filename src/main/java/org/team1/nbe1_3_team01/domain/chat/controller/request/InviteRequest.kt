package org.team1.nbe1_3_team01.domain.chat.controller.request

data class InviteRequest(
    val participantIds: List<Long>,
    var channelId: Long
)