package org.team1.nbe1_3_team01.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.team1.nbe1_3_team01.domain.chat.entity.Participant;
import org.team1.nbe1_3_team01.domain.chat.repository.ChannelRepository;
import org.team1.nbe1_3_team01.domain.chat.repository.ParticipantRepository;

@RequiredArgsConstructor
@Service
public class UserChannelUtil {

    private final ChannelRepository channelRepository;
    private final ParticipantRepository participantRepository;

    public Participant findUser(Long userId, Long channelId) {
        channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("채널을 찾을 수 없습니다."));

        Participant participant = participantRepository.findByUserIdAndChannelId(userId, channelId)
                .orElseThrow(() -> new RuntimeException("참여자를 찾을 수 없습니다."));

        return participant;
    }
}
