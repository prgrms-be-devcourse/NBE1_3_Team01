package org.team1.nbe1_3_team01.domain.chat.service

import lombok.RequiredArgsConstructor
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.chat.controller.request.ChannelRequest
import org.team1.nbe1_3_team01.domain.chat.controller.request.ChatMessageRequest
import org.team1.nbe1_3_team01.domain.chat.controller.request.InviteRequest
import org.team1.nbe1_3_team01.domain.chat.entity.*
import org.team1.nbe1_3_team01.domain.chat.repository.ChannelRepository
import org.team1.nbe1_3_team01.domain.chat.repository.ChatRepository
import org.team1.nbe1_3_team01.domain.chat.repository.ParticipantRepository
import org.team1.nbe1_3_team01.domain.chat.service.response.ChannelResponse
import org.team1.nbe1_3_team01.domain.chat.service.response.ParticipantResponse
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import org.team1.nbe1_3_team01.global.util.SecurityUtil
import java.time.LocalDateTime

@Service
class ParticipantService (
    private val participantRepository: ParticipantRepository,
    private val channelRepository: ChannelRepository,
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val chatService: ChatService,
    private val userChannelUtil: UserChannelUtil
) {
    fun joinChannel(userId: Long, request: ChannelRequest): ParticipantResponse {
        val userOptional = userRepository.findById(userId)
        if (userOptional.isEmpty) {
            throw AppException(ErrorCode.USER_NOT_FOUND)
        }
        val user = userOptional.get()

        // 채널 정보 가져오기
        val channelId = request.channelId
        val channelOptional = channelRepository.findById(channelId)
        if (channelOptional.isEmpty) {
            throw AppException(ErrorCode.CHANEL_NOT_FOUND)
        }
        val channel = channelOptional.get()

        // 이미 채널에 참여중인지 확인
        val existingParticipant = channel.participants.find { it.user?.id == user.id && it.isParticipated }
        if (existingParticipant != null) {
            throw AppException(ErrorCode.ALREADY_PARTICIPANT)
        }

        // 참가자 생성
        val participant = Participant(
            user = user,
            channel = channel,
            isCreator = false,
            participatedAt = LocalDateTime.now(),
            isParticipated = true
        )

        channel.addParticipant(participant)
        channelRepository.save(channel)

        return ParticipantResponse(userId = user.id)
    }

    @Transactional
    fun inviteUser(inviteRequest: InviteRequest) {
        val inviteUserId = userChannelUtil.currentUser().id
        val inviter = participantRepository.findParticipantByUserIdAndChannelId(inviteUserId, inviteRequest.channelId)
            ?: throw AppException(ErrorCode.NOT_CHANEL_CREATOR)

        if (!inviter.isCreator) {
            throw AppException(ErrorCode.NOT_CHANEL_CREATOR)
        }

        inviteRequest.participantIds.forEach { participantId ->
            try {
                joinChannel(participantId, ChannelRequest(channelId = inviteRequest.channelId))

                val newParticipant = participantRepository.findParticipantByUserIdAndChannelId(participantId, inviteRequest.channelId)
                    ?: throw AppException(ErrorCode.PARTICIPANTS_NOT_FOUND)

                val chat = Chat(
                    actionType = ChatActionType.ENTER,
                    content = "",
                    createdAt = LocalDateTime.now(),
                    participant = newParticipant
                )

                chatRepository.save(chat)

                val enterMessage = ChatMessageRequest(
                    channelId = inviteRequest.channelId,
                    userId = participantId,
                    content = "",
                    createdAt = LocalDateTime.now(),
                    actionType = ChatActionType.ENTER
                )

                chatService.sendMessage(inviteRequest.channelId, enterMessage)
            } catch (e: AppException) {
                if (e.errorCode != ErrorCode.ALREADY_PARTICIPANT) {
                    throw e
                }
            }
        }
    }


    // 참여자가 속해 있는 채팅방 + 각 채팅방에 마지막 메시지 + 시간을 보여주기
    @Transactional(readOnly = true)
    fun checkUserChannel(): List<ChannelResponse> {

        val userId = userChannelUtil.currentUser().id

        val participants = participantRepository.findByUserId(userId) ?: return emptyList()

        val channelResponses = participants.mapNotNull { participant ->
            participant?.channel?.let { channel ->
                val lastChat = chatRepository.findTopByIdOrderByCreatedAtDesc(channel.id!!)

                ChannelResponse(
                    channelId = channel.id!!,
                    name = channel.channelName,
                    lastMessage = lastChat?.content ?: "",
                    lastMessageTime = lastChat?.createdAt ?: LocalDateTime.now()
                )
            }
        }
        return channelResponses
    }


    @Transactional
    fun leaveChannel(channelId: Long) {
        // 현재 인증된 사용자 가져오기
        val userId = userChannelUtil.currentUser().id

        // 현재 사용자와 채널에 대한 참여 정보 조회
        val participant = participantRepository.findByUserIdAndChannelId(userId, channelId)
            ?.orElseThrow { AppException(ErrorCode.PARTICIPANTS_NOT_FOUND) } // Optional 처리

        participantRepository.delete(participant!!) // 참여자 삭제

        // 방의 모든 사용자에게 나간 알림 전송 (나간 사용자 제외)
        val participants = participantRepository.findByChannelId(channelId)
        participants?.forEach { currentParticipant ->
            if (currentParticipant?.user?.id != userId) { // 나간 사용자는 제외
                val leaveMessage = ChatMessageRequest(
                    channelId = channelId,
                    userId = userId,
                    content = "",
                    createdAt = LocalDateTime.now(),
                    actionType = ChatActionType.EXIT
                )
                chatService.sendMessage(channelId, leaveMessage)
            }
        }
    }


    // 강퇴하기
    @Transactional
    fun removeParticipant(participantPK: ParticipantPK, participantIdToRemove: Long) {
        // 참여자 조회
        val participant = participantRepository.findById(participantPK)
            .orElseThrow { AppException(ErrorCode.NO_PARTICIPANTS) }

        // 현재 참여자가 채널 생성자인지 확인
        if (participant != null && !participant.isCreator) {
            throw AppException(ErrorCode.NOT_CHANEL_CREATOR)
        }

        // 제거할 참여자 조회
        val participantToRemove = participantRepository.findById(
            ParticipantPK(participantIdToRemove, participantPK.channelId)
        ).orElseThrow { AppException(ErrorCode.PARTICIPANTS_NOT_FOUND) }

        participantRepository.delete(participantToRemove)

        val kickMessage = ChatMessageRequest(
            channelId = participantPK.channelId ?: throw AppException(ErrorCode.CHANEL_NOT_FOUND),
            userId = participantIdToRemove,
            content = "",
            createdAt = LocalDateTime.now(),
            actionType = ChatActionType.KICK
        )

        // 방의 모든 사용자에게 킥 메시지 전송 (킥된 사용자는 제외)
        val participants = participantRepository.findByChannelId(participantPK.channelId)
        participants?.forEach { currentParticipant ->
            if (currentParticipant?.userId != participantIdToRemove) {
                chatService.sendMessage(participantPK.channelId!!, kickMessage)
            }
        }
    }
}