package org.team1.nbe1_3_team01.domain.user.entity

import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import org.hibernate.annotations.SQLRestriction
import org.team1.nbe1_3_team01.domain.board.entity.CourseBoard
import org.team1.nbe1_3_team01.domain.calendar.entity.CourseSchedule
import org.team1.nbe1_3_team01.domain.group.entity.Team

@Entity
@Getter
@Table(name = "course")
@SQLRestriction("is_delete = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Course @Builder private constructor(private var name: String) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Column(columnDefinition = "TINYINT(1)")
    private var isDelete = false

    @OneToMany(mappedBy = "course")
    private val users: MutableList<User> = ArrayList()

    @OneToMany(mappedBy = "course")
    private val teams: MutableList<Team> = ArrayList()

    @OneToMany(mappedBy = "course")
    private val courseSchedules: MutableList<CourseSchedule> = ArrayList()

    @OneToMany(mappedBy = "course")
    private val courseBoards: MutableList<CourseBoard> = ArrayList()

    fun addUser(user: User) {
        users.add(user)
    }

    fun addTeam(team: Team) {
        teams.add(team)
    }

    fun addCourseBoard(courseBoard: CourseBoard) {
        courseBoards.add(courseBoard)
    }

    fun addCourseSchedule(courseSchedule: CourseSchedule) {
        courseSchedules.add(courseSchedule)
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun delete() {
        this.isDelete = true
    }
}
