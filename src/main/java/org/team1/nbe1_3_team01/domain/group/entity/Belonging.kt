package org.team1.nbe1_3_team01.domain.group.entity

import jakarta.persistence.*
import org.team1.nbe1_3_team01.domain.user.entity.User

@Entity
@Table(name = "belonging")
class Belonging(
    @Id
    @SequenceGenerator(
        name = "BELONGING_SEQ_GENERATOR",   // 식별자 생성기 이름
        sequenceName = "BELONGING_SEQ",  // 데이터베이스에 등록되어있는 시퀀스 이름: DB에는 해당 이름으로 매핑된다.
        initialValue = 1,  // DDL 생성시에만 사용되며 시퀀스 DDL을 생성할 때 처음 시작하는 수를 지정
        allocationSize = 50  // 시퀀스 한 번 호출에 증가하는 수
    )
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "BELONGING_SEQ_GENERATOR"
    )
    val id: Long? = null,

    @Column(columnDefinition = "TINYINT(1)")
    val isOwner: Boolean,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    var team: Team? = null
) {

    init {
        user.addBelonging(this)
    }

    fun assignTeam(team: Team) {
        this.team = team
    }

}