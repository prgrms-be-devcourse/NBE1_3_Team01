package org.team1.nbe1_3_team01.domain.board.service.response

import lombok.*

@Getter
@ToString
class CategoryResponse @Builder private constructor(
    private val id: Long?,
    private val name: String?,
    private val boardCount: Long
) {
    companion object {
        fun of(categoryId: Long?, name: String?, count: Long): CategoryResponse
            = CategoryResponse(
                id = categoryId,
                name = name,
                boardCount = count
            )
    }
}
