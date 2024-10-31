package org.team1.nbe1_3_team01.domain.board.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter @ToString
public class CategoryResponse {

    private final Long id;
    private final String name;
    private final Long boardCount;

    @Builder
    private CategoryResponse(Long id, String name, Long boardCount) {
        this.id = id;
        this.name = name;
        this.boardCount = boardCount;
    }

    public static CategoryResponse of(Long categoryId, String name, Long count) {
        return CategoryResponse.builder()
                .id(categoryId)
                .name(name)
                .boardCount(count)
                .build();
    }
}
