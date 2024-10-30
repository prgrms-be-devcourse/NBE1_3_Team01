package org.team1.nbe1_3_team01.domain.group.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.team1.nbe1_3_team01.domain.group.entity.Belonging;
import org.team1.nbe1_3_team01.domain.user.entity.User;

import java.util.Optional;

public interface BelongingRepository extends JpaRepository<Belonging, Long> {

    @Query("select u " +
            "from Belonging b " +
            "join b.user u " +
            "left join u.course c " +
            "where b.team.id = :teamId")
    List<User> findUsersByTeamId(@Param("teamId") Long teamId);

    @Query("select b " +
            "from Belonging b " +
            "join fetch b.team t " +
            "join fetch b.user u " +
            "where t.id = :teamId")
    List<Belonging> findAllByTeamId_JoinTeamAndUser(@Param("teamId") Long teamId);

    @Modifying
    @Query("delete " +
            "from Belonging b " +
            "where b.team.id = :teamId and b.isOwner = false and b.user.id in :userIds"
    )
    int deleteBelongings(@Param("teamId") Long teamId, @Param("userIds") List<Long> userIds);

    Belonging findByTeamIdAndIsOwner(Long teamId, boolean isOwner);

    Optional<Belonging> findByTeam_IdAndUser_Username(Long teamId, String username);

    Optional<Belonging> findByUserId(Long userId);

    boolean existsByTeamIdAndUserId(Long teamId, Long userId);

    @Query("select b " +
            "from Belonging b " +
            "join fetch b.team t " +
            "join fetch t.course c " +
            "where b.user = :user")
    List<Belonging> findByUser(@Param("user") User user);

}
