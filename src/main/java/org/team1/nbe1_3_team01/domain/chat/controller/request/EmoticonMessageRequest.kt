package org.team1.nbe1_3_team01.domain.chat.controller.request

import lombok.Builder
import lombok.Data

data class EmoticonMessageRequest(
    val userId: Long,
    val emoticonUrl: String
)