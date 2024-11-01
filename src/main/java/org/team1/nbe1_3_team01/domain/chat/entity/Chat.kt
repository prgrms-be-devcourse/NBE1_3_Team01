package org.team1.nbe1_3_team01.domain.chat.entity

import jakarta.persistence.*
import java.time.LocalDateTime


@Entity
@Table(name = "chat")
class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var actionType: ChatActionType? = null,

    @Column(columnDefinition = "TEXT")
    var content: String,

    var createdAt: LocalDateTime? = LocalDateTime.now(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
        JoinColumn(name = "user_id", referencedColumnName = "user_id"),
        JoinColumn(name = "channel_id", referencedColumnName = "channel_id")
    )
    var participant: Participant? = null
)