package org.team1.nbe1_3_team01.domain.chat.controller.request


import lombok.Setter
import org.team1.nbe1_3_team01.domain.chat.entity.ChatActionType
import java.time.LocalDateTime

data class ChatMessageRequest(
    val channelId: Long,
    val userId: Long,
    val content: String,
    val createdAt: LocalDateTime,
    val actionType: ChatActionType?
)
