package org.team1.nbe1_3_team01.domain.chat.controller.request

data class EmoticonMessageRequest(
    val userId: Long,
    val emoticonUrl: String
)