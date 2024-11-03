package org.team1.nbe1_3_team01.domain.attendance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository;

@Component
@RequiredArgsConstructor
public class AttendanceDeleter {

    private final AttendanceRepository attendanceRepository;

    @Transactional
    public void delete(Attendance attendance) {
        attendanceRepository.deleteById(attendance.getId());
    }
}
