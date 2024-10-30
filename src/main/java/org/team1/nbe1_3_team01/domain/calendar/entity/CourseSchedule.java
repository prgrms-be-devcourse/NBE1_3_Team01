package org.team1.nbe1_3_team01.domain.calendar.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleUpdateRequest;
import org.team1.nbe1_3_team01.domain.user.entity.Course;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "course_schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private Course course;

    @Builder
    private CourseSchedule(
            Course course,
            String name,
            LocalDateTime startAt,
            LocalDateTime endAt,
            String description) {
        this.course = course;
        this.name = name;
        this.startAt = startAt;
        this.endAt = endAt;
        this.description = description;
        course.addCourseSchedule(this);
    }

    public void update(ScheduleUpdateRequest scheduleUpdateRequest) {
        this.name = scheduleUpdateRequest.name();
        this.startAt = scheduleUpdateRequest.startAt();
        this.endAt = scheduleUpdateRequest.endAt();
        this.description = scheduleUpdateRequest.description();
    }
}
