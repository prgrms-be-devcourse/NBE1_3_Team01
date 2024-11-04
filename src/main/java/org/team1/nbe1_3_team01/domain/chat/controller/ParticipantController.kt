package org.team1.nbe1_3_team01.domain.chat.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team1.nbe1_3_team01.domain.chat.controller.request.ChannelRequest
import org.team1.nbe1_3_team01.domain.chat.controller.request.InviteRequest
import org.team1.nbe1_3_team01.domain.chat.controller.request.KickOutRequest
import org.team1.nbe1_3_team01.domain.chat.entity.ParticipantPK
import org.team1.nbe1_3_team01.domain.chat.entity.QParticipantPK.participantPK
import org.team1.nbe1_3_team01.domain.chat.service.ParticipantService
import org.team1.nbe1_3_team01.domain.chat.service.response.ChannelResponse
import org.team1.nbe1_3_team01.domain.chat.service.response.ParticipantResponse
import org.team1.nbe1_3_team01.global.util.Response

@RequestMapping("/api/participants")
@RestController
class ParticipantController(private val participantService: ParticipantService)
{
    // 참여자가 스스로 방 아이디를 치고 들어오는 경우
    @PostMapping("/join")
    fun joinChannel(@RequestBody channelRequest: ChannelRequest): ResponseEntity<Response<ParticipantResponse>> {
        val participantResponse = participantService.joinChannel(channelRequest.channelId)
        return ResponseEntity.ok().body(Response.success(participantResponse))
    }

    // 방장(생성자) -> 참여자를 초대
    @PostMapping("/invite")
    fun inviteUser(@RequestBody inviteRequest: InviteRequest): ResponseEntity<Void> {
        participantService.inviteUser(inviteRequest)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    // 참여자가 속해 있는 채널들 보기
    @GetMapping("/show")
    fun checkUserChannels(): ResponseEntity<Response<List<ChannelResponse>>> {
        val responses = participantService.checkUserChannel()
        return ResponseEntity.ok().body(Response.success(responses))
    }

    // 참여자가 스스로 방을 나가기 (채널 탈퇴)
    @DeleteMapping("/leave")
    fun leaveChannel(@RequestBody channelRequest: ChannelRequest): ResponseEntity<Void> {
        participantService.leaveChannel(channelRequest.channelId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    // 방장이 참여자를 내보내기 (강퇴)
    @DeleteMapping("/kickOut")
    fun removeParticipant(@RequestBody kickOutRequest: KickOutRequest): ResponseEntity<String> {
        val creator = ParticipantPK(kickOutRequest.creatorId, kickOutRequest.channelId)
        participantService.removeParticipant(creator, kickOutRequest.participantIdToRemove)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }
}