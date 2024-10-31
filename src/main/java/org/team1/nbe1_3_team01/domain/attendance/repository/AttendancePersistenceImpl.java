package org.team1.nbe1_3_team01.domain.attendance.repository;

import org.team1.nbe1_3_team01.domain.attendance.entity.QAttendance.attendance;
import org.team1.nbe1_3_team01.domain.user.entity.QUser.user;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.team1.nbe1_3_team01.domain.attendance.controller.response.AttendanceResponse;
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendancePersistence;

@Component
@RequiredArgsConstructor
public class AttendancePersistenceImpl implements AttendancePersistence {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<AttendanceResponse> findAll() {
        return queryFactory.select(Projections.constructor(AttendanceResponse.class,
                        attendance.id,
                        user.id,
                        user.name,
                        attendance.issueType,
                        attendance.duration.startAt,
                        attendance.duration.endAt,
                        attendance.description))
                .from(attendance)
                .innerJoin(user).on(attendance.registrant.userId.eq(user.id))
                .fetch();
    }

    @Override
    public List<AttendanceResponse> findByUsername(String username) {
        return queryFactory.select(Projections.constructor(AttendanceResponse.class,
                        attendance.id,
                        user.id,
                        user.name,
                        attendance.issueType,
                        attendance.duration.startAt,
                        attendance.duration.endAt,
                        attendance.description))
                .from(attendance)
                .innerJoin(user).on(attendance.registrant.userId.eq(user.id))
                .where(user.username.eq(username))
                .fetch();
    }

    @Override
    public Optional<AttendanceResponse> findById(Long id) {
        return Optional.ofNullable(queryFactory.select(Projections.constructor(AttendanceResponse.class,
                        attendance.id,
                        user.id,
                        user.name,
                        attendance.issueType,
                        attendance.duration.startAt,
                        attendance.duration.endAt,
                        attendance.description))
                .from(attendance)
                .where(attendance.id.eq(id))
                .fetchFirst());
    }

    @Override
    public Optional<AttendanceResponse> findByIdAndUsername(Long id, String username) {
        return Optional.ofNullable(queryFactory.select(Projections.constructor(AttendanceResponse.class,
                        attendance.id,
                        user.id,
                        user.name,
                        attendance.issueType,
                        attendance.duration.startAt,
                        attendance.duration.endAt,
                        attendance.description))
                .from(attendance)
                .innerJoin(user).on(attendance.registrant.userId.eq(user.id))
                .where(attendance.id.eq(id).and(user.username.eq(username)))
                .fetchFirst());
    }
}
