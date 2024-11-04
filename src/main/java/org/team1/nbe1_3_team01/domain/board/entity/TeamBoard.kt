package org.team1.nbe1_3_team01.domain.board.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.team1.nbe1_3_team01.domain.board.controller.dto.TeamBoardUpdateRequest
import org.team1.nbe1_3_team01.domain.group.entity.Team
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import java.time.LocalDateTime

@Entity
@Table(name = "team_board")
data class TeamBoard (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, // 외부에서 접근 불가하도록 가시성 조정

    var categoryId: Long,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    val team: Team,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    var title: String,

    @Column(columnDefinition = "TEXT")
    var content: String,

    @CreationTimestamp
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    val comments: MutableList<Comment> = mutableListOf()
) {
    init {
        team.addTeamBoard(this)
        user.addTeamBoard(this)
    }

    fun addComment(comment: Comment) {
        comments.add(comment)
        comment.board = this
    }

    fun updateTeamBoard(updateRequest: TeamBoardUpdateRequest) {
        val newTitle = updateRequest.title
        val newContent = updateRequest.content
        val newCategoryId = updateRequest.categoryId

        if (this.title == newTitle && this.content == newContent && this.categoryId == newCategoryId) {
            throw AppException(ErrorCode.BOARD_NOT_UPDATED)
        }

        this.categoryId = newCategoryId
        this.title = newTitle
        this.content = newContent
        this.updatedAt = LocalDateTime.now()
    }
}
