package org.team1.nbe1_3_team01.domain.attendance.service.port;

import java.util.List;
import java.util.Optional;
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance;

public interface AttendanceRepository {

    Optional<Attendance> findById(Long id);

    List<Attendance> findByUserId(Long userId);

    List<Attendance> findAll();

    Attendance save(Attendance attendance);

    void deleteById(Long id);
}
