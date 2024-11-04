package org.team1.nbe1_3_team01.domain.calendar.entity

import jakarta.persistence.*
import org.team1.nbe1_3_team01.domain.user.entity.Course
import java.time.LocalDateTime

@Entity
@Table(name = "course_schedule")
class CourseSchedule(

    @Column(length = 50)
    var name: String,

    var startAt: LocalDateTime,

    var endAt: LocalDateTime,

    var description: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    var course: Course
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L
        protected set

    fun update(
        name: String,
        startAt: LocalDateTime,
        endAt: LocalDateTime,
        description: String
    ) {
        this.name = name
        this.startAt = startAt
        this.endAt = endAt
        this.description = description
    }
}
