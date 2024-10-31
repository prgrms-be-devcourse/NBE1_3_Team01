package org.team1.nbe1_3_team01.domain.chat.service

import lombok.RequiredArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.team1.nbe1_3_team01.domain.chat.entity.Participant
import org.team1.nbe1_3_team01.domain.chat.repository.ChannelRepository
import org.team1.nbe1_3_team01.domain.chat.repository.ParticipantRepository
import java.util.function.Supplier

@Service
class UserChannelUtil @Autowired constructor(
    private val channelRepository: ChannelRepository,
    private val participantRepository: ParticipantRepository
) {

    fun findUser(userId: Long, channelId: Long): Participant {
        channelRepository.findById(channelId)
            .orElseThrow { RuntimeException("채널을 찾을 수 없습니다.") }

        return participantRepository.findByUserIdAndChannelId(userId, channelId)
            ?.orElseThrow { RuntimeException("참여자를 찾을 수 없습니다.") }!!
    }
}