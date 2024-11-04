package org.team1.nbe1_3_team01.domain.board.service.response

import lombok.*

data class CategoryResponse (
    val id: Long?,
    val name: String?,
    val boardCount: Long
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
