package org.team1.nbe1_3_team01.domain.chat.service

import jakarta.persistence.EntityNotFoundException
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.chat.controller.request.ChatMessageRequest
import org.team1.nbe1_3_team01.domain.chat.controller.request.EmoticonMessageRequest
import org.team1.nbe1_3_team01.domain.chat.entity.Chat
import org.team1.nbe1_3_team01.domain.chat.entity.ChatActionType
import org.team1.nbe1_3_team01.domain.chat.entity.Participant
import org.team1.nbe1_3_team01.domain.chat.entity.ParticipantPK
import org.team1.nbe1_3_team01.domain.chat.repository.ChatRepository
import org.team1.nbe1_3_team01.domain.chat.repository.ParticipantRepository
import org.team1.nbe1_3_team01.domain.chat.service.response.ChatMessageResponse
import org.team1.nbe1_3_team01.domain.chat.service.response.ChatResponse
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import java.time.LocalDateTime

@Service
class ChatService (
    private val chatRepository: ChatRepository,
    private val participantRepository: ParticipantRepository)
{
    // 채팅 보내기
    @Transactional
    fun sendMessage(channelId: Long, msgRequest: ChatMessageRequest): ChatMessageResponse {
        try {
            // 일반 채팅이라 sendMessage 코드 사용
            val updatedRequest = msgRequest.copy(
                actionType = msgRequest.actionType ?: ChatActionType.SEND_MESSAGE
            )

            val participant = participantRepository.findByUserIdAndChannelId(updatedRequest.userId, channelId)
                ?.orElseThrow { AppException(ErrorCode.PARTICIPANTS_NOT_FOUND) }

            val chat = Chat(
                actionType = updatedRequest.actionType,
                content = updatedRequest.content,
                createdAt = LocalDateTime.now(),
                participant = participant
            )
            chatRepository.save(chat)

            return ChatMessageResponse(
                channelId = channelId,
                userId = updatedRequest.userId,
                content = chat.content,
                createdAt = chat.createdAt ?: LocalDateTime.now()
            )
        } catch (e: EntityNotFoundException) {
            throw AppException(ErrorCode.PARTICIPANTS_NOT_FOUND)
        }
    }



    // 이모티콘 보내기
    @Transactional
    fun sendEmoticon(channelId: Long, emoticonMessageRequest: EmoticonMessageRequest): ChatMessageResponse {
        // 이모티콘 메시지 요청 객체 생성
        val emoticonMessage = ChatMessageRequest(
            channelId = channelId,
            userId = emoticonMessageRequest.userId,
            content = emoticonMessageRequest.emoticonUrl, // 이모티콘 URL 설정
            createdAt = LocalDateTime.now(),
            actionType = ChatActionType.SEND_EMOTICON // 이모티콘 액션 타입 설정
        )

        // 실제 메시지 전송
        return sendMessage(channelId, emoticonMessage)
    }


    // 채팅 수정하기
    fun updateMessage(chatId: Long?, userId: Long?, newChatMessage: String): Long {
        // 채팅 메시지 조회
        val chat = chatRepository.findById(chatId)
            .orElseThrow { AppException(ErrorCode.NOT_CHAT_MESSAGE) }

        // 사용자가 해당 채팅 메시지의 작성자인지 확인
        if (chat?.participant?.user?.id != userId) {
            throw AppException(ErrorCode.USER_NOT_AUTHORIZE)
        }

        // 메시지 내용 및 작성 시간 업데이트
        chat?.content = newChatMessage
        chat?.createdAt = LocalDateTime.now()

        return chat?.id ?: throw AppException(ErrorCode.NOT_CHAT_MESSAGE) // null 일 경우 예외 발생

    }


    fun deleteMessage(chatId: Long?, userId: Long?) {
        // chat 확인
        val chat: Chat = chatId?.let {
            chatRepository.findById(it)
                .orElseThrow { AppException(ErrorCode.NOT_CHAT_MESSAGE) }
        } ?: throw AppException(ErrorCode.NOT_CHAT_MESSAGE) // chatId가 null인 경우 예외 발생

        // 메시지 보낸 사람과 동일인인지 확인
        if (chat.participant?.user?.id != userId) {
            throw AppException(ErrorCode.USER_NOT_AUTHORIZE)
        }
        chatRepository.delete(chat)
    }

//    // 채팅 만들기
//    @Transactional
//    fun createChat(channelId: Long, message: String, userId: Long?): Chat {
//        val participantPK = ParticipantPK(userId, channelId)
//
//
//        val participant: Participant = participantRepository?.findById(participantPK)
//            ?.orElseThrow { AppException(ErrorCode.PARTICIPANTS_NOT_FOUND) } as Participant // 캐스팅
//
//        // 채널 ID를 통해 참가자가 해당 채널에 소속되어 있는지 확인
//        if (participant.channelId != channelId) {
//            throw AppException(ErrorCode.NO_PARTICIPANTS)
//        }
//
//        // 채팅 생성 및 저장
//        return chatRepository?.save(
//            Chat(
//                content = message,
//                createdAt = LocalDateTime.now(),
//                participant = participant
//            )
//        ) ?: throw AppException(ErrorCode.CHAT_REPOSITORY_NOT_FOUND)
//    }


    // 채팅 불러오기
    @Transactional(readOnly = true)
    fun getChatsByChannelId(channelId: Long): List<ChatResponse> {
        val chats: List<Chat> = chatRepository.findByParticipant_Channel_Id(channelId)
            ?.filterNotNull() // null 이 아닌 Chat만 필터링
            ?: throw AppException(ErrorCode.NOT_CHAT)

        if (chats.isEmpty()) {
            throw AppException(ErrorCode.NOT_CHAT)
        }

        return chats.map { chat ->
            ChatResponse(
                userId = chat.participant?.user?.id!!,
                content = chat.content,
                userName = chat.participant?.user?.name!!,
                createdAt = chat.createdAt ?: LocalDateTime.now()
            )
        }
    }


    // 현재 채팅방에 누가 있는지 확인 (이름으로 반환)
    fun showParticipant(channelId: Long): List<String> {
        val participants = participantRepository?.findByChannelId(channelId).orEmpty()

        if (participants.isEmpty()) {
            throw AppException(ErrorCode.PARTICIPANTS_NOT_FOUND)
        }

        return participants.map { participant ->
            participant?.user?.name!!
        }
    }
}