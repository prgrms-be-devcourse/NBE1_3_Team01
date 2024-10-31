package org.team1.nbe1_3_team01.domain.chat.entity

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import lombok.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "channel")
@Getter
@Setter
class Channel(
    @Column(length = 50)
    @NotNull
    var channelName: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

    @CreationTimestamp
    var createdAt: LocalDateTime? = null

    @OneToMany(mappedBy = "channel", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var participants: MutableList<Participant> = ArrayList()


    fun addParticipant(participant: Participant) {
        participants.add(participant)
        participant.channel = this // 양방향 관계를 위해 채널을 참가자에 설정
    }
}