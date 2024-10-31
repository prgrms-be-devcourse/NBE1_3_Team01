package org.team1.nbe1_3_team01.domain.chat.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.chat.entity.Channel
import org.team1.nbe1_3_team01.domain.chat.entity.Participant
import org.team1.nbe1_3_team01.domain.chat.repository.ChannelRepository
import org.team1.nbe1_3_team01.domain.chat.repository.ParticipantRepository
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import java.time.LocalDateTime

@Service
open class ChannelService(
    private val channelRepository: ChannelRepository,
    private val participantRepository: ParticipantRepository,
    private val userRepository: UserRepository,
    private val userChannelUtil: UserChannelUtil
) {

    // 채널 목록 전체 조회
    fun showAllChannel(): List<String> {
        return channelRepository.findAllChannelName().orEmpty() // null인 경우 빈 리스트 반환
            .filterNotNull() // null 값 제거
    }

    // 채널 생성
    @Transactional
    open fun createChannel(creatorUserId: Long, channelName: String): Long {
        val creator: User = userRepository.findById(creatorUserId)
//            .orElseThrow { AppException(ErrorCode.INVITER_NOT_FOUND) }
            .orElseThrow { RuntimeException("임시로 해둔 예외처리 : $creatorUserId") }


        val channel = Channel.Builder()
            .channelName(channelName)
            .build()

        val savedChannel = channelRepository.save(channel)

        // 참여자 추가
        val participant = Participant(
            user = creator,
            channel = channel,
            isCreator = true,
            participatedAt = LocalDateTime.now(),
            isParticipated = true
        )

        participantRepository.save(participant)

//        return savedChannel.id ?: throw AppException(ErrorCode.CHANNEL_ID_NULL) // ID 반환, null일 경우 예외 처리
        return savedChannel.id ?: throw RuntimeException("임시로 해둔 예외처리 ") // ID 반환, null일 경우 예외 처리

    }

    // 채널 수정
    @Transactional
    open fun updateChannel(userId: Long, channelId: Long, channelName: String): Long {
        val participant: Participant = userChannelUtil.findUser(userId, channelId)

//        if (!participant.isCreator) {
//            throw AppException(ErrorCode.NOT_CHANEL_CREATOR)
//        }
        if (!participant.isCreator) {
            throw RuntimeException("임시로 해둔 예외처리  ID:  $userId , 채널 :  $channelId") // 생성자가 아닐 경우 예외
        }

        val channel = participant.channel ?: throw IllegalArgumentException("임시로 해둔 예외처리 ")
        channel.channelName = channelName

        channelRepository.save(channel)

//        return channel?.id ?: throw AppException(ErrorCode.CHANNEL_ID_NULL)
        return channel?.id ?: throw RuntimeException("임시로 해둔 예외처리 ") // ID 반환, null일 경우 예외 처리

    }

    // 채널 삭제
    @Transactional
    open fun deleteChannel(userId: Long, channelId: Long) {
        val participant: Participant = userChannelUtil.findUser(userId, channelId)

//        if (!participant.isCreator) {
//            throw AppException(ErrorCode.NOT_CHANEL_DELETE)
//        }
        if (!participant.isCreator) {
            throw RuntimeException("id : $userId , 임시로 해둔 예외처리 채널 id: $channelId") // 삭제 권한이 없는 경우 예외
        }

//        val participants: List<Participant> = participantRepository.findByChannelId(channelId)
//            ?.filterNotNull() ?: throw AppException(ErrorCode.PARTICIPANTS_NOT_FOUND)
        val participants: List<Participant> = participantRepository.findByChannelId(channelId)
            ?.filterNotNull() ?: throw RuntimeException("임시로 해둔 예외처리  channel ID: $channelId")

        participantRepository.deleteAll(participants) // 모든 참여자 먼저 삭제

        val channel = participant.channel ?: throw IllegalArgumentException("임시로 해둔 예외처리 ")
        channelRepository.delete(channel)
    }
}