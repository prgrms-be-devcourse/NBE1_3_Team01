package org.team1.nbe1_3_team01.domain.board.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.team1.nbe1_3_team01.domain.board.entity.Category;
import org.team1.nbe1_3_team01.domain.group.entity.Team;

public record CategoryRequest(
        @NotNull(message = "카테고리 등록에 실패했습니다.")
        @Positive(message = "카테고리 등록에 실패했습니다.")
        Long teamId,

        @NotBlank(message = "카테고리명은 필수값입니다.")
        String name
) {
    public Category toEntity(Team team) {
        return Category.builder()
                .team(team)
                .name(name)
                .build();
    }
}
