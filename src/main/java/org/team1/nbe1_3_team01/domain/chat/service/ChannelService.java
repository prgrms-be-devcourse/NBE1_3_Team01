package org.team1.nbe1_3_team01.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.chat.entity.Channel;
import org.team1.nbe1_3_team01.domain.chat.entity.Participant;
import org.team1.nbe1_3_team01.domain.chat.repository.ChannelRepository;
import org.team1.nbe1_3_team01.domain.chat.repository.ParticipantRepository;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_3_team01.global.exception.AppException;

import java.time.LocalDateTime;
import java.util.List;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final UserChannelUtil userChannelUtil;


    // 채널 목록 전체 조회
    public List<String> showAllChannel() {
        return channelRepository.findAllChannelName();
    }


    // 채널 생성
    @Transactional
    public Long createChannel(Long creatorUserId, String channelName) {
        User creator = userRepository.findById(creatorUserId)
                .orElseThrow(() -> new AppException(INVITER_NOT_FOUND));

        Channel channel = Channel.builder()
                .channelName(channelName)
                .build();

        Channel saveChannel = channelRepository.save(channel);

        // 참여자 추가
        Participant participant = Participant.builder()
                .user(creator)
                .channel(channel)
                .isCreator(true) // 생성자니까 true
                .participatedAt(LocalDateTime.now())
                .isParticipated(true) // 필요에 따라 설정
                .build();

        participantRepository.save(participant);

        return saveChannel.getId();
    }


    // 채널 수정
    @Transactional
    public Long updateChannel(Long userId, Long channelId, String channelName) {
        Participant participant = userChannelUtil.findUser(userId, channelId);

        if (!participant.isCreator()) {
            throw new AppException(NOT_CHANEL_CREATOR);
        }

        Channel channel = participant.getChannel();

        channel.setChannelName(channelName); // 수정이라 빌더 패턴 대신 set 사용

        channelRepository.save(channel);

        return channel.getId(); // pk값 반환
    }

    // 채널 삭제
    @Transactional
    public void deleteChannel(Long userId, Long channelId) {
        Participant participant = userChannelUtil.findUser(userId, channelId);

        if (!participant.isCreator()) {
            throw new AppException(NOT_CHANEL_DELETE);
        }

        List<Participant> participants = participantRepository.findByChannelId(channelId);
        participantRepository.deleteAll(participants); // 모든 참여자 먼저 삭제

        Channel channel = participant.getChannel();
        channelRepository.delete(channel);
    }

}
