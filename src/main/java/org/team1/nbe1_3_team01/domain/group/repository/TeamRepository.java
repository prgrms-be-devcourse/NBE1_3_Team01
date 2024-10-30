package org.team1.nbe1_3_team01.domain.group.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.team1.nbe1_3_team01.domain.group.entity.Team;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    List<Team> findByCreationWaiting(boolean waiting);

    void deleteById(Long id);

    @Query("select t " +
            "from Team t " +
            "join fetch t.belongings b " +
            "where b.isOwner = true and t.id = :teamId")
    Optional<Team> findByIdWithLeaderBelonging(@Param("teamId") Long teamId);

    @Query("select t " +
            "from Team t " +
            "join fetch t.course c " +
            "where c.id = :courseId")
    List<Team> findByCourseId(@Param("courseId") Long courseId);
}
