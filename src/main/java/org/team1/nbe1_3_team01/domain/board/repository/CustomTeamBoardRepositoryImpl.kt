package org.team1.nbe1_3_team01.domain.board.repository

import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import lombok.RequiredArgsConstructor
import org.team1.nbe1_3_team01.domain.board.entity.QCategory.category
import org.team1.nbe1_3_team01.domain.board.entity.QComment.comment
import org.team1.nbe1_3_team01.domain.board.entity.QTeamBoard.teamBoard
import org.team1.nbe1_3_team01.domain.board.entity.QTeamReadCount.teamReadCount
import org.team1.nbe1_3_team01.domain.board.entity.TeamReadCount
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse
import org.team1.nbe1_3_team01.domain.board.service.response.PagingResponse
import org.team1.nbe1_3_team01.domain.board.service.response.TeamBoardResponse
import org.team1.nbe1_3_team01.domain.user.entity.QUser.user
import org.team1.nbe1_3_team01.domain.user.entity.Role
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.global.util.SecurityUtil
import java.util.concurrent.atomic.AtomicInteger

@RequiredArgsConstructor
class CustomTeamBoardRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
    private val entityManager: EntityManager
) : CustomTeamBoardRepository {

    override fun findAllTeamBoardByType (
        teamId: Long,
        categoryId: Long?,
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

    override fun findTeamBoardDetailById(teamBoardId: Long): BoardDetailResponse? {
        val tuple = queryFactory.select(
            teamBoard.id,
            teamBoard.title,
            teamReadCount.id.count(),
            teamBoard.content,
            user.name,
            teamBoard.createdAt,
            teamBoard.user.id
        )
            .from(teamBoard)
            .innerJoin(user).on(teamBoard.user.eq(user))
            .leftJoin(teamReadCount).on(teamReadCount.teamBoardId.eq(teamBoard.id))
            .where(teamBoard.id.eq(teamBoardId))
            .fetchOne()

        return if(tuple == null) null else {
            var flag = false
            val currentUser = findCurrentUser()
            val readLog = queryFactory.selectFrom(teamReadCount)
                .where(teamReadCount.teamBoardId.eq(teamBoardId)
                    .and(teamReadCount.userId.eq(currentUser!!.id)))
                .fetchOne()

            if (readLog == null && !tuple.get(teamBoard.user.id)!!.equals(currentUser.id)) {
                val newReadCount = TeamReadCount(
                    userId = currentUser.id!!,
                    teamBoardId = teamBoardId
                )
                entityManager.persist(newReadCount)
                flag = true
            }

            convertToBoardDetailResponse(tuple, currentUser, flag)
        }
    }

    override fun findPaginationInfo(teamId: Long, categoryId: Long?): List<PagingResponse> {
        val sql = if (categoryId == null) {
            """
               SELECT subquery.id FROM (
               SELECT team_board.id as id, ROW_NUMBER() OVER (ORDER BY team_board.id DESC) AS row_num
               FROM team_board
               WHERE team_board.id = :teamId
               ) subquery WHERE subquery.row_num % 10 = 1 and subquery.row_num > 10
               ORDER BY subquery.row_num DESC")
            """.trimIndent()
        } else {
            """
                SELECT subquery.id FROM (
                SELECT team_board.id as id, ROW_NUMBER() OVER (ORDER BY team_board.id DESC) AS row_num
                FROM team_board
                WHERE team_board.id = :teamId
                AND team_board.categoryId = :categoryId
                ) subquery WHERE subquery.row_num % 10 = 1 and subquery.row_num > 10
                ORDER BY subquery.row_num DESC
            """.trimIndent()
        }

        val query = entityManager.createNativeQuery(sql)
            .setParameter("teamId", teamId)
        if (categoryId != null) {
            query.setParameter("categoryId", categoryId)
        }
        val results: MutableList<Long?> = (query.resultList as MutableList<Long?>).apply {

            add(0, null)
        }
        val index = AtomicInteger(1)

        return results.stream().map<Any> { result: Long? ->
            when (result) {
                null -> {
                    return@map PagingResponse.of(index.getAndIncrement().toLong(), null as Long?) // null인 경우
                }
                else -> {
                    return@map PagingResponse.of(index.getAndIncrement().toLong(), result) // boardId 값
                }
            }
        }.toList() as List<PagingResponse>
    }

    private fun buildTeamBoardListQuery(teamId: Long, categoryId: Long?): JPAQuery<Tuple> {
        val jpaQuery = queryFactory
            .select(
                teamBoard.id,
                teamBoard.title,
                user.name,
                category.name,
                teamReadCount.id.count(),
                teamBoard.createdAt,
                comment.count()
            )
            .from(teamBoard)
            .innerJoin(user).on(teamBoard.user.eq(user))
            .leftJoin(teamReadCount).on(teamReadCount.teamBoardId.eq(teamBoard.id))
            .leftJoin(category).on(category.id.eq(categoryId))
            .leftJoin(comment).on(comment.board.eq(teamBoard))
            .where(teamBoard.team.id.eq(teamId))

        if(categoryId != null) {
            jpaQuery.where(teamBoard.categoryId.eq(categoryId))
        }

        return jpaQuery
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
            categoryName = tuple[category.name] ?: "기타",
            title = tuple[teamBoard.title],
            writer = tuple[user.name],
            createdAt = tuple[teamBoard.createdAt],
            readCount = tuple[teamReadCount.id.count()] ?: 0L,
            commentCount = tuple[comment.count()]?: 0L
        )
    }

    private fun convertToBoardDetailResponse(tuple: Tuple, currentUser: User?, flag: Boolean): BoardDetailResponse {
        val addCount = if (flag) 1L else 0L
        return BoardDetailResponse.of(
            id = tuple.get(teamBoard.id),
            title = tuple.get(teamBoard.title),
            readCount = (tuple.get(teamReadCount.id.count()) ?: 0L) + addCount,
            content = tuple.get(teamBoard.content),
            writer = tuple.get(user.name),
            categoryName = tuple.get(category.name) ?: "기타",
            createdAt = tuple.get(teamBoard.createdAt),
            isAdmin = currentUser!!.role == Role.ADMIN,
            isMine = tuple.get(teamBoard.user.id) == currentUser.id
        )
    }

    companion object {
        private const val PAGE_SIZE = 10
    }
}
