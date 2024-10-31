//package org.team1.nbe1_3_team01.domain.chat.service;
//
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.team1.nbe1_3_team01.domain.chat.controller.request.ChatMessageRequest;
//import org.team1.nbe1_3_team01.domain.chat.controller.request.EmoticonMessageRequest;
//import org.team1.nbe1_3_team01.domain.chat.entity.Chat;
//import org.team1.nbe1_3_team01.domain.chat.entity.ChatActionType;
//import org.team1.nbe1_3_team01.domain.chat.entity.Participant;
//import org.team1.nbe1_3_team01.domain.chat.entity.ParticipantPK;
//import org.team1.nbe1_3_team01.domain.chat.repository.ChatRepository;
//import org.team1.nbe1_3_team01.domain.chat.repository.ParticipantRepository;
//import org.team1.nbe1_3_team01.domain.chat.service.response.ChatMessageResponse;
//import org.team1.nbe1_3_team01.domain.chat.service.response.ChatResponse;
//import org.team1.nbe1_3_team01.global.exception.AppException;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.team1.nbe1_3_team01.global.util.ErrorCode.*;
//
//@Service
//@RequiredArgsConstructor
//public class ChatService {
//
//    private final ChatRepository chatRepository;
//    private final ParticipantRepository participantRepository;
//
//    // 채팅 보내기
//    @Transactional
//    public ChatMessageResponse sendMessage(Long channelId, ChatMessageRequest msgRequest) {
//        try {
//            if (msgRequest.getActionType() == null) {
//                msgRequest.setActionType(ChatActionType.SEND_MESSAGE); // Null 이면  SEND_MESSAGE로 설정
//            }
//            Chat chat = createChat(channelId, msgRequest.getContent(), msgRequest.getUserId()); // 채팅방을 만드는 메소드
//
//            return ChatMessageResponse.builder()
//                    .channelId(channelId)
//                    .userId(chat.getParticipant().getUserId())
//                    .content(chat.getContent())
//                    .createdAt(LocalDateTime.now())
//                    .build();
//        } catch (EntityNotFoundException e) {
//            throw new AppException(PARTICIPANTS_NOT_FOUND);
//        }
//    }
//
//    // 이모티콘 보내기
//    @Transactional
//    public ChatMessageResponse sendEmoticon(Long channelId, EmoticonMessageRequest emoticonMessageRequest) {
//        ChatMessageRequest emoticonMessage = ChatMessageRequest.builder()
//                .channelId(channelId)
//                .userId(emoticonMessageRequest.getUserId())
//                .content(emoticonMessageRequest.getEmoticonUrl())  // 일단은 url 로 설정
//                .actionType(ChatActionType.SEND_EMOTICON)  // 이모티콘 액션 타입 설정
//                .build();
//
//        // 실제 메시지 전송
//        return sendMessage(channelId, emoticonMessage);
//    }
//
//    // 채팅 수정하기
//    public Long updateMessage(Long chatId, Long userId, String newChatMessage) {
//        Chat chat = chatRepository.findById(chatId)
//                .orElseThrow(() -> new AppException(NOT_CHAT_MESSAGE));
//
//        if (!chat.getParticipant().getUser().getId().equals(userId)) {
//            throw new AppException(USER_NOT_AUTHORIZE);
//        }
//        chat.setContent(newChatMessage);
//        chat.setCreatedAt(LocalDateTime.now());
//        return chat.getId();
//    }
//
//    // 채팅 삭제하기
//    public void deleteMessage(Long chatId, Long userId) {
//        // chat 확인
//        Chat chat = chatRepository.findById(chatId)
//                .orElseThrow(() -> new AppException(NOT_CHAT_MESSAGE));
//
//        // 메시지 보낸 사람과 동일인인지 확인
//        if (!chat.getParticipant().getUser().getId().equals(userId)) {
//            throw new AppException(USER_NOT_AUTHORIZE);
//        }
//    }
//
//    // 채팅 만들기
//    @Transactional
//    public Chat createChat(Long channelId, String message, Long userId) {
//        ParticipantPK participantPK = new ParticipantPK(userId, channelId);
//
//        Participant participant = participantRepository.findById(participantPK)
//                .orElseThrow(() -> new AppException(PARTICIPANTS_NOT_FOUND));
//
//        // 채널 ID를 통해 참가자가 해당 채널에 소속되어 있는지 확인
//        if (!participant.getChannelId().equals(channelId)) {
//            throw new AppException(NO_PARTICIPANTS);
//        }
//
//        return chatRepository.save(Chat.builder()
//                .content(message)
//                .createdAt(LocalDateTime.now())
//                .participant(participant)
//                .build());
//    }
//
//    // 채팅 불러오기
//    @Transactional(readOnly = true)
//    public List<ChatResponse> getChatsByChannelId(Long channelId) {
//        List<Chat> chats = chatRepository.findByParticipant_Channel_Id(channelId);
//
//        if (chats.isEmpty()) {
//            throw new AppException(NOT_CHAT);
//        }
//        return chats.stream()
//                .map(chat -> new ChatResponse(chat.getId(), chat.getContent(), chat.getParticipant().getUser().getName(), chat.getCreatedAt()))
//                .collect(Collectors.toList());
//    }
//
//    // 현재 채팅방에 누가 있는지 확인 (이름으로 반환)
//    public List<String> showParticipant(Long channelId) {
//        List<Participant> participants = participantRepository.findByChannelId(channelId);
//
//        if (participants.isEmpty()) {
//            throw new AppException(PARTICIPANTS_NOT_FOUND);
//        }
//
//        return participants.stream()
//                .map(participant -> participant.getUser().getName())
//                .collect(Collectors.toList());
//    }
//}