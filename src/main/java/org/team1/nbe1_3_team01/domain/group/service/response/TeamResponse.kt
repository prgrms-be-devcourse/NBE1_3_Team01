package org.team1.nbe1_3_team01.domain.group.service.response

import org.team1.nbe1_3_team01.domain.group.entity.Team


data class TeamResponse (
    private val id: Long,
    private val courseName: String,
    private val teamType: String,
    private val name: String,
    private val creationWaiting: Boolean,
    private val deletionWaiting: Boolean
) {
    private val belongings: MutableList<BelongingResponse> = ArrayList()

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
