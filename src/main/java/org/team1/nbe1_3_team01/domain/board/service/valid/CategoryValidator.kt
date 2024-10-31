package org.team1.nbe1_3_team01.domain.board.service.valid

import lombok.AccessLevel
import lombok.NoArgsConstructor
import org.team1.nbe1_3_team01.domain.group.entity.Belonging
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode

@NoArgsConstructor(access = AccessLevel.PRIVATE)
object CategoryValidator {
    /**
     * 해당 소속 번호가 팀장의 소속 번호인지
     * 검증하는 메서드
     * (belonging 도메인에 있는게 더 바람직해보임)
     * @param belonging
     */
    fun validateTeamLeader(belonging: Belonging) {
        if (!belonging.isOwner) {
            throw AppException(ErrorCode.NOT_TEAM_LEADER)
        }
    }
}
