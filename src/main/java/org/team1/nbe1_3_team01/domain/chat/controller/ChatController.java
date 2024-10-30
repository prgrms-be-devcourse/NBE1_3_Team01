package org.team1.nbe1_3_team01.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_3_team01.domain.chat.controller.request.EmoticonMessageRequest;
import org.team1.nbe1_3_team01.domain.chat.service.response.ChatMessageResponse;
import org.team1.nbe1_3_team01.domain.chat.service.response.ChatResponse;
import org.team1.nbe1_3_team01.domain.chat.controller.request.ChatMessageRequest;
import org.team1.nbe1_3_team01.domain.chat.service.ChatService;
import org.team1.nbe1_3_team01.global.util.Response;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class ChatController {

    private final ChatService chatService;

    // 메시지 보내기
    @MessageMapping("/chat/{channelId}")
    @SendTo("/topic/chat/{channelId}")
    public ChatMessageResponse sendMessage(@DestinationVariable Long channelId, @Payload ChatMessageRequest msgRequest) {
        return chatService.sendMessage(channelId, msgRequest);
    }

    // 이모티콘 보내기
    @MessageMapping("/chat/{channelId}/emoticon")
    @SendTo("/topic/chat/{channelId}/emoticon")
    public ChatMessageResponse sendEmoticon(@DestinationVariable Long channelId, @Payload EmoticonMessageRequest emoticonRequest) {
        return chatService.sendEmoticon(channelId, emoticonRequest);
    }

    // 채팅방 목록을 불러오기
    @GetMapping("/chats/{channelId}")
    public ResponseEntity<Response<List<ChatResponse>>> getChatsByChannelId(@PathVariable("channelId") Long channelId) {
        List<ChatResponse> chatResponses = chatService.getChatsByChannelId(channelId);
        return ResponseEntity.ok().body(Response.success(chatResponses));
    }

    // 현재 채팅방에 누가 있는지 확인 (카톡처럼 이름으로 반환)
    @GetMapping("/chat/{channelId}/participants")
    public ResponseEntity<Response<List<String>>> getParticipants(@PathVariable Long channelId) {
        List<String> participantNames = chatService.showParticipant(channelId);
        return ResponseEntity.ok().body(Response.success(participantNames));
    }

    // 채팅 수정하기
    @PatchMapping("/chat/{channelId}/{chatId}")
    public ResponseEntity<?> updateChatMessage(@PathVariable Long channelId, @PathVariable Long chatId,
                                               @RequestParam Long userId, @RequestParam String newChatMessage) {
        Long updateChatPk = chatService.updateMessage(chatId, userId, newChatMessage);
        return ResponseEntity.ok().body(Response.success(updateChatPk));
    }

    // 채팅 삭제하기
    @DeleteMapping("/chat/{channelId}/{chatId}")
    public ResponseEntity<?> deleteChatMessage(@PathVariable Long channelId, @PathVariable Long chatId, @RequestParam Long userId) {
        chatService.deleteMessage(chatId, userId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}