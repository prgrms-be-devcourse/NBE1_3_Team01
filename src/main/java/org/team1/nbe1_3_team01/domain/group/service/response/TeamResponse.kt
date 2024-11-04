package org.team1.nbe1_3_team01.domain.group.service.response

import org.team1.nbe1_3_team01.domain.group.entity.Team


data class TeamResponse (
    val id: Long,
    val courseName: String,
    val teamType: String,
    val name: String,
    val creationWaiting: Boolean,
    val deletionWaiting: Boolean,
    val belongings: MutableList<BelongingResponse> = mutableListOf()
) {
    companion object {
        fun from(team: Team): TeamResponse {
            val teamResponse: TeamResponse = TeamResponse(
                id = team.id!!,
                courseName = team.course.name,
                teamType = team.teamType.name,
                name = team.name,
                creationWaiting = team.creationWaiting,
                deletionWaiting = team.deletionWaiting
            )
            for (b in team.belongings) {
                teamResponse.belongings.add(BelongingResponse.from(b))
            }
            return teamResponse
        }
    }
}
