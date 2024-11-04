package org.team1.nbe1_3_team01.domain.group.controller.request

import jakarta.validation.constraints.NotNull

data class TeamLeaderUpdateRequest(
    @field:NotNull(message = "teamId가 누락되었습니다.")
    val teamId: Long,

    @field:NotNull(message = "leaderId가 누락되었습니다.")
    val newLeaderId: Long
) {
}