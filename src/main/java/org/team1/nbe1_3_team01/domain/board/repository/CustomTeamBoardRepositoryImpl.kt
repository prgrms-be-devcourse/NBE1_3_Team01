package org.team1.nbe1_3_team01.domain.board.repository

import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import lombok.RequiredArgsConstructor
import org.team1.nbe1_3_team01.domain.board.entity.QCategory.category
import org.team1.nbe1_3_team01.domain.board.entity.QComment.comment
import org.team1.nbe1_3_team01.domain.board.entity.QCourseBoard.courseBoard
import org.team1.nbe1_3_team01.domain.board.entity.QTeamBoard.teamBoard
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse
import org.team1.nbe1_3_team01.domain.board.service.response.PagingResponse
import org.team1.nbe1_3_team01.domain.board.service.response.TeamBoardResponse
import org.team1.nbe1_3_team01.domain.user.entity.QUser.user
import org.team1.nbe1_3_team01.domain.user.entity.Role
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.global.util.SecurityUtil
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

@RequiredArgsConstructor
class CustomTeamBoardRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
    private val entityManager: EntityManager
) : CustomTeamBoardRepository {

    override fun findAllTeamBoardByType (
        teamId: Long,
        categoryId: Long,
        boardId: Long?
    ): List<TeamBoardResponse> {
        val teamBoardQuery = buildTeamBoardListQuery(teamId, categoryId)
        setPagingStart(teamBoardQuery, boardId)

        val tuples = teamBoardQuery
            .limit(PAGE_SIZE.toLong())
            .groupBy(
                teamBoard.id,
                teamBoard.title,
                user.name,
                category.name,
                teamBoard.createdAt
            )
            .orderBy(teamBoard.createdAt.desc(), teamBoard.id.desc())
            .fetch()

        return tuples.stream()
            .map { tuple: Tuple -> this.convertToTeamBoardResponse(tuple) }
            .toList()
    }

    override fun findTeamBoardDetailById(teamBoardId: Long?): Optional<BoardDetailResponse> {
        val tuple = queryFactory!!.select(
            teamBoard.id,
            teamBoard.title,
            teamBoard.readCount,
            teamBoard.content,
            user.name,
            teamBoard.createdAt,
            teamBoard.user.id
        )
            .from(teamBoard)
            .innerJoin(user).on(teamBoard.user.eq(user))
            .where(teamBoard.id.eq(teamBoardId))
            .fetchOne()

        if (tuple == null) {
            return Optional.empty()
        }

        val currentUser = findCurrentUser()
        val boardDetailResponse = convertToBoardDetailResponse(tuple, currentUser)

        return Optional.of(boardDetailResponse)
    }

    override fun findPaginationInfo(teamId: Long, categoryId: Long): List<PagingResponse?>? {
        val sql = """
            SELECT subquery.id FROM (
            SELECT course_board.id as id, ROW_NUMBER() OVER (ORDER BY course_board.id DESC) AS row_num
            FROM course_board
            JOIN users ON team_board.user_id = users.id
            WHERE team_board.id = :teamId
            AND team_board.categoryId =:categoryId
            ) subquery WHERE subquery.row_num % 10 = 1 and subquery.row_num > 10
            ORDER BY subquery.row_num DESC
            """.trimIndent()

        val results: MutableList<Long?> = entityManager!!.createNativeQuery(sql)
            .setParameter("teamId", teamId)
            .setParameter("categoryId", categoryId)
            .resultList as MutableList<Long?>

        results.add(0, null)
        val index = AtomicInteger(1)

        return results.stream().map { result: Long? ->
            if (result == null) {
                return@map PagingResponse.of(index.getAndIncrement().toLong(), null as Long?) // null인 경우
            } else {
                return@map PagingResponse.of(index.getAndIncrement().toLong(), result) // boardId 값
            }
        }.toList()
    }

    private fun buildTeamBoardListQuery(teamId: Long, categoryId: Long): JPAQuery<Tuple> {
        return queryFactory
            .select(
                teamBoard.id,
                teamBoard.title,
                user.name,
                category.name,
                teamBoard.createdAt,
                comment.count()
            )
            .from(teamBoard)
            .innerJoin(user).on(teamBoard.user.eq(user))
            .innerJoin(category).on(category.id.eq(categoryId))
            .leftJoin(comment).on(comment.board.eq(teamBoard))
            .where(
                teamBoard.team.id.eq(teamId)
                    .and(teamBoard.categoryId.eq(categoryId))
            )
    }

    private fun setPagingStart(commonQuery: JPAQuery<Tuple>, boardId: Long?) {
        if (boardId != null) {
            commonQuery.where(teamBoard.id.lt(boardId))
        }
    }

    private fun findCurrentUser(): User? {
        val currentUsername = SecurityUtil.getCurrentUsername()
        return queryFactory.selectFrom(user).where(user.username.eq(currentUsername)).fetchOne()
    }

    private fun convertToTeamBoardResponse(tuple: Tuple): TeamBoardResponse {
        return TeamBoardResponse.of(
            id = tuple[teamBoard.id],
            categoryName = tuple[category.name],
            title = tuple[teamBoard.title],
            writer = tuple[user.name],
            createdAt = tuple[teamBoard.createdAt],
            commentCount = tuple[comment.count()]?: 0L
        )
    }

    private fun convertToBoardDetailResponse(tuple: Tuple, currentUser: User?): BoardDetailResponse =
        BoardDetailResponse.of(
            id = tuple.get(courseBoard.id),
            title = tuple.get(courseBoard.title),
            readCount = Optional.ofNullable(tuple.get(courseBoard.readCount)).orElse(0L),
            content = tuple.get(courseBoard.content),
            writer = tuple.get(user.name),
            createdAt = tuple.get(courseBoard.createdAt),
            isAdmin = currentUser!!.role == Role.ADMIN,
            isMine = tuple.get(courseBoard.user.id) == currentUser.id
        )

    companion object {
        private const val PAGE_SIZE = 10
    }
}
