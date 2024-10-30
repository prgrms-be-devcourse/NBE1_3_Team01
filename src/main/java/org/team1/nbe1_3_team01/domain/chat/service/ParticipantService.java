package org.team1.nbe1_3_team01.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.chat.controller.request.ChatMessageRequest;
import org.team1.nbe1_3_team01.domain.chat.controller.request.InviteRequest;
import org.team1.nbe1_3_team01.domain.chat.entity.Channel;
import org.team1.nbe1_3_team01.domain.chat.entity.ChatActionType;
import org.team1.nbe1_3_team01.domain.chat.entity.Participant;
import org.team1.nbe1_3_team01.domain.chat.entity.ParticipantPK;
import org.team1.nbe1_3_team01.domain.chat.repository.ChannelRepository;
import org.team1.nbe1_3_team01.domain.chat.repository.ParticipantRepository;
import org.team1.nbe1_3_team01.domain.chat.service.response.ChannelResponse;
import org.team1.nbe1_3_team01.domain.chat.service.response.ParticipantResponse;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_3_team01.global.exception.AppException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;


    // 채널에 참여
    @Transactional
    public ParticipantResponse joinChannel(Long channelId, Long userId) {
        try {
            UserChannelUtil userChannelUtil = new UserChannelUtil(channelRepository, participantRepository);
            Participant existParticipant = userChannelUtil.findUser(userId, channelId);
            return new ParticipantResponse(existParticipant.getUser().getId()); // 이미 참여 중인 경우 userId만 반환
        } catch (RuntimeException e) {
            User user = userRepository.findById(userId).orElseThrow(() -> new AppException(INVITER_NOT_FOUND));
            Channel channel = channelRepository.findById(channelId).orElseThrow(() -> new AppException(CHANEL_NOT_FOUND));

            Participant participant = Participant.builder()
                    .isCreator(false)
                    .participatedAt(LocalDateTime.now())
                    .isParticipated(true)
                    .user(user)
                    .channel(channel)
                    .build();

            participantRepository.save(participant);
            return new ParticipantResponse(user.getId());  // 새로운 참여자의 userId만 반환
        }
    }


    // 사용자 초대
    @Transactional
    public void inviteUser(InviteRequest inviteRequest) {
        ParticipantPK participantPK = new ParticipantPK(inviteRequest.getInviteUserId(), inviteRequest.getChannelId());
        Participant inviter = participantRepository.findById(participantPK)
                .orElseThrow(() -> new AppException(INVITER_NOT_FOUND));

        if (!inviter.isCreator()) {
            throw new AppException(NOT_CHANEL_CREATOR);
        }

        joinChannel(inviteRequest.getChannelId(), inviteRequest.getParticipantId());

        // 입장 코드 전송
        ChatMessageRequest enterMessage = ChatMessageRequest.builder()
                .channelId(inviteRequest.getChannelId())
                .userId(inviteRequest.getParticipantId())
                .actionType(ChatActionType.ENTER)  // 입장코드 9번 전달
                .build();

        chatService.sendMessage(inviteRequest.getChannelId(), enterMessage); // 일단 메시지 없이 코드만 전달
    }

    // 참여중인 채널 조회
    // Service
    @Transactional(readOnly = true)
    public List<ChannelResponse> checkUserChannel(Long userId) {
        List<Participant> participants = participantRepository.findByUserId(userId);

        return participants.stream()
                .map(participant -> {
                    Channel channel = participant.getChannel();
                    return new ChannelResponse(channel.getId(), channel.getChannelName());
                })
                .collect(Collectors.toList());
    }

    // 참여자가 스스로 방을 나감
    @Transactional
    public void leaveChannel(ParticipantPK participantPK) {
        Participant participant = participantRepository.findById(participantPK)
                .orElseThrow(() -> new AppException(PARTICIPANTS_NOT_FOUND));;
        participantRepository.delete(participant);

        // 방을 나간 메시지 생성
        ChatMessageRequest leaveMessage = ChatMessageRequest.builder()
                .channelId(participantPK.getChannelId())
                .userId(participantPK.getUserId())  // 나간 사용자 ID 설정
                .actionType(ChatActionType.EXIT)  // 0번 코드 전달
                .build();

        // 방의 모든 사용자에게 나간 알림 전송 (나간 사용자 제외)
        List<Participant> participants = participantRepository.findByChannelId(participantPK.getChannelId());
        for (Participant currentParticipant : participants) {
            if (!currentParticipant.getUserId().equals(participantPK.getUserId())) {  // 나간 사용자는 제외
                chatService.sendMessage(participantPK.getChannelId(), leaveMessage);
            }
        }
    }

    // 방장이 참여자를 강퇴
    @Transactional
    public void removeParticipant(ParticipantPK participantPK, Long participantIdToRemove) {
        // 현재 참여자 정보 가져옴 (강퇴 요청한 사람)
        Participant participant = participantRepository.findById(participantPK)
                .orElseThrow(() -> new AppException(NO_PARTICIPANTS));

        // 채널 생성자가 아님
        if (!participant.isCreator()) {
            throw new AppException(NOT_CHANEL_CREATOR);
        }

        // 강퇴당할 사용자 찾기
        Participant participantToRemove = participantRepository.findById(new ParticipantPK(participantIdToRemove, participantPK.getChannelId()))
                .orElseThrow(() -> new AppException(PARTICIPANTS_NOT_FOUND));

        // 강퇴 처리 (채널에서 해당 사용자 제거)
        participantRepository.delete(participantToRemove);

        // 메시지 생성
        ChatMessageRequest kickMessage = ChatMessageRequest.builder()
                .channelId(participantPK.getChannelId())
                .userId(participantIdToRemove)  // 강퇴당한 사용자 ID 설정
                .actionType(ChatActionType.KICK)  // 8번 코드 전달
                .build();

        // 채널의 모든 사용자에게 강퇴 알림 (강퇴당한 사용자 제외)
        List<Participant> participants = participantRepository.findByChannelId(participantPK.getChannelId());
        for (Participant currentParticipant : participants) {
            if (!currentParticipant.getUserId().equals(participantIdToRemove)) {  // 강퇴당한 사용자는 제외
                chatService.sendMessage(participantPK.getChannelId(), kickMessage);
            }
        }
    }
}