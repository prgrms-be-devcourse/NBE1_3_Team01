package org.team1.nbe1_3_team01.domain.chat.service

import lombok.RequiredArgsConstructor
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
@RequiredArgsConstructor
class ParticipantService {
    private val participantRepository: ParticipantRepository? = null
    private val channelRepository: ChannelRepository? = null
    private val chatRepository: ChatRepository? = null
    private val userRepository: UserRepository? = null
    private val chatService: ChatService? = null
    private val userChannelUtil: UserChannelUtil? = null

    // 채널에 참여
    @Transactional
    fun joinChannel(channelId: Long): ParticipantResponse {
        // 현재 사용자 가져오기
        val userId = userChannelUtil?.currentUser()?.id

        // 이미 참여 중인지 확인
        participantRepository?.findByUserIdAndChannelId(userId, channelId)
            ?: throw AppException(ErrorCode.ALREADY_PARTICIPANT)

        // 참여하지 않은 경우 새로 참여
        val user = userRepository?.findById(userId)?.orElseThrow { AppException(ErrorCode.USER_NOT_FOUND) }
        val channel = channelRepository?.findById(channelId)?.orElseThrow { AppException(ErrorCode.CHANEL_NOT_FOUND)}

        val participant = Participant(
            isCreator = false,
            participatedAt = LocalDateTime.now(),
            isParticipated = true,
            user = user,
            channel = channel
        )

        participantRepository.save(participant)
        return ParticipantResponse(user!!.id)
    }

    // 사용자 초대
    @Transactional
    fun inviteUser(inviteRequest: InviteRequest) {
        // 현재 사용자를 초대자로 설정
        val inviteUserId = userChannelUtil?.currentUser()?.id

        // 초대자가 채널의 생성자인지 확인
        val inviterPK = ParticipantPK(inviteUserId, inviteRequest.channelId)
        val inviter = participantRepository?.findById(inviterPK)
            ?.orElseThrow { AppException(ErrorCode.INVITER_NOT_FOUND) }

        if (!inviter!!.isCreator) {
            throw AppException(ErrorCode.NOT_CHANEL_CREATOR)
        }

        // 초대 받은 모든 사용자에 대해 반복
        inviteRequest.participantIds.forEach { participantId ->
            // 초대 받은 사용자를 채널에 참여시키기
            joinChannel(inviteRequest.channelId)

            val newParticipantPK = ParticipantPK(participantId, inviteRequest.channelId)
            val newParticipant = participantRepository?.findById(newParticipantPK)
                ?.orElseThrow { AppException(ErrorCode.PARTICIPANTS_NOT_FOUND) }

            // 입장 메시지 생성 및 저장
            val chat = Chat(
                actionType = ChatActionType.ENTER,
                content = "", // 메시지 없이 코드만 전달
                createdAt = LocalDateTime.now(),
                participant = newParticipant
            )

            chatRepository?.save(chat)

            // 입장 코드 전송
            val enterMessage = ChatMessageRequest(
                channelId = inviteRequest.channelId,
                userId = participantId,
                content = "", // 메시지 없이 코드만 전달
                createdAt = LocalDateTime.now(),
                actionType = ChatActionType.ENTER
            )

            chatService?.sendMessage(inviteRequest.channelId, enterMessage)
        }
    }



    // 참여자가 속해 있는 채팅방 + 각 채팅방에 마지막 메시지 + 시간을 보여주기
    @Transactional(readOnly = true)
    fun checkUserChannel(): List<ChannelResponse> {

        val userId = userChannelUtil?.currentUser()?.id

        val participants = participantRepository?.findByUserId(userId) ?: return emptyList()

        val channelResponses = participants.mapNotNull { participant ->
            participant?.channel?.let { channel ->
                val lastChat = chatRepository?.findTopByIdOrderByCreatedAtDesc(channel.id!!)

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
        val userId = userChannelUtil?.currentUser()?.id

        // 현재 사용자와 채널에 대한 참여 정보 조회
        val participant = participantRepository?.findByUserIdAndChannelId(userId, channelId)
            ?.orElseThrow { AppException(ErrorCode.PARTICIPANTS_NOT_FOUND) } // Optional 처리

        participantRepository?.delete(participant!!) // 참여자 삭제

        // 방의 모든 사용자에게 나간 알림 전송 (나간 사용자 제외)
        val participants = participantRepository?.findByChannelId(channelId)
        participants?.forEach { currentParticipant ->
            if (currentParticipant?.user?.id != userId) { // 나간 사용자는 제외
                val leaveMessage = ChatMessageRequest(
                    channelId = channelId,
                    userId = userId!!,
                    content = "",
                    createdAt = LocalDateTime.now(),
                    actionType = ChatActionType.EXIT
                )
                chatService?.sendMessage(channelId, leaveMessage)
            }
        }
    }


    // 강퇴하기
    @Transactional
    fun removeParticipant(participantPK: ParticipantPK, participantIdToRemove: Long) {
        // 참여자 조회
        val participant = participantRepository?.findById(participantPK)
            ?.orElseThrow { AppException(ErrorCode.NO_PARTICIPANTS) }

        // 현재 참여자가 채널 생성자인지 확인
        if (participant != null && !participant.isCreator) {
            throw AppException(ErrorCode.NOT_CHANEL_CREATOR)
        }

        // 제거할 참여자 조회
        val participantToRemove = participantRepository?.findById(
            ParticipantPK(participantIdToRemove, participantPK.channelId)
        )?.orElseThrow { AppException(ErrorCode.PARTICIPANTS_NOT_FOUND) }

        participantRepository?.delete(participantToRemove)

        val kickMessage = ChatMessageRequest(
            channelId = participantPK.channelId ?: throw AppException(ErrorCode.CHANEL_NOT_FOUND),
            userId = participantIdToRemove,
            content = "",
            createdAt = LocalDateTime.now(),
            actionType = ChatActionType.KICK
        )

        // 방의 모든 사용자에게 킥 메시지 전송 (킥된 사용자는 제외)
        val participants = participantRepository?.findByChannelId(participantPK.channelId)
        participants?.forEach { currentParticipant ->
            if (currentParticipant?.userId != participantIdToRemove) {
                chatService?.sendMessage(participantPK.channelId!!, kickMessage)
            }
        }
    }
}