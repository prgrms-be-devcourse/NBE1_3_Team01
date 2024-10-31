package org.team1.nbe1_3_team01.domain.chat.service

import lombok.RequiredArgsConstructor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.chat.controller.request.ChatMessageRequest
import org.team1.nbe1_3_team01.domain.chat.controller.request.InviteRequest
import org.team1.nbe1_3_team01.domain.chat.entity.Channel
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
    // 채널에 참여
    @Transactional
    fun joinChannel(channelId: Long, userId: Long): ParticipantResponse {
        val userChannelUtil = UserChannelUtil(channelRepository!!, participantRepository!!)

        // 이미 참여 중인지 확인
        val existingParticipant = userChannelUtil.findUser(userId, channelId)
        if (existingParticipant != null) {
            return ParticipantResponse(existingParticipant.user?.id ?: throw AppException(ErrorCode.USER_NOT_FOUND))
        }

        // 참여하지 않은 경우 새로 참여
        val user = userRepository?.findById(userId)?.orElseThrow { AppException(ErrorCode.INVITER_NOT_FOUND) }
        val channel = channelRepository?.findById(channelId)?.orElseThrow { AppException(ErrorCode.CHANEL_NOT_FOUND) }

        val participant = Participant(
            isCreator = false,
            participatedAt = LocalDateTime.now(),
            isParticipated = true,
            user = user,
            channel = channel
        )

        participantRepository?.save(participant)
        return ParticipantResponse(user?.id!!)
    }


    // 사용자 초대
    @Transactional
    fun inviteUser(inviteRequest: InviteRequest) {
        val participantPK = ParticipantPK(inviteRequest.inviteUserId, inviteRequest.channelId)
        val inviter = participantRepository?.findById(participantPK)
            ?.orElseThrow { AppException(ErrorCode.INVITER_NOT_FOUND) }


        if (inviter != null) {
            if (!inviter.isCreator) {
                throw AppException(ErrorCode.NOT_CHANEL_CREATOR)
            }
        }

        joinChannel(inviteRequest.channelId, inviteRequest.participantId)

        // 입장 코드 전송
        val enterMessage = ChatMessageRequest(
            channelId = inviteRequest.channelId,
            userId = inviteRequest.participantId,
            content = "", // 메시지 없이 코드만 전달 (일단은 코드만 전달하겠습니다!)
            createdAt = LocalDateTime.now(),
            actionType = ChatActionType.ENTER
        )

        chatService?.sendMessage(inviteRequest.channelId, enterMessage)
    }

    @Transactional(readOnly = true)
    fun checkUserChannel(userId: Long): List<ChannelResponse> {
        val participants = participantRepository?.findByUserId(userId)
            ?: throw AppException(ErrorCode.PARTICIPANTS_NOT_FOUND)

        // 참여자가 하나도 없는 경우
        if (participants.isEmpty()) {
            throw AppException(ErrorCode.NO_PARTICIPANTS)
        }

        val channelResponses = participants.mapNotNull { participant ->
            participant?.channel?.let { channel ->
                // channel.id가 null 이 아닐 경우에만 ChannelResponse 생성
                channel.id?.let { channelId ->
                    ChannelResponse(channelId, channel.channelName)
                }
            }
        }

        // 채널이 하나도 없는 경우 예외 발생
        if (channelResponses.isEmpty()) {
            throw AppException(ErrorCode.CHANEL_NOT_FOUND) // 필요한 예외 코드에 맞게 변경
        }

        return channelResponses
    }

    @Transactional
    fun leaveChannel(participantPK: ParticipantPK) {
        val participant = participantRepository?.findById(participantPK)
            ?.orElseThrow { AppException(ErrorCode.PARTICIPANTS_NOT_FOUND) }

        participantRepository?.delete(participant)

        val channelId = participantPK.channelId ?: throw AppException(ErrorCode.CHANEL_NOT_FOUND)
        val userId = participantPK.userId ?: throw AppException(ErrorCode.USER_NOT_FOUND)


        // 방을 나간 메시지 생성
        val leaveMessage = ChatMessageRequest(
            channelId = channelId,
            userId = userId,
            content = "", //
            createdAt = LocalDateTime.now(),
            actionType = ChatActionType.EXIT // 0번 코드 전달
        )

        // 방의 모든 사용자에게 나간 알림 전송 (나간 사용자 제외)
        val participants = participantRepository?.findByChannelId(participantPK.channelId)

        participants?.forEach { currentParticipant ->
            if (currentParticipant?.userId != participantPK.userId) { // 나간 사용자는 제외
                chatService?.sendMessage(channelId, leaveMessage)
            }
        }
    }


    @Transactional
    fun removeParticipant(participantPK: ParticipantPK, participantIdToRemove: Long) {
        // 참여자 조회
        val participant = participantRepository?.findById(participantPK)
            ?.orElseThrow { AppException(ErrorCode.NO_PARTICIPANTS) }

        // 현재 참여자가 채널 생성자인지 확인
        if (participant != null) {
            if (!participant.isCreator) {
                throw AppException(ErrorCode.NOT_CHANEL_CREATOR)
            }
        }

        // 제거할 참여자 조회
        val participantToRemove =
            participantRepository?.findById(ParticipantPK(participantIdToRemove, participantPK.channelId))
                ?.orElseThrow { AppException(ErrorCode.PARTICIPANTS_NOT_FOUND) }

        // 참여자 삭제
        participantRepository?.delete(participantToRemove)

        // 킥 메시지 생성
        val channelId = participantPK.channelId ?: throw AppException(ErrorCode.CHANEL_NOT_FOUND)
        val userIdToKick = participantIdToRemove

        val kickMessage = ChatMessageRequest(
            channelId = channelId,
            userId = userIdToKick,
            content = "",
            createdAt = LocalDateTime.now(),
            actionType = ChatActionType.KICK
        )

        // 방의 모든 사용자에게 킥 메시지 전송 (킥된 사용자는 제외)
        val participants = participantRepository?.findByChannelId(channelId)
        participants?.forEach { currentParticipant ->
            if (currentParticipant?.userId != userIdToKick) {
                chatService?.sendMessage(channelId, kickMessage)
            }
        }
    }
}
