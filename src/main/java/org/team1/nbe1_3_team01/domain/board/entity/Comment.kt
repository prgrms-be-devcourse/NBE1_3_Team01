package org.team1.nbe1_3_team01.domain.board.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.team1.nbe1_3_team01.domain.user.entity.User
import java.time.LocalDateTime

@Entity
@Table(name = "comment")
data class Comment (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    val id: Long? = null,

    @JoinColumn(name = "user_id")
    @ManyToOne(fetch = FetchType.LAZY)
    val user: User,

    val content: String,

    @JoinColumn(name = "team_board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    var board: TeamBoard,

    @CreationTimestamp
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null
) {
    init {
        user.addComment(this)
        board.addComment(this)
    }
}
