package org.team1.nbe1_3_team01.domain.board.entity

import jakarta.persistence.*
import org.team1.nbe1_3_team01.domain.group.entity.Team

@Entity
@Table(name = "board_category")
data class Category (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long = 0L,

    @JoinColumn(name = "team_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private val team: Team,

    @Column(length = 50)
    private var name: String
) {

    init {
        team.addCategory(this)
    }
}
