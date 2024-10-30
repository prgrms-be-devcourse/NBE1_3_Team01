package org.team1.nbe1_3_team01.domain.group.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamDeleteRequest {

    @NotNull(message = "teamId가 누락되었습니다.")
    private Long teamId;

}
