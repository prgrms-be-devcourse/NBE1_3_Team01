import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQueryFactory
import org.team1.nbe1_3_team01.domain.board.service.response.CategoryResponse
import org.team1.nbe1_3_team01.domain.board.service.response.CategoryResponse.Companion.of
import org.springframework.stereotype.Repository
import org.team1.nbe1_3_team01.domain.board.entity.QCategory.category
import org.team1.nbe1_3_team01.domain.board.entity.QTeamBoard.teamBoard
import org.team1.nbe1_3_team01.domain.board.repository.CustomCategoryRepository

@Repository
class CustomCategoryRepositoryImpl(
    private val queryFactory: JPAQueryFactory
) : CustomCategoryRepository {

    override fun getAllCategoryByTeamId(teamId: Long): List<CategoryResponse> {
        val fetchedList = queryFactory.select(
            category.id,
            category.name,
            teamBoard.count().`as`("boardCount")
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
            of(
                tuple.get(category.id),
                tuple.get(category.name),
                tuple.get(teamBoard.count()) ?: 0L
            )
    }
}
