package org.team1.nbe1_3_team01.domain.attendance.service;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.ATTENDANCE_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository;
import org.team1.nbe1_3_team01.global.exception.AppException;

@Component
@RequiredArgsConstructor
public class AttendanceReader {

    private final AttendanceRepository attendanceRepository;

    @Transactional(readOnly = true)
    public List<Attendance> getList(Long userId) {
        return attendanceRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Attendance get(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new AppException(ATTENDANCE_NOT_FOUND));
    }
}
