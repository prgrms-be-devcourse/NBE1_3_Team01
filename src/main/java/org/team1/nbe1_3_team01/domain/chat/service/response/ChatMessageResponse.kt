package org.team1.nbe1_3_team01.domain.chat.service.response

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import java.time.LocalDateTime

data class ChatMessageResponse(
    val channelId: Long,
    val userId: Long,
    val content: String,
    val createdAt: LocalDateTime
)
