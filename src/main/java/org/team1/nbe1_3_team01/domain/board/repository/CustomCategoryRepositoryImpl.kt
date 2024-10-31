import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQueryFactory
import org.team1.nbe1_3_team01.domain.board.entity.QTeamBoard
import org.team1.nbe1_3_team01.domain.board.service.response.CategoryResponse
import org.team1.nbe1_3_team01.domain.board.service.response.CategoryResponse.Companion.of
import org.springframework.stereotype.Repository
import java.util.Optional

import org.team1.nbe1_3_team01.domain.board.entity.QCategory.category
import org.team1.nbe1_3_team01.domain.board.repository.CustomCategoryRepository

@Repository
class CustomCategoryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomCategoryRepository {

    override fun getAllCategoryByTeamId(teamId: Long): List<CategoryResponse> {
        val fetchedList = queryFactory.select(
            category.id,
            category.name,
            QTeamBoard.teamBoard.count().`as`("boardCount")
        )
            .from(category)
            .leftJoin(QTeamBoard.teamBoard).on(QTeamBoard.teamBoard.categoryId.eq(category.id))
            .where(category.team.id.eq(teamId))
            .groupBy(category.id, category.name)
            .orderBy(category.id.asc())
            .fetch()

        return convertToResponse(fetchedList)
    }

    private fun convertToResponse(fetchedList: List<Tuple>): List<CategoryResponse>
        = fetchedList.map { tuple ->
            of(
                tuple.get(category.id),
                tuple.get(category.name),
                Optional.ofNullable(tuple.get(QTeamBoard.teamBoard.count())).orElse(0L)
            )
    }
}
