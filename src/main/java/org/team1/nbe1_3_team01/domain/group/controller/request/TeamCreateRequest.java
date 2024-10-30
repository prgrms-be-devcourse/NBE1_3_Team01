package org.team1.nbe1_3_team01.domain.group.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.team1.nbe1_3_team01.domain.group.entity.Team;
import org.team1.nbe1_3_team01.domain.group.entity.TeamType;
import org.team1.nbe1_3_team01.domain.user.entity.Course;

import java.util.List;

@Data
@AllArgsConstructor
public class TeamCreateRequest {

    @NotNull(message = "courseId가 누락되었습니다.")
    private Long courseId;

    @Pattern(regexp = "PROJECT|STUDY", message = "teamType은 PROJECT 또는 STUDY여야 합니다.")
    @NotBlank(message = "teamType이 누락되었습니다.")
    private String teamType;

    @NotBlank(message = "name이 누락되었습니다.")
    private String name;

    @NotEmpty(message = "userIds가 누락되었습니다.")
    private List<Long> userIds;

    @NotNull(message = "leaderId가 누락되었습니다.")
    private Long leaderId;

    public Team toTeamEntity(boolean study, Course course) {
        return Team.builder()
                .teamType(TeamType.valueOf(this.getTeamType()))
                .name(this.getName())
                .creationWaiting(study)
                .deletionWaiting(false)
                .course(course)
                .build();
    }

}
