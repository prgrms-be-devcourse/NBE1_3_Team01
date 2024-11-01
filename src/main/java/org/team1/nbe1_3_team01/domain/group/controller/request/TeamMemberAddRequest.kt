package org.team1.nbe1_3_team01.domain.group.controller.request

import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import lombok.Data


data class TeamMemberAddRequest (
    @field:NotNull(message = "teamId가 누락되었습니다.")
    val teamId: Long,

    @field:NotEmpty(message = "userIds가 누락되었습니다.")
    val userIds: MutableList<Long>
) {
}
