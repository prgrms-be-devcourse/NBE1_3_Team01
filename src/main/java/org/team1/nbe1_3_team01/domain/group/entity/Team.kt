package org.team1.nbe1_3_team01.domain.group.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.team1.nbe1_3_team01.domain.board.entity.Category
import org.team1.nbe1_3_team01.domain.board.entity.TeamBoard
import org.team1.nbe1_3_team01.domain.calendar.entity.TeamSchedule
import org.team1.nbe1_3_team01.domain.user.entity.Course

@Entity
@Table(name = "team")
class Team(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    val course: Course,

    @NotNull
    @Enumerated(EnumType.STRING)
    val teamType: TeamType,

    @Column(length = 50)
    var name: String,

    @Column(columnDefinition = "TINYINT(1)")
    var creationWaiting: Boolean = false,
    @Column(columnDefinition = "TINYINT(1)")
    var deletionWaiting: Boolean = false,

    @OneToMany(
        mappedBy = "team",
        cascade = [CascadeType.REMOVE],
        orphanRemoval = true
    )
    val belongings: MutableList<Belonging> = mutableListOf(),

    @OneToMany(mappedBy = "team")
    val teamBoards: MutableList<TeamBoard> = mutableListOf(),

    @OneToMany(mappedBy = "team")
    val teamSchedules: MutableList<TeamSchedule> = mutableListOf(),

    @OneToMany(mappedBy = "team")
    val categories: MutableList<Category> = mutableListOf(),
) {

    init {
        course.addTeam(this)
    }

    fun addTeamBoard(teamBoard: TeamBoard) {
        teamBoards.add(teamBoard)
    }

    fun addTeamSchedule(teamSchedule: TeamSchedule) {
        teamSchedules.add(teamSchedule)
    }

    fun addCategory(category: Category) {
        categories.add(category)
    }

}