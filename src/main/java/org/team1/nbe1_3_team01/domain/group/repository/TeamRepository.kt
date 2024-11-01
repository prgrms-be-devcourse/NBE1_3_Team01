package org.team1.nbe1_3_team01.domain.group.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.team1.nbe1_3_team01.domain.group.entity.Team
import java.util.*

interface TeamRepository : JpaRepository<Team, Long> {
    fun findByCreationWaiting(waiting: Boolean): List<Team>

    @Query(
        "select t " +
                "from Team t " +
                "join fetch t.belongings b " +
                "where b.isOwner = true and t.id = :teamId"
    )
    fun findByIdWithLeaderBelonging(@Param("teamId") teamId: Long): Team?

    @Query(
        "select t " +
                "from Team t " +
                "join fetch t.course c " +
                "where c.id = :courseId"
    )
    fun findByCourseId(@Param("courseId") courseId: Long): List<Team>
}
