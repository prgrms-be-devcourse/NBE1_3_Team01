package org.team1.nbe1_3_team01.domain.board.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter @ToString
public class PagingResponse {
    private final Long page;
    private final Long boardId;

    @Builder
    private PagingResponse(Long page, Long boardId) {
        this.page = page;
        this.boardId = boardId;
    }

    public static PagingResponse of(
            Long page,
            Long boardId
    ) {
        return PagingResponse.builder()
                .page(page)
                .boardId(boardId)
                .build();
    }
}
