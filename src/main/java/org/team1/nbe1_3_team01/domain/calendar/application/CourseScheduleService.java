package org.team1.nbe1_3_team01.domain.calendar.application;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.COURSE_NOT_FOUND;
import static org.team1.nbe1_3_team01.global.util.ErrorCode.SCHEDULE_NOT_FOUND;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.calendar.application.port.CourseScheduleRepository;
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleCreateRequest;
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleUpdateRequest;
import org.team1.nbe1_3_team01.domain.calendar.entity.CourseSchedule;
import org.team1.nbe1_3_team01.domain.calendar.application.response.ScheduleIdResponse;
import org.team1.nbe1_3_team01.domain.user.entity.Course;
import org.team1.nbe1_3_team01.domain.user.repository.CourseRepository;
import org.team1.nbe1_3_team01.global.exception.AppException;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseScheduleService {

    private final CourseScheduleRepository courseScheduleRepository;
    private final CourseRepository courseRepository;

    public ScheduleIdResponse registSchedule(Long courseId, ScheduleCreateRequest scheduleCreateRequest) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(COURSE_NOT_FOUND));

        CourseSchedule courseSchedule = scheduleCreateRequest.toCourseSchedule(course);

        CourseSchedule saved = courseScheduleRepository.save(courseSchedule);
        return ScheduleIdResponse.from(saved.getId());
    }

    public ScheduleIdResponse updateSchedule(ScheduleUpdateRequest scheduleUpdateRequest) {
        CourseSchedule courseSchedule = courseScheduleRepository.findById(scheduleUpdateRequest.id())
                .orElseThrow(() -> new AppException(SCHEDULE_NOT_FOUND));

        courseSchedule.update(scheduleUpdateRequest);

        return ScheduleIdResponse.from(courseSchedule.getId());
    }

    public void deleteSchedule(Long scheduleId) {
        CourseSchedule courseSchedule = courseScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new AppException(SCHEDULE_NOT_FOUND));

        courseScheduleRepository.delete(courseSchedule);
    }
}
