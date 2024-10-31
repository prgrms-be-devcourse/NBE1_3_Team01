package org.team1.nbe1_3_team01.domain.board.repository

import org.team1.nbe1_3_team01.domain.board.service.response.CategoryResponse

interface CustomCategoryRepository {
    fun getAllCategoryByTeamId(teamId: Long): List<CategoryResponse>
}
