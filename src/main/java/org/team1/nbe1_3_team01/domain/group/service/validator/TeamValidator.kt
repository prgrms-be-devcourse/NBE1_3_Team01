package org.team1.nbe1_3_team01.domain.group.service.validator

import lombok.AccessLevel
import lombok.NoArgsConstructor
import org.team1.nbe1_3_team01.domain.group.entity.Team
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode

object TeamValidator {
    fun validateTeamLeader(team: Team, user: User): Long? {
        var leaderId: Long? = null
        for (b in team.belongings) if (b.isOwner) leaderId = b.user.id

        if (user.id != leaderId) throw AppException(ErrorCode.NOT_TEAM_LEADER)

        return leaderId
    }
}
