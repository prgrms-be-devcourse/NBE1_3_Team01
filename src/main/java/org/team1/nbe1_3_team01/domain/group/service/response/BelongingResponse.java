package org.team1.nbe1_3_team01.domain.group.service.response;

import lombok.Builder;
import lombok.Data;
import org.team1.nbe1_3_team01.domain.group.entity.Belonging;

@Data
public class BelongingResponse {

    private Long id;
    private Long userId;
    private Long teamId;

    @Builder
    private BelongingResponse(Long id, Long userId, Long teamId) {
        this.id = id;
        this.userId = userId;
        this.teamId = teamId;
    }

    public static BelongingResponse from(Belonging belonging) {
        return BelongingResponse.builder()
                .id(belonging.getId())
                .userId(belonging.getUser() != null ? belonging.getUser().getId() : null)
                .teamId(belonging.getTeam() != null ? belonging.getTeam().getId() : null)
                .build();
    }

}
