package org.team1.nbe1_3_team01.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_3_team01.domain.chat.controller.request.ChannelRequest;
import org.team1.nbe1_3_team01.domain.chat.controller.request.ChannelUpdateRequest;
import org.team1.nbe1_3_team01.domain.chat.controller.request.ChannelCreateRequest;
import org.team1.nbe1_3_team01.domain.chat.service.ChannelService;

import java.util.List;

@RequestMapping("/api/channel")
@RestController
@RequiredArgsConstructor
public class ChannelController {

    private final ChannelService channelService;

    @GetMapping
    public ResponseEntity<List<String>> showAllChannel() {
        List<String> channelList = channelService.showAllChannel();
        return ResponseEntity.ok().body(channelList);
    }

    @PostMapping
    public ResponseEntity<Long> createChannel(@RequestBody ChannelCreateRequest channelCreateRequest) {
        Long pk = channelService.createChannel(channelCreateRequest.getCreatorUserId(), channelCreateRequest.getChannelName());
        return ResponseEntity.status(HttpStatus.CREATED).body(pk);
    }

    @PatchMapping("/update")
    public ResponseEntity<Long> updateChannel(@RequestBody ChannelUpdateRequest channelUpdateRequest) {
        Long pk = channelService.updateChannel(channelUpdateRequest.getUserId(), channelUpdateRequest.getChannelId(), channelUpdateRequest.getChannelName());
        return ResponseEntity.status(HttpStatus.OK).body(pk);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteChannel(@RequestBody ChannelRequest channelRequest) {
        channelService.deleteChannel(channelRequest.getUserId(), channelRequest.getChannelId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

