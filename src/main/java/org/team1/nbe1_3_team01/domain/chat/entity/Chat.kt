import jakarta.persistence.*
import lombok.AccessLevel
import lombok.Builder
import lombok.NoArgsConstructor
import org.team1.nbe1_3_team01.domain.chat.entity.ChatActionType
import org.team1.nbe1_3_team01.domain.chat.entity.Participant
import java.time.LocalDateTime


@Entity
@Table(name = "chat")
class Chat(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var actionType: ChatActionType? = null,

    @Column(columnDefinition = "TEXT")
    var content: String? = null,

    var createdAt: LocalDateTime? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns(
        JoinColumn(name = "user_id", referencedColumnName = "user_id"),
        JoinColumn(name = "channel_id", referencedColumnName = "channel_id")
    )
    var participant: Participant? = null
) {
    // 일단 Builder 생성자를 만들어두긴 했는데....
    @Builder
    constructor(
        content: String,
        actionType: ChatActionType,
        createdAt: LocalDateTime,
        participant: Participant
    ) : this() {
        this.content = content
        this.actionType = actionType
        this.createdAt = createdAt
        this.participant = participant
        participant.addChat(this) // Chat을 Participant에 추가
    }
}