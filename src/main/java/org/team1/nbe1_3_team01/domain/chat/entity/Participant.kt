package org.team1.nbe1_3_team01.domain.chat.entity

import Chat
import jakarta.persistence.*
import org.team1.nbe1_3_team01.domain.user.entity.User
import java.time.LocalDateTime


@Entity
@Table(name = "participant")
@IdClass(ParticipantPK::class)
class Participant ( // 기본 생성자 추가
    @Id
    @Column(name = "user_id")
    var userId: Long? = null,

    @Id
    @Column(name = "channel_id")
    var channelId: Long? = null,

    @Column(columnDefinition = "TINYINT(1)")
    var isCreator: Boolean = false,

    var participatedAt: LocalDateTime? = null,

    @Column(columnDefinition = "TINYINT(1)")
    var isParticipated: Boolean = false,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    var user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    var channel: Channel? = null,

    @OneToMany(mappedBy = "participant", fetch = FetchType.EAGER)
    var chats: MutableList<Chat> = mutableListOf()
) {
    // 커스텀 생성자
    constructor(
        user: User,
        channel: Channel,
        isCreator: Boolean,
        participatedAt: LocalDateTime,
        isParticipated: Boolean
    ) : this() {
        this.userId = user.id
        this.channelId = channel.id
        this.isCreator = isCreator
        this.participatedAt = participatedAt
        this.isParticipated = isParticipated
        this.user = user
        this.channel = channel
        user.addParticipant(this)
        channel.addParticipant(this)
    }

    fun addChat(chat: Chat) {
        this.chats.add(chat)
    }
}
