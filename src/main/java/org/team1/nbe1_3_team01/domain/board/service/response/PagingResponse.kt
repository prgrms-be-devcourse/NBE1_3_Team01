package org.team1.nbe1_3_team01.domain.board.service.response

import lombok.*

@Getter
@ToString
class PagingResponse @Builder private constructor(
    val page: Long,
    val boardId: Long?
) {
    companion object {
        fun of(
            page: Long,
            boardId: Long?
        ): PagingResponse =
            PagingResponse(
                page = page,
                boardId = boardId
            )
    }
}
