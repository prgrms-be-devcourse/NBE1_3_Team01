package org.team1.nbe1_3_team01.domain.board.repository

import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import org.team1.nbe1_3_team01.domain.board.service.response.CategoryResponse
import org.team1.nbe1_3_team01.domain.board.service.response.CategoryResponse.Companion.of
import org.team1.nbe1_3_team01.domain.board.entity.QCategory.category
import org.team1.nbe1_3_team01.domain.board.entity.QTeamBoard.teamBoard

@Repository
class CustomCategoryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomCategoryRepository {

    override fun getAllCategoryByTeamId(teamId: Long): List<CategoryResponse> {
        val fetchedList = queryFactory.select(
            category.id,
            category.name,
            teamBoard.id.count()
        )
            .from(category)
            .leftJoin(teamBoard).on(teamBoard.categoryId.eq(category.id))
            .where(category.team.id.eq(teamId))
            .groupBy(category.id, category.name)
            .orderBy(category.id.asc())
            .fetch()

        return convertToResponse(fetchedList)
    }

    private fun convertToResponse(fetchedList: List<Tuple>): List<CategoryResponse>
        = fetchedList.map { tuple ->
            CategoryResponse(
                id = tuple.get(category.id),
                name = tuple.get(category.name),
                boardCount = tuple.get(teamBoard.count()) ?: 0L
            )
    }
}
