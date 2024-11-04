package org.team1.nbe1_3_team01.domain.calendar.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import org.team1.nbe1_3_team01.domain.calendar.controller.dto.ScheduleUpdateRequest;
import org.team1.nbe1_3_team01.domain.group.entity.Team;

@Entity
@Getter
@Table(name = "team_schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Enumerated(value = EnumType.STRING)
    private ScheduleType scheduleType;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Builder
    private TeamSchedule(
            Team team,
            String name,
            ScheduleType scheduleType,
            LocalDateTime startAt,
            LocalDateTime endAt,
            String description) {
        this.team = team;
        this.name = name;
        this.scheduleType = scheduleType;
        this.startAt = startAt;
        this.endAt = endAt;
        this.description = description;
        team.addTeamScheDule(this);
    }

    public void update(ScheduleUpdateRequest scheduleUpdateRequest) {
        this.name = scheduleUpdateRequest.name();
        this.scheduleType = scheduleUpdateRequest.scheduleType();
        this.startAt = scheduleUpdateRequest.startAt();
        this.endAt = scheduleUpdateRequest.endAt();
        this.description = scheduleUpdateRequest.description();
    }
}
