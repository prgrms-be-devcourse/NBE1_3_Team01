package org.team1.nbe1_3_team01.domain.group.controller.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import org.team1.nbe1_3_team01.domain.group.entity.Team
import org.team1.nbe1_3_team01.domain.group.entity.TeamType
import org.team1.nbe1_3_team01.domain.user.entity.Course

data class TeamCreateRequest (
    @field:NotNull(message = "courseId가 누락되었습니다.")
    val courseId: Long,

    @field:NotBlank(message = "teamType이 누락되었습니다.")
    @Pattern(regexp = "PROJECT|STUDY", message = "teamType은 PROJECT 또는 STUDY여야 합니다.")
    val teamType: String,

    @field:NotBlank(message = "name이 누락되었습니다.")
    val name: String,

    @field:NotEmpty(message = "userIds가 누락되었습니다.")
    val userIds: MutableList<Long>,

    @field:NotNull(message = "leaderId가 누락되었습니다.")
    val leaderId:Long
) {
    fun toTeamEntity(study: Boolean, course: Course): Team {
        return Team(
            teamType = TeamType.valueOf(this.teamType),
            name = this.name,
            creationWaiting = study,
            deletionWaiting = false,
            course = course
        )
    }
}
