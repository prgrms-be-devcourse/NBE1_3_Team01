package org.team1.nbe1_3_team01.domain.attendance.service;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.ATTENDANCE_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.nbe1_3_team01.domain.attendance.controller.response.AttendanceResponse;
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendancePersistence;
import org.team1.nbe1_3_team01.global.exception.AppException;

@Service
@RequiredArgsConstructor
public class AttendanceQueryService {

    private final AttendancePersistence attendancePersistence;

    public List<AttendanceResponse> getAll() {
        return attendancePersistence.findAll();
    }

    public List<AttendanceResponse> getMyAttendances(String currentUsername) {
        return attendancePersistence.findByUsername(currentUsername);
    }

    public AttendanceResponse getById(Long id) {
        return attendancePersistence.findById(id)
                .orElseThrow(() -> new AppException(ATTENDANCE_NOT_FOUND));
    }

    public AttendanceResponse getByIdAndUserId(Long attendanceId, String currentUsername) {
        return attendancePersistence.findByIdAndUsername(attendanceId, currentUsername)
                .orElseThrow(() -> new AppException(ATTENDANCE_NOT_FOUND));
    }
}
