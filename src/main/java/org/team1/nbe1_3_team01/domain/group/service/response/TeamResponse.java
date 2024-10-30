package org.team1.nbe1_3_team01.domain.group.service.response;

import lombok.Builder;
import lombok.Data;
import org.team1.nbe1_3_team01.domain.group.entity.Belonging;
import org.team1.nbe1_3_team01.domain.group.entity.Team;

import java.util.ArrayList;
import java.util.List;

@Data
public class TeamResponse {

    private Long id;
    private String courseName;
    private String teamType;
    private String name;
    private boolean creationWaiting;
    private boolean deletionWaiting;
    private List<BelongingResponse> belongings;

    @Builder
    private TeamResponse(Long id, String courseName, String teamType, String name, boolean creationWaiting, boolean deletionWaiting) {
        this.id = id;
        this.courseName = courseName;
        this.teamType = teamType;
        this.name = name;
        this.creationWaiting = creationWaiting;
        this.deletionWaiting = deletionWaiting;
        this.belongings = new ArrayList<>();
    }

    public static TeamResponse from(Team team) {
        TeamResponse teamResponse = TeamResponse.builder()
                .id(team.getId())
                .courseName(team.getCourse().getName())
                .teamType(team.getTeamType().name())
                .name(team.getName())
                .creationWaiting(team.isCreationWaiting())
                .deletionWaiting(team.isDeletionWaiting())
                .build();

        for (Belonging b : team.getBelongings()) {
            teamResponse.belongings.add(BelongingResponse.from(b));
        }

        return teamResponse;
    }

}
