package org.team1.nbe1_3_team01.domain.attendance.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance;

@Repository
public interface AttendanceJpaRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findByRegistrant_UserId(Long userId);

    Optional<Attendance> findByIdAndRegistrant_UserId(Long attendanceId, Long userId);
}
