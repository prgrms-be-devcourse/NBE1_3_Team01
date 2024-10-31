package org.team1.nbe1_3_team01.domain.user.repository
import com.querydsl.jpa.impl.JPAQueryFactory
import org.team1.nbe1_3_team01.domain.user.entity.QUser
import org.team1.nbe1_3_team01.domain.user.entity.Role
import org.team1.nbe1_3_team01.domain.user.entity.User

class CustomUserRepositoryImpl(private val queryFactory: JPAQueryFactory) : CustomUserRepository {

    override fun findAllUsersByIdList(userIds: List<Long>): List<User>? {
        return queryFactory.selectFrom(QUser.user)
            .where(QUser.user.id.`in`(userIds))
            .fetch()
    }

    override fun findUsersAndAdminsByCourseId(courseId: Long): List<User>? {
        return queryFactory.selectFrom(QUser.user)
            .where(QUser.user.role.eq(Role.ADMIN).or(QUser.user.course.id.eq(courseId)))
            .fetch()
    }
}
