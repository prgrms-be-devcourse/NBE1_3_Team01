package org.team1.nbe1_3_team01.domain.board.service

import org.team1.nbe1_3_team01.domain.board.controller.dto.CategoryDeleteRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.CategoryRequest
import org.team1.nbe1_3_team01.domain.board.service.response.CategoryResponse
import org.team1.nbe1_3_team01.global.util.Message

interface CategoryService {
    fun getAllCategoryByBelongings(teamId: Long): List<CategoryResponse>

    fun addCategory(categoryRequest: CategoryRequest): Message

    fun deleteCategory(request: CategoryDeleteRequest): Message
}
