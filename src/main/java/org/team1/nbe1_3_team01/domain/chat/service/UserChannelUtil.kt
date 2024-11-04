package org.team1.nbe1_3_team01.domain.chat.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.team1.nbe1_3_team01.domain.chat.entity.Participant
import org.team1.nbe1_3_team01.domain.chat.repository.ChannelRepository
import org.team1.nbe1_3_team01.domain.chat.repository.ParticipantRepository
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import org.team1.nbe1_3_team01.global.util.SecurityUtil

@Service
class UserChannelUtil @Autowired constructor(
    private val channelRepository: ChannelRepository,
    private val participantRepository: ParticipantRepository,
    private val userRepository: UserRepository
) {

    fun findUser(channelId: Long): Participant {
        channelRepository.findById(channelId)
            .orElseThrow { RuntimeException("채널을 찾을 수 없습니다.") }

        val userId = currentUser().id

        return participantRepository.findByUserIdAndChannelId(userId, channelId)
            ?.orElseThrow { RuntimeException("참여자를 찾을 수 없습니다.") }!!
    }

     fun currentUser(): User {
        val currentUsername = SecurityUtil.getCurrentUsername()

        return userRepository.findByUsername(currentUsername)
            ?: throw AppException(ErrorCode.USER_NOT_FOUND)
    }

}