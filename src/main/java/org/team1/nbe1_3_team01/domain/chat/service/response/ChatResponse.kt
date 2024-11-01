package org.team1.nbe1_3_team01.domain.chat.service.response

import java.time.LocalDateTime

data class ChatResponse(
    val userId: Long,
    val content: String,
    val userName: String,
    val createdAt: LocalDateTime
)