package org.team1.nbe1_3_team01.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.team1.nbe1_3_team01.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "participant")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@IdClass(ParticipantPK.class)
public class Participant {

    @Id
    @Column(name = "user_id")
    private Long userId;
    @Id
    @Column(name = "channel_id")
    private Long channelId;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isCreator;

    private LocalDateTime participatedAt;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean isParticipated;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id", insertable = false, updatable = false)
    private Channel channel;

    @OneToMany(mappedBy = "participant", fetch = FetchType.EAGER)
    private List<Chat> chats = new ArrayList<>();

    @Builder
    private Participant(User user,
                        Channel channel,
                        boolean isCreator,
                        LocalDateTime participatedAt,
                        boolean isParticipated) {
        this.userId = user.getId();  // User ID 설정
        this.channelId = channel.getId();  // Channel ID 설정
        this.isCreator = isCreator;
        this.participatedAt = participatedAt;
        this.isParticipated = isParticipated;
        user.addParticipant(this);
        channel.addParticipant(this);
    }

    public void addChat(Chat chat) {
        this.chats.add(chat);
    }
}