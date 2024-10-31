package org.team1.nbe1_3_team01.domain.chat.controller

import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team1.nbe1_3_team01.domain.chat.controller.request.ChannelCreateRequest
import org.team1.nbe1_3_team01.domain.chat.controller.request.ChannelRequest
import org.team1.nbe1_3_team01.domain.chat.controller.request.ChannelUpdateRequest
import org.team1.nbe1_3_team01.domain.chat.service.ChannelService
import org.team1.nbe1_3_team01.global.util.Response

@RequestMapping("/api/channel")
@RestController
@RequiredArgsConstructor
class ChannelController {
    private val channelService: ChannelService? = null

    @GetMapping
    fun showAllChannel(): ResponseEntity<Response<List<String>>> {
        val channelList = channelService!!.showAllChannel()
        return ResponseEntity.ok().body(Response.success(channelList))
    }

    @PostMapping
    fun createChannel(@RequestBody channelCreateRequest: ChannelCreateRequest): ResponseEntity<Response<Long>> {
        val pk = channelService!!.createChannel(channelCreateRequest.creatorUserId, channelCreateRequest.channelName)
        return ResponseEntity.ok().body(Response.success(pk))
    }

    @PatchMapping("/update")
    fun updateChannel(@RequestBody channelUpdateRequest: ChannelUpdateRequest): ResponseEntity<Response<Long>> {
        val pk = channelService!!.updateChannel(
            channelUpdateRequest.userId,
            channelUpdateRequest.channelId,
            channelUpdateRequest.channelName
        )
        return ResponseEntity.ok().body(Response.success(pk))
    }

    @DeleteMapping("/delete")
    fun deleteChannel(@RequestBody channelRequest: ChannelRequest): ResponseEntity<Void> {
        channelService!!.deleteChannel(channelRequest.userId, channelRequest.channelId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}

