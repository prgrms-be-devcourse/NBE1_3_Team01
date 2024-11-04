package org.team1.nbe1_3_team01.domain.group.service.response

import org.team1.nbe1_3_team01.domain.group.entity.Belonging

data class BelongingResponse (
    private val id: Long,
    private val userId: Long,
    private val teamId: Long
) {
    companion object {
        fun from(belonging: Belonging): BelongingResponse {
            return BelongingResponse(
                id = belonging.id!!,
                userId = belonging.user.id!!,
                teamId = belonging.team!!.id!!
            )
        }
    }
}
