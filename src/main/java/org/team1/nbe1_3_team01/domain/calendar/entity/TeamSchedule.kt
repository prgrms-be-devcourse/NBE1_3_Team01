package org.team1.nbe1_3_team01.domain.calendar.entity

import jakarta.persistence.*
import org.team1.nbe1_3_team01.domain.group.entity.Team
import java.time.LocalDateTime

@Entity
@Table(name = "team_schedule")
class TeamSchedule(

    @Column(length = 50)
    var name: String,

    @Enumerated(value = EnumType.STRING)
    var scheduleType: ScheduleType,

    var startAt: LocalDateTime,

    var endAt: LocalDateTime,

    var description: String,

    @JoinColumn(name = "team_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val team: Team
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
        protected set

    fun update(
        name: String,
        scheduleType: ScheduleType,
        startAt: LocalDateTime,
        endAt: LocalDateTime,
        description: String
    ) {
        this.name = name
        this.scheduleType = scheduleType
        this.startAt = startAt
        this.endAt = endAt
        this.description = description
    }
}
