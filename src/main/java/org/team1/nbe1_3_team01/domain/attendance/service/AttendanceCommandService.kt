package org.team1.nbe1_3_team01.domain.attendance.service;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.USER_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceCreateRequest;
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceUpdateRequest;
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_3_team01.domain.attendance.service.response.AttendanceIdResponse;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_3_team01.global.exception.AppException;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceCommandService {

    private final AttendanceRegistrar attendanceRegistrar;
    private final AttendanceReader attendanceReader;
    private final AttendanceUpdater attendanceUpdater;
    private final AttendanceDeleter attendanceDeleter;
    private final UserRepository userRepository;

    public AttendanceIdResponse register(
            String registrantName,
            AttendanceCreateRequest attendanceCreateRequest
    ) {
        // validate
        User registrant = getCurrentUser(registrantName);
        attendanceReader.getList(registrant.getId())
                .forEach(Attendance::validateCanRegister);

        // register
        Long attendanceId = attendanceRegistrar.register(registrant.getId(), attendanceCreateRequest);

        // parse to response
        return AttendanceIdResponse.from(attendanceId);
    }

    public AttendanceIdResponse update(
            String currentUsername,
            AttendanceUpdateRequest attendanceUpdateRequest
    ) {
        // validate
        User currentUser = getCurrentUser(currentUsername);
        Attendance attendance = attendanceReader.get(attendanceUpdateRequest.id());
        attendance.validateRegistrant(currentUser.getId());

        // update
        Long attendanceId = attendanceUpdater.update(attendance, attendanceUpdateRequest);

        // parse to response
        return AttendanceIdResponse.from(attendanceId);
    }

    public void delete(
            String currentUsername,
            Long attendanceId
    ) {
        // validate
        User currentUser = getCurrentUser(currentUsername);
        Attendance attendance = attendanceReader.get(attendanceId);
        attendance.validateRegistrant(currentUser.getId());

        // delete
        attendanceDeleter.delete(attendance);
    }

    // 타 도메인 메서드
    private User getCurrentUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(USER_NOT_FOUND));
    }
}
