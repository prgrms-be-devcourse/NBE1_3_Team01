package org.team1.nbe1_3_team01.domain.attendance.service.port;

import java.util.List;
import java.util.Optional;
import org.team1.nbe1_3_team01.domain.attendance.controller.response.AttendanceResponse;

public interface AttendancePersistence {

    List<AttendanceResponse> findAll();

    List<AttendanceResponse> findByUsername(String username);

    Optional<AttendanceResponse> findById(Long id);

    Optional<AttendanceResponse> findByIdAndUsername(Long id, String username);
}
