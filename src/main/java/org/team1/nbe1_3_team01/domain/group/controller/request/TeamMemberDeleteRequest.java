package org.team1.nbe1_3_team01.domain.group.controller.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class TeamMemberDeleteRequest {

    @NotNull(message = "teamId가 누락되었습니다.")
    private Long teamId;

    @NotEmpty(message = "userIds가 누락되었습니다.")
    List<Long> userIds;

}
