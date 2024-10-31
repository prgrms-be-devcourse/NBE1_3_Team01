package org.team1.nbe1_3_team01.domain.board.comment.repository

import com.querydsl.core.Tuple
import com.querydsl.jpa.impl.JPAQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import lombok.RequiredArgsConstructor
import org.team1.nbe1_3_team01.domain.board.comment.service.response.CommentResponse
import org.team1.nbe1_3_team01.domain.board.comment.service.response.CommentResponse.Companion.of
import org.team1.nbe1_3_team01.domain.board.entity.QComment.comment
import org.team1.nbe1_3_team01.domain.user.entity.QUser.user
import org.team1.nbe1_3_team01.domain.user.entity.Role
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.global.util.SecurityUtil

@RequiredArgsConstructor
class CustomCommentRepositoryImpl(
    private val queryFactory: JPAQueryFactory
): CustomCommentRepository {


    override fun getCommentsByBoardId(boardId: Long?, lastCommentId: Long?): List<CommentResponse> {
        val initialQuery = queryFactory.select(
            comment.id,
            user.username,
            comment.content,
            comment.createdAt,
            user.id
        )
            .from(comment)
            .innerJoin(user).on(comment.user.eq(user))
            .where(comment.board.id.eq(boardId))

        setPagingStart(initialQuery, lastCommentId)

        val tuples = initialQuery
            .limit(PAGE_SIZE.toLong())
            .orderBy(comment.createdAt.desc(), comment.id.desc())
            .fetch()

        val currentUser = findCurrentUser()
        return convertToResponses(tuples, currentUser)
    }

    private fun setPagingStart(query: JPAQuery<Tuple>, commentId: Long?) {
        if (commentId != null) {
            query.where(comment.id.lt(commentId))
        }
    }

    private fun findCurrentUser(): User? {
        val currentUsername = SecurityUtil.getCurrentUsername()
        return queryFactory.selectFrom(user).where(user.username.eq(currentUsername)).fetchOne()
    }

    companion object {
        private const val PAGE_SIZE = 10

        private fun convertToResponses(tuples: List<Tuple>, currentUser: User?): List<CommentResponse> {
            return tuples.stream().map { tuple: Tuple ->
                of(
                    tuple.get(comment.id),
                    tuple.get(user.username),
                    tuple.get(comment.content),
                    tuple.get(comment.createdAt),
                    currentUser!!.role == Role.ADMIN,
                    tuple.get(user.id) == currentUser.id
                )
            }.toList()
        }
    }
}
