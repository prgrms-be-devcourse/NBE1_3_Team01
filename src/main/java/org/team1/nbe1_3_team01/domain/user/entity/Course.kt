package org.team1.nbe1_3_team01.domain.user.entity

import jakarta.persistence.*
import lombok.Builder
import org.hibernate.annotations.SQLRestriction
import org.team1.nbe1_3_team01.domain.board.entity.CourseBoard
import org.team1.nbe1_3_team01.domain.calendar.entity.CourseSchedule
import org.team1.nbe1_3_team01.domain.group.entity.Team

@Entity
@Table(name = "course")
@SQLRestriction("is_delete = false")
class Course private constructor(
    name: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    var name: String = name
        protected set

    @Column(columnDefinition = "TINYINT(1)")
    var isDelete = false
        protected set

    @OneToMany(mappedBy = "course")
    val users: MutableList<User> = mutableListOf()

    @OneToMany(mappedBy = "course")
    val teams: MutableList<Team> = mutableListOf()

    @OneToMany(mappedBy = "course")
    val courseSchedules: MutableList<CourseSchedule> = mutableListOf()

    @OneToMany(mappedBy = "course")
    val courseBoards: MutableList<CourseBoard> = mutableListOf()

    companion object {
        fun of(
            name: String
        ): Course = Course(
            name = name
        )
    }

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
