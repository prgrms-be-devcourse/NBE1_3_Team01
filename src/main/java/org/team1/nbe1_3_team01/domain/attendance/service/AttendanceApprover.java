package org.team1.nbe1_3_team01.domain.attendance.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_3_team01.domain.attendance.service.response.AttendanceIdResponse;

@Component
@RequiredArgsConstructor
public class AttendanceApprover {

    private final AttendanceReader attendanceReader;
    private final AttendanceDeleter attendanceDeleter;

    @Transactional
    public AttendanceIdResponse approve(Long attendanceId) {
        Attendance attendance = attendanceReader.get(attendanceId);

        attendance.approve();

        /**
         * @TODO 출결 일정 등록 로직 구현
         */
        /*belongingRepository.findByRegistrant_UserId(attendance.getUser().getId())
                .ifPresent(belonging -> registAttendanceSchedule(attendance, belonging));*/

        return AttendanceIdResponse.from(attendance.getId());
    }

    /*private void registAttendanceSchedule(Attendance attendance, Belonging belonging) {
        Long teamId = belonging.getTeam().getId();
        ScheduleCreateRequest scheduleCreateRequest = ScheduleCreateRequest.builder()
                .name("출결 이슈")
                .scheduleType(ScheduleType.ATTENDANCE)
                .startAt(attendance.getStartAt())
                .endAt(attendance.getEndAt())
                .description(attendance.getDescription())
                .build();
        teamScheduleService.registSchedule(teamId, scheduleCreateRequest);
    }*/

    /**
     * @TODO soft delete 전략 고민
     */
    @Transactional
    public void reject(Long attendanceId) {
        Attendance attendance = attendanceReader.get(attendanceId);

        attendanceDeleter.delete(attendance);
    }
}
