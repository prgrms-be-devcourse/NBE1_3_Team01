package org.team1.nbe1_3_team01.domain.attendance.repository

import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import org.team1.nbe1_3_team01.domain.attendance.controller.response.AttendanceResponse
import org.team1.nbe1_3_team01.domain.attendance.entity.QAttendance.attendance
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendancePersistence
import org.team1.nbe1_3_team01.domain.user.entity.QUser.user

@Component
class AttendancePersistenceImpl(
    private val queryFactory: JPAQueryFactory
) : AttendancePersistence {

    override fun findAll(): List<AttendanceResponse> {
        return queryFactory.select(
            Projections.constructor(
                AttendanceResponse::class.java,
                attendance.id,
                user.id,
                user.name,
                attendance.issueType,
                attendance.duration.startAt,
                attendance.duration.endAt,
                attendance.description
            )
        )
            .from(attendance)
            .innerJoin(user).on(attendance.registrant.userId.eq(user.id))
            .fetch()
    }

    override fun findByUsername(username: String): List<AttendanceResponse> {
        return queryFactory.select(
            Projections.constructor(
                AttendanceResponse::class.java,
                attendance.id,
                user.id,
                user.name,
                attendance.issueType,
                attendance.duration.startAt,
                attendance.duration.endAt,
                attendance.description
            )
        )
            .from(attendance)
            .innerJoin(user).on(attendance.registrant.userId.eq(user.id))
            .where(user.username.eq(username))
            .fetch()
    }

    override fun findById(id: Long): AttendanceResponse? {
        return queryFactory.select(
            Projections.constructor(
                AttendanceResponse::class.java,
                attendance.id,
                user.id,
                user.name,
                attendance.issueType,
                attendance.duration.startAt,
                attendance.duration.endAt,
                attendance.description
            )
        )
            .from(attendance)
            .where(attendance.id.eq(id))
            .fetchFirst()
    }

    override fun findByIdAndUsername(id: Long, username: String): AttendanceResponse? {
        return queryFactory.select(
            Projections.constructor(
                AttendanceResponse::class.java,
                attendance.id,
                user.id,
                user.name,
                attendance.issueType,
                attendance.duration.startAt,
                attendance.duration.endAt,
                attendance.description
            )
        )
            .from(attendance)
            .innerJoin(user).on(attendance.registrant.userId.eq(user.id))
            .where(attendance.id.eq(id).and(user.username.eq(username)))
            .fetchFirst()
    }
}
