package org.team1.nbe1_3_team01.domain.user.entity

import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Builder
import lombok.Getter
import lombok.NoArgsConstructor
import org.hibernate.annotations.SQLRestriction
import org.springframework.security.crypto.password.PasswordEncoder
import org.team1.nbe1_3_team01.domain.board.entity.Comment
import org.team1.nbe1_3_team01.domain.board.entity.CourseBoard
import org.team1.nbe1_3_team01.domain.board.entity.TeamBoard
import org.team1.nbe1_3_team01.domain.chat.entity.Participant
import org.team1.nbe1_3_team01.domain.group.entity.Belonging

@Entity
@Getter
@Table(name = "users")
@SQLRestriction("is_delete = false")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class User @Builder private constructor(
    @Column(length = 20, nullable = false)
    private var username: String,

    @Column(nullable = false)
    private var password: String,

    @Column(length = 50, nullable = false)
    private var email: String,

    @Column(length = 10)
    private var name: String,

    @Enumerated(EnumType.STRING)
    private val role: Role,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private val course: Course? = null
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private val id: Long? = null

    @Column(columnDefinition = "TINYINT(1)")
    private var isDelete: Boolean? = false

    @OneToMany(mappedBy = "user")
    private val belongings: MutableList<Belonging> = ArrayList()

    @OneToMany(mappedBy = "user")
    private val comments: MutableList<Comment> = ArrayList()

    @OneToMany(mappedBy = "user")
    private val teamBoards: MutableList<TeamBoard> = ArrayList()

    @OneToMany(mappedBy = "user")
    private val courseBoards: MutableList<CourseBoard> = ArrayList()

    @OneToMany(mappedBy = "user")
    private val participants: MutableList<Participant> = ArrayList()



    fun addTeamBoard(teamBoard: TeamBoard) {
        teamBoards.add(teamBoard)
    }

    fun addCourseBoard(courseBoard: CourseBoard) {
        courseBoards.add(courseBoard)
    }

    fun addComment(comment: Comment) {
        comments.add(comment)
    }

    fun addParticipant(participant: Participant) {
        participants.add(participant)
    }

    fun addBelonging(belonging: Belonging) {
        belongings.add(belonging)
    }

    fun passwordEncode(passwordEncoder: PasswordEncoder) {
        this.password = passwordEncoder.encode(this.password)
    }

    fun updateName(name: String) {
        this.name = name
    }

    fun updatePassword(password: String) {
        this.password = password
    }

    fun delete() {
        this.isDelete = true
    }
}
