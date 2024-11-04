package org.team1.nbe1_3_team01.domain.chat.controller.request

data class InviteRequest(
    var inviteUserId: Long,
    var participantId: Long,
    var channelId: Long
)