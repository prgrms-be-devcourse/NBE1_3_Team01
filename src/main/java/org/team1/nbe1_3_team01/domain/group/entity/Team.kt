package org.team1.nbe1_3_team01.domain.group.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import lombok.*
import org.team1.nbe1_3_team01.domain.board.entity.Category
import org.team1.nbe1_3_team01.domain.board.entity.TeamBoard
import org.team1.nbe1_3_team01.domain.calendar.entity.TeamSchedule
import org.team1.nbe1_3_team01.domain.user.entity.Course

@Entity
@Getter
@Table(name = "team")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Team @Builder private constructor(
    @field:JoinColumn(name = "course_id") @field:ManyToOne(fetch = FetchType.LAZY) private val course: Course,
    teamType: TeamType,
    @Column(length = 50) @Setter private var name: String,
    @Column(columnDefinition = "TINYINT(1)") @Setter private var creationWaiting: Boolean,
    @Column(columnDefinition = "TINYINT(1)") @Setter private var deletionWaiting: Boolean
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Enumerated(EnumType.STRING)
    private val teamType: @NotNull TeamType? = teamType

    @OneToMany(mappedBy = "team", cascade = [CascadeType.REMOVE], orphanRemoval = true)
    private val belongings: MutableList<Belonging> = ArrayList()

    @OneToMany(mappedBy = "team")
    private val teamBoards: MutableList<TeamBoard> = ArrayList()

    @OneToMany(mappedBy = "team")
    private val teamSchedules: MutableList<TeamSchedule> = ArrayList()

    @OneToMany(mappedBy = "team")
    private val categories: MutableList<Category> = ArrayList()

    init {
        course.addTeam(this)
    }

    fun assignBelonging(belonging: Belonging) {
        belongings.add(belonging)
        belonging.assignTeam(this)
    }

    fun addTeamBoard(teamBoard: TeamBoard) {
        teamBoards.add(teamBoard)
    }

    fun addTeamScheDule(teamSchedule: TeamSchedule) {
        teamSchedules.add(teamSchedule)
    }

    fun addCategory(category: Category) {
        categories.add(category)
    }
}
