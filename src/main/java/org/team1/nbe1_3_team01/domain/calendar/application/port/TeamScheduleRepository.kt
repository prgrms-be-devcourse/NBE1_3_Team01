package org.team1.nbe1_3_team01.domain.calendar.application.port;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.nbe1_3_team01.domain.calendar.entity.TeamSchedule;

public interface TeamScheduleRepository extends JpaRepository<TeamSchedule, Long> {

    List<TeamSchedule> findByTeamId(Long teamId);
}
