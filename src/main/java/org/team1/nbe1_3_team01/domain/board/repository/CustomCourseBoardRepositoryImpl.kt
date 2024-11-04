package org.team1.nbe1_3_team01.domain.board.repository

import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import lombok.RequiredArgsConstructor
import org.team1.nbe1_3_team01.domain.board.constants.CommonBoardType
import org.team1.nbe1_3_team01.domain.board.entity.CourseReadCount
import org.team1.nbe1_3_team01.domain.board.entity.QCourseBoard.courseBoard
import org.team1.nbe1_3_team01.domain.board.entity.QCourseReadCount.courseReadCount
import org.team1.nbe1_3_team01.domain.board.entity.QTeamBoard
import org.team1.nbe1_3_team01.domain.board.entity.QTeamReadCount
import org.team1.nbe1_3_team01.domain.board.entity.TeamReadCount
import org.team1.nbe1_3_team01.domain.board.service.converter.DateTimeToStringConverter
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse
import org.team1.nbe1_3_team01.domain.board.service.response.CourseBoardResponse
import org.team1.nbe1_3_team01.domain.board.service.response.PagingResponse
import org.team1.nbe1_3_team01.domain.user.entity.QUser.user
import org.team1.nbe1_3_team01.domain.user.entity.Role
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.global.util.SecurityUtil
import java.util.concurrent.atomic.AtomicInteger

@RequiredArgsConstructor
class CustomCourseBoardRepositoryImpl(
    private val queryFactory: JPAQueryFactory,
    private val entityManager: EntityManager
) : CustomCourseBoardRepository {
    override fun findAllCourseBoard(
        type: CommonBoardType,
        courseId: Long,
        boardId: Long?
    ): List<CourseBoardResponse> {
        val query = buildBoardQueryByType(type, courseId)
        setPagingStart(query, boardId)

        val results = query
            .limit(PAGE_SIZE.toLong())
            .orderBy(courseBoard.createdAt.desc(), courseBoard.id.desc())
            .groupBy(courseBoard.id,
                courseBoard.title,
                user.name,
                courseBoard.createdAt)
            .fetch()

        return results.stream()
            .map { tuple: Tuple -> this.convertToCourseBoardResponse(tuple) }
            .toList()
    }

    override fun findCourseBoardDetailById(courseId: Long): BoardDetailResponse? {
        val tuple = queryFactory.select(
            courseBoard.id,
            courseBoard.title,
            courseReadCount.id.count(),
            courseBoard.content,
            user.name,
            courseBoard.createdAt,
            courseBoard.user.id
        )
            .from(courseBoard)
            .innerJoin(user).on(courseBoard.user.eq(user))
            .leftJoin(courseReadCount).on(courseReadCount.courseBoardId.eq(courseBoard.id))
            .where(courseBoard.id.eq(courseId))
            .groupBy(courseBoard.id,
                courseBoard.title,
                courseBoard.content,
                user.name,
                courseBoard.createdAt,
                courseBoard.user.id)
            .fetchOne()

        return if(tuple == null) null else {
            var flag = false
            val currentUser = findCurrentUser()
            val readLog = queryFactory.selectFrom(courseReadCount)
                .where(
                    courseReadCount.courseBoardId.eq(courseId)
                    .and(courseReadCount.userId.eq(currentUser!!.id)))
                .fetchOne()

            if (readLog == null && !tuple.get(courseBoard.user.id)!!.equals(currentUser.id)) {
                val newReadCount = CourseReadCount(
                    userId = currentUser.id!!,
                    courseBoardId = courseId
                )
                entityManager.persist(newReadCount)
                flag = true
            }

            convertToBoardDetailResponse(tuple, currentUser, flag)
        }
    }

    override fun findPaginationInfo(courseId: Long, boardType: CommonBoardType): List<PagingResponse> {
        val sql = """
            SELECT subquery.id FROM (
            SELECT course_board.id as id, ROW_NUMBER() OVER (ORDER BY course_board.id DESC) AS row_num
            FROM course_board
            JOIN users ON course_board.user_id = users.id
            WHERE course_board.course_id = :courseId
            AND users.role = :role
            ) subquery WHERE subquery.row_num % 10 = 1 and subquery.row_num > 10
            ORDER BY subquery.row_num DESC
            """.trimIndent()

        val role = getRole(boardType)
        val results: MutableList<Long?> = (entityManager.createNativeQuery(sql)
            .setParameter("courseId", courseId)
            .setParameter("role", role)
            .resultList as MutableList<Long?>).apply {

            add(0, null)
        }
        val index = AtomicInteger(1)

        return results.stream().map { result: Long? ->
            when (result) {
                null -> return@map PagingResponse.of(index.getAndIncrement().toLong(), null as Long?) // null인 경우
                else -> return@map PagingResponse.of(index.getAndIncrement().toLong(), result) // boardId 값
            }
        }.toList()
    }


    private fun buildBoardQueryByType(type: CommonBoardType, courseId: Long): JPAQuery<Tuple> {
        val commonQuery = queryFactory
            .select(
                courseBoard.id,
                courseBoard.title,
                courseReadCount.id.count(),
                user.name,
                courseBoard.createdAt
            )
            .from(courseBoard)
            .innerJoin(user).on(courseBoard.user.eq(user))
            .leftJoin(courseReadCount).on((courseReadCount.courseBoardId.eq(courseBoard.id)))
            .where(courseBoard.course.id.eq(courseId))

        if (type == CommonBoardType.NOTICE) {
            return commonQuery.where(user.role.eq(Role.ADMIN))
        }

        return commonQuery.where(user.role.eq(Role.USER))
    }

    private fun findCurrentUser(): User? {
        val currentUsername = SecurityUtil.getCurrentUsername()
        return queryFactory.selectFrom(user).where(user.username.eq(currentUsername)).fetchOne()
    }

    private fun setPagingStart(commonQuery: JPAQuery<Tuple>, boardId: Long?) {
        if (boardId != null) {
            commonQuery.where(courseBoard.id.lt(boardId))
        }
    }

    private fun convertToCourseBoardResponse(tuple: Tuple): CourseBoardResponse =
        CourseBoardResponse(
            id = tuple.get(courseBoard.id),
            title = tuple.get(courseBoard.title),
            writer = tuple.get(user.name),
            readCount = tuple.get(courseReadCount.id.count(),) ?: 0L,
            createdAt = DateTimeToStringConverter.convert(tuple.get(courseBoard.createdAt))
        )


    private fun convertToBoardDetailResponse(tuple: Tuple, currentUser: User?, flag: Boolean): BoardDetailResponse {
        val addCount = if (flag) 1L else 0L
        return BoardDetailResponse(
            id = tuple.get(courseBoard.id),
            title = tuple.get(courseBoard.title),
            readCount = (tuple.get(courseReadCount.id.count(),) ?: 0L) + addCount,
            content = tuple.get(courseBoard.content),
            writer = tuple.get(user.name),
            createdAt = DateTimeToStringConverter.convert(tuple.get(courseBoard.createdAt)),
            isAdmin = currentUser!!.role == Role.ADMIN,
            categoryName = null,
            isMine = tuple.get(courseBoard.user.id) == currentUser.id
        )

    }

    companion object {
        private const val PAGE_SIZE = 10

        private fun getRole(boardType: CommonBoardType): String =
             if (boardType == CommonBoardType.NOTICE) "ADMIN" else "USER"
    }
}
