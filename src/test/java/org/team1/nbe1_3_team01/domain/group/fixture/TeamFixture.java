package org.team1.nbe1_3_team01.domain.group.fixture;

import org.team1.nbe1_3_team01.domain.group.entity.Team;
import org.team1.nbe1_3_team01.domain.group.entity.TeamType;

public final class TeamFixture {

    public static Team create_PROJECT_TEAM_1() {
        return Team.builder()
                .name("TEAM_1")
                .teamType(TeamType.PROJECT)
                .creationWaiting(false)
                .deletionWaiting(false)
                .build();
    }
}
