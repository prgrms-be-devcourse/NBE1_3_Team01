package org.team1.nbe1_3_team01.domain.attendance.repository

import com.querydsl.core.types.ConstructorExpression
import com.querydsl.core.types.Projections
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.team1.nbe1_3_team01.domain.attendance.controller.response.AttendanceResponse
import org.team1.nbe1_3_team01.domain.attendance.entity.ApprovalState
import org.team1.nbe1_3_team01.domain.attendance.entity.QAttendance.attendance
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendancePersistence
import org.team1.nbe1_3_team01.domain.user.entity.QUser.user
import org.team1.nbe1_3_team01.domain.user.entity.Role

@Component
class AttendancePersistenceImpl(
    private val queryFactory: JPAQueryFactory
) : AttendancePersistence {
    override fun findAll(pageable: Pageable): Page<AttendanceResponse> {
        val attendanceResponses: List<AttendanceResponse> = queryFactory.select(
            createAttendanceResponseExpression()
        )
            .from(attendance)
            .innerJoin(user).on(attendance.registrant.userId.eq(user.id))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count: Long = queryFactory.select(attendance.count())
            .from(attendance)
            .fetchFirst()!!

        return PageImpl(attendanceResponses, pageable, count)
    }

    override fun findStudentAttendances(pageable: Pageable): Page<AttendanceResponse> {
        val attendanceResponses: List<AttendanceResponse> = queryFactory.select(
            createAttendanceResponseExpression()
        )
            .from(attendance)
            .innerJoin(user).on(attendance.registrant.userId.eq(user.id))
            .where(user.role.eq(Role.USER))
            .where(attendance.approvalState.eq(ApprovalState.PENDING))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count: Long = queryFactory.select(attendance.count())
            .from(attendance)
            .fetchFirst()!!

        return PageImpl(attendanceResponses, pageable, count)
    }

    override fun findByUsername(pageable: Pageable, username: String): Page<AttendanceResponse> {
        val attendanceResponses: List<AttendanceResponse> = queryFactory.select(
            createAttendanceResponseExpression()
        )
            .from(attendance)
            .innerJoin(user).on(attendance.registrant.userId.eq(user.id))
            .where(user.username.eq(username))
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val count: Long = queryFactory.select(attendance.count())
            .from(attendance)
            .fetchFirst()!!

        return PageImpl(attendanceResponses, pageable, count)
    }

    override fun findById(id: Long): AttendanceResponse? {
        return queryFactory.select(
            createAttendanceResponseExpression()
        )
            .from(attendance)
            .where(attendance.id.eq(id))
            .fetchFirst()
    }

    override fun findByIdAndUsername(id: Long, username: String): AttendanceResponse? {
        return queryFactory.select(
            createAttendanceResponseExpression()
        )
            .from(attendance)
            .innerJoin(user).on(attendance.registrant.userId.eq(user.id))
            .where(attendance.id.eq(id).and(user.username.eq(username)))
            .fetchFirst()
    }

    private fun createAttendanceResponseExpression(): ConstructorExpression<AttendanceResponse> =
        Projections.constructor(
            AttendanceResponse::class.java,
            attendance.id,
            user.id,
            user.name,
            attendance.approvalState,
            attendance.issueType,
            attendance.duration.startAt,
            attendance.duration.endAt,
            attendance.description
        )
}
