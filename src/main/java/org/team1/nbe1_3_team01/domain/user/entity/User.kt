package org.team1.nbe1_3_team01.domain.user.entity

import jakarta.persistence.*
import lombok.Builder
import lombok.Getter
import org.hibernate.annotations.SQLRestriction
import org.springframework.security.crypto.password.PasswordEncoder
import org.team1.nbe1_3_team01.domain.board.entity.Comment
import org.team1.nbe1_3_team01.domain.board.entity.CourseBoard
import org.team1.nbe1_3_team01.domain.board.entity.TeamBoard
import org.team1.nbe1_3_team01.domain.chat.entity.Participant
import org.team1.nbe1_3_team01.domain.group.entity.Belonging

@Entity
@Table(name = "users")
@SQLRestriction("is_delete = false")
class User private constructor(
    username: String,

    password: String,

    email: String,

    name: String,

    role: Role,

    course: Course?

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L

    @Column(length = 20, nullable = false)
    var username:String = username
        protected set

    @Column(nullable = false)
    var password:String = password
        protected set

    @Column(length = 50, nullable = false)
    var email: String = email
        protected set

    @Column(length = 10)
    var name: String = name
        protected set

    @Enumerated(EnumType.STRING)
    var role: Role = role
        protected set

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    var course: Course? = course
        protected set

    @Column(columnDefinition = "TINYINT(1)")
    var isDelete: Boolean? = false
        protected set

    @OneToMany(mappedBy = "user")
    val belongings: MutableList<Belonging> = mutableListOf()

    @OneToMany(mappedBy = "user")
    val comments: MutableList<Comment> = mutableListOf()

    @OneToMany(mappedBy = "user")
    val teamBoards: MutableList<TeamBoard> = mutableListOf()

    @OneToMany(mappedBy = "user")
    val courseBoards: MutableList<CourseBoard> = mutableListOf()

    @OneToMany(mappedBy = "user")
    val participants: MutableList<Participant> = mutableListOf()

    companion object {
        fun ofUser(
            username: String,
            password: String,
            email: String,
            name: String,
            course: Course
        ): User = User(
            username = username,
            password = password,
            email = email,
            name = name,
            role = Role.USER,
            course = course
        )

        fun ofAdmin(
            username: String,
            password: String,
            email: String,
            name: String,
        ): User = User(
            username = username,
            password = password,
            email = email,
            name = name,
            role = Role.ADMIN,
            course = null
        )
    }

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
