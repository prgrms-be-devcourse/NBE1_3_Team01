package org.team1.nbe1_3_team01.domain.chat.service

import lombok.RequiredArgsConstructor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.chat.controller.request.ChatMessageRequest
import org.team1.nbe1_3_team01.domain.chat.controller.request.InviteRequest
import org.team1.nbe1_3_team01.domain.chat.entity.ChatActionType
import org.team1.nbe1_3_team01.domain.chat.entity.Participant
import org.team1.nbe1_3_team01.domain.chat.entity.ParticipantPK
import org.team1.nbe1_3_team01.domain.chat.repository.ChannelRepository
import org.team1.nbe1_3_team01.domain.chat.repository.ParticipantRepository
import org.team1.nbe1_3_team01.domain.chat.service.response.ChannelResponse
import org.team1.nbe1_3_team01.domain.chat.service.response.ParticipantResponse
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import java.time.LocalDateTime
import java.util.stream.Collectors

@Service
@RequiredArgsConstructor
class ParticipantService {
    private val participantRepository: ParticipantRepository? = null
    private val channelRepository: ChannelRepository? = null
    private val userRepository: UserRepository? = null
    private val chatService: ChatService? = null
    private val messagingTemplate: SimpMessagingTemplate? = null


    // 채널에 참여
    @Transactional
    fun joinChannel(channelId: Long, userId: Long): ParticipantResponse {
        try {
            val userChannelUtil = UserChannelUtil(channelRepository, participantRepository)
            val existParticipant = userChannelUtil.findUser(userId, channelId)
            return ParticipantResponse(existParticipant.user!!.id) // 이미 참여 중인 경우 userId만 반환
        } catch (e: RuntimeException) {
            val user = userRepository!!.findById(userId).orElseThrow { AppException(ErrorCode.INVITER_NOT_FOUND) }
            val channel =
                channelRepository!!.findById(channelId).orElseThrow { AppException(ErrorCode.CHANEL_NOT_FOUND) }!!

            val participant: Participant = Participant.builder()
                .isCreator(false)
                .participatedAt(LocalDateTime.now())
                .isParticipated(true)
                .user(user)
                .channel(channel)
                .build()

            participantRepository!!.save(participant)
            return ParticipantResponse(user.id) // 새로운 참여자의 userId만 반환
        }
    }


    // 사용자 초대
    @Transactional
    fun inviteUser(inviteRequest: InviteRequest) {
        val participantPK = ParticipantPK(inviteRequest.inviteUserId, inviteRequest.channelId)
        val inviter = participantRepository!!.findById(participantPK)
            .orElseThrow { AppException(ErrorCode.INVITER_NOT_FOUND) }!!

        if (!inviter.isCreator) {
            throw AppException(ErrorCode.NOT_CHANEL_CREATOR)
        }

        joinChannel(inviteRequest.channelId, inviteRequest.participantId)

        // 입장 코드 전송
        val enterMessage: ChatMessageRequest = ChatMessageRequest.builder()
            .channelId(inviteRequest.channelId)
            .userId(inviteRequest.participantId)
            .actionType(ChatActionType.ENTER) // 입장코드 9번 전달
            .build()

        chatService.sendMessage(inviteRequest.channelId, enterMessage) // 일단 메시지 없이 코드만 전달
    }

    // 참여중인 채널 조회
    // Service
    @Transactional(readOnly = true)
    fun checkUserChannel(userId: Long?): List<ChannelResponse> {
        val participants = participantRepository!!.findByUserId(userId)

        return participants!!.stream()
            .map { participant: Participant? ->
                val channel = participant!!.channel
                ChannelResponse(channel!!.id!!, channel.channelName)
            }
            .collect(Collectors.toList())
    }

    // 참여자가 스스로 방을 나감
    @Transactional
    fun leaveChannel(participantPK: ParticipantPK) {
        val participant = participantRepository!!.findById(participantPK)
            .orElseThrow { AppException(ErrorCode.PARTICIPANTS_NOT_FOUND) }!!

        participantRepository.delete(participant)

        // 방을 나간 메시지 생성
        val leaveMessage: ChatMessageRequest = ChatMessageRequest.builder()
            .channelId(participantPK.getChannelId())
            .userId(participantPK.getUserId()) // 나간 사용자 ID 설정
            .actionType(ChatActionType.EXIT) // 0번 코드 전달
            .build()

        // 방의 모든 사용자에게 나간 알림 전송 (나간 사용자 제외)
        val participants = participantRepository.findByChannelId(participantPK.getChannelId())
        for (currentParticipant in participants!!) {
            if (currentParticipant!!.userId != participantPK.getUserId()) {  // 나간 사용자는 제외
                chatService.sendMessage(participantPK.getChannelId(), leaveMessage)
            }
        }
    }

    @Transactional
    fun removeParticipant(participantPK: ParticipantPK, participantIdToRemove: Long) {
        val participant = participantRepository.findById(participantPK)
            .orElseThrow { AppException(NO_PARTICIPANTS) }

        if (!participant.isCreator) {
            throw AppException(NOT_CHANEL_CREATOR)
        }

        val participantToRemove = participantRepository.findById(ParticipantPK(participantIdToRemove, participantPK.channelId))
            .orElseThrow { AppException(PARTICIPANTS_NOT_FOUND) }

        participantRepository.delete(participantToRemove)

        val kickMessage = ChatMessageRequest(
            channelId = participantPK.channelId,
            userId = participantIdToRemove,
            actionType = ChatActionType.KICK
        )

        val participants = participantRepository.findByChannelId(participantPK.channelId)
        participants.forEach { currentParticipant ->
            if (currentParticipant.userId != participantIdToRemove) {
                chatService.sendMessage(participantPK.channelId, kickMessage)
            }
        }
    }
}