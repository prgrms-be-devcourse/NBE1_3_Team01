package org.team1.nbe1_3_team01.domain.group.service.validator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.team1.nbe1_3_team01.domain.group.entity.Belonging;
import org.team1.nbe1_3_team01.domain.group.entity.Team;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.ErrorCode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TeamValidator {

    public static Long validateTeamLeader(Team team, User user) {
        Long leaderId = null;
        for (Belonging b : team.getBelongings()) if (b.isOwner()) leaderId = b.getUser().getId();

        if (!user.getId().equals(leaderId)) throw new AppException(ErrorCode.NOT_TEAM_LEADER);

        return leaderId;
    }

}
