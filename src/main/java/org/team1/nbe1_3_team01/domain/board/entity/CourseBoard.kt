package org.team1.nbe1_3_team01.domain.board.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardUpdateRequest
import org.team1.nbe1_3_team01.domain.user.entity.Course
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import java.time.LocalDateTime

@Entity
@Table(name = "course_board")
data class CourseBoard(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null, // 외부에서 접근 불가하도록 private 제거

    @CreationTimestamp
    val createdAt: LocalDateTime? = null,

    @UpdateTimestamp
    var updatedAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    var course: Course,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User,

    var title: String,

    @Column(columnDefinition = "TEXT")
    var content: String
) {

    fun updateBoard(updateRequest: CourseBoardUpdateRequest) {
        val newTitle = updateRequest.title
        val newContent = updateRequest.content

        if (this.title == newTitle && this.content == newContent) {
            throw AppException(ErrorCode.BOARD_NOT_UPDATED)
        }

        this.title = newTitle
        this.content = newContent
        this.updatedAt = LocalDateTime.now()
    }
}
