package org.team1.nbe1_3_team01.domain.attendance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceUpdateRequest;
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository;

@Component
@RequiredArgsConstructor
public class AttendanceUpdater {

    private final AttendanceRepository attendanceRepository;

    @Transactional
    public Long update(
            Attendance attendance,
            AttendanceUpdateRequest attendanceUpdateRequest
    ) {
        attendance.update(attendanceUpdateRequest);

        Attendance saved = attendanceRepository.save(attendance);

        return saved.getId();
    }
}
