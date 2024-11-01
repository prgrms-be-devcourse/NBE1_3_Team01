package org.team1.nbe1_3_team01.domain.chat.controller.request


data class KickOutRequest(
    val channelId: Long,
    val participantIdToRemove: Long,
    val creatorId: Long
)