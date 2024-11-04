package org.team1.nbe1_3_team01.domain.chat.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.chat.entity.Channel
import org.team1.nbe1_3_team01.domain.chat.entity.Participant
import org.team1.nbe1_3_team01.domain.chat.repository.ChannelRepository
import org.team1.nbe1_3_team01.domain.chat.repository.ParticipantRepository
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import java.time.LocalDateTime

@Service
open class ChannelService(
    private val channelRepository: ChannelRepository,
    private val participantRepository: ParticipantRepository,
    private val userRepository: UserRepository,
    private val userChannelUtil: UserChannelUtil
) {

    //단순 채널 목록 전체 조회
    fun showAllChannel(): List<String> {
        return channelRepository.findAllChannelName().orEmpty()
            .filterNotNull()
    }

    // 채널 생성
    @Transactional
    open fun createChannel(creatorUserId: Long, channelName: String): Long {
        val creator: User = userRepository.findById(creatorUserId)
            .orElseThrow { AppException(ErrorCode.INVITER_NOT_FOUND) }

        val channel = Channel.Builder()
            .channelName(channelName)
            .build()

        val savedChannel = channelRepository.save(channel)

        // 참여자 추가
        val participant = Participant(
            user = creator,
            channel = savedChannel,
            isCreator = true,
            participatedAt = LocalDateTime.now(),
            isParticipated = true
        )

        participantRepository.save(participant)

        return savedChannel.id!!
    }


    // 채널 수정
    @Transactional
    open fun updateChannel(userId: Long, channelId: Long, channelName: String): Long {
        val participant: Participant = userChannelUtil.findUser(userId, channelId)

        if (!participant.isCreator) {
            throw AppException(ErrorCode.NOT_CHANEL_CREATOR)
        }

        val channel = participant.channel ?: throw AppException(ErrorCode.CHANEL_NOT_FOUND)

        channel.channelName = channelName
        channelRepository.save(channel)

        return channel.id!!
    }

    // 채널 삭제
    @Transactional
    open fun deleteChannel(userId: Long, channelId: Long) {
        val participant: Participant = userChannelUtil.findUser(userId, channelId)

        if (!participant.isCreator) {
            throw AppException(ErrorCode.NOT_CHANEL_DELETE)
        }

        val participants = participantRepository.findByChannelId(channelId)
            ?.filterNotNull() ?: throw AppException(ErrorCode.PARTICIPANTS_NOT_FOUND)

        participantRepository.deleteAll(participants) // 모든 참여자 먼저 삭제

        val channel = participants.firstOrNull()?.channel
            ?: throw AppException(ErrorCode.PARTICIPANTS_NOT_FOUND)
        channelRepository.delete(channel)
    }
}