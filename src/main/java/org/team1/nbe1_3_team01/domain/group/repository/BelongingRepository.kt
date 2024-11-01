package org.team1.nbe1_3_team01.domain.group.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.team1.nbe1_3_team01.domain.group.entity.Belonging
import org.team1.nbe1_3_team01.domain.user.entity.User
import java.util.*

interface BelongingRepository : JpaRepository<Belonging?, Long?> {
    @Query(
        "select u " +
                "from Belonging b " +
                "join b.user u " +
                "left join u.course c " +
                "where b.team.id = :teamId"
    )
    fun findUsersByTeamId(@Param("teamId") teamId: Long): List<User>

    @Query(
        "select b " +
                "from Belonging b " +
                "join fetch b.team t " +
                "join fetch b.user u " +
                "where t.id = :teamId"
    )
    fun findAllByTeamId_JoinTeamAndUser(@Param("teamId") teamId: Long): List<Belonging>

    @Modifying
    @Query(
        "delete " +
                "from Belonging b " +
                "where b.team.id = :teamId and b.isOwner = false and b.user.id in :userIds"
    )
    fun deleteBelongings(@Param("teamId") teamId: Long, @Param("userIds") userIds: List<Long>): Int

    fun findByTeamIdAndIsOwner(teamId: Long, isOwner: Boolean): Belonging

    fun findByTeam_IdAndUser_Username(teamId: Long, username: String): Belonging?

    fun findByUserId(userId: Long): List<Belonging>

    fun existsByTeamIdAndUserId(teamId: Long, userId: Long): Boolean

    @Query(
        "select b " +
                "from Belonging b " +
                "join fetch b.team t " +
                "join fetch t.course c " +
                "where b.user = :user"
    )
    fun findByUser(@Param("user") user: User): List<Belonging>
}
