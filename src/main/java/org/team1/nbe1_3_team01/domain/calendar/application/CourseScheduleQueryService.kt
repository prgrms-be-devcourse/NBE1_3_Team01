package org.team1.nbe1_3_team01.domain.calendar.application;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.SCHEDULE_NOT_FOUND;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.calendar.application.port.CourseScheduleRepository;
import org.team1.nbe1_3_team01.domain.calendar.entity.CourseSchedule;
import org.team1.nbe1_3_team01.domain.calendar.application.response.ScheduleResponse;
import org.team1.nbe1_3_team01.global.exception.AppException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseScheduleQueryService {

    private final CourseScheduleRepository courseScheduleRepository;

    public List<ScheduleResponse> getCourseSchedules(Long courseId) {
        List<CourseSchedule> courseSchedules = courseScheduleRepository.findByCourseId(courseId);

        return courseSchedules.stream()
                .map(ScheduleResponse::from)
                .toList();
    }

    public ScheduleResponse getCourseSchedule(Long scheduleId) {
        CourseSchedule courseSchedule = courseScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException(SCHEDULE_NOT_FOUND));

        return ScheduleResponse.from(courseSchedule);
    }
}
