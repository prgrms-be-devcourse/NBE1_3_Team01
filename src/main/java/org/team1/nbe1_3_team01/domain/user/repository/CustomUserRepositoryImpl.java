package org.team1.nbe1_3_team01.domain.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.team1.nbe1_3_team01.domain.user.entity.Role;
import org.team1.nbe1_3_team01.domain.user.entity.User;

import java.util.List;

import static org.team1.nbe1_3_team01.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class CustomUserRepositoryImpl implements CustomUserRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<User> findAllUsersByIdList(List<Long> userIds) {
        return queryFactory.selectFrom(user)
                .where(user.id.in(userIds))
                .fetch();
    }

    @Override
    public List<User> findUsersAndAdminsByCourseId(Long courseId) {
        return queryFactory.selectFrom(user)
                .where(user.role.eq(Role.ADMIN).or(user.course.id.eq(courseId)))
                .fetch();
    }
}
