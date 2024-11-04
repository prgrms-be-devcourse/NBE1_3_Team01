package org.team1.nbe1_3_team01.domain.board.controller

import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team1.nbe1_3_team01.domain.board.controller.dto.*
import org.team1.nbe1_3_team01.domain.board.service.TeamBoardService
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse
import org.team1.nbe1_3_team01.domain.board.service.response.PagingResponse
import org.team1.nbe1_3_team01.domain.board.service.response.TeamBoardResponse
import org.team1.nbe1_3_team01.global.util.Message
import org.team1.nbe1_3_team01.global.util.Response
import java.net.URI

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/board")
class TeamBoardController(
    private val teamBoardService: TeamBoardService
) {


    /**
     * 팀 내에서 카테고리에 맞는 게시글 리스트 조회
     * 만약 categoryId가 null이면, 전체 카테고리 데이터 가져옴
     * @param request
     * @return
     */
    @GetMapping
    fun getTeamBoardList(
        @ModelAttribute request: TeamBoardListRequest?
    ): ResponseEntity<Response<List<TeamBoardResponse?>?>> {
        val boardList = teamBoardService.getTeamBoardListByType(request!!)
        return ResponseEntity.ok()
            .body(Response.success(boardList))
    }

    /**
     * 페이징 데이터 전달하는 쿼리
     * 각 페이지와 마지막 boardId값 반환
     * @param request
     * @return
     */
    @GetMapping("/page")
    fun getPaginationInfo(
        @ModelAttribute request: TeamBoardListRequest
    ): ResponseEntity<Response<List<PagingResponse?>?>> {
        val response = teamBoardService.getPaginationInfo(request)
        return ResponseEntity.ok()
            .body(Response.success(response))
    }

    /**
     * 팀 게시글 작성
     * @param request 등록할 팀 게시글 정보
     * @return
     */
    @PostMapping
    fun addTeamBoard(
        @RequestBody request: @Valid TeamBoardRequest?
    ): ResponseEntity<Response<Message>> {
        val message = teamBoardService.addTeamBoard(request!!)
        return ResponseEntity.ok()
            .body(Response.success(message))
    }

    /**
     * 팀 게시글 상세 조회
     * @param teamBoardId
     * @return
     */
    @GetMapping("/{teamBoardId}")
    fun getBoardDetailById(
        @PathVariable teamBoardId: Long
    ): ResponseEntity<Response<BoardDetailResponse?>> {
        val data = teamBoardService.getTeamBoardDetailById(teamBoardId)
        return ResponseEntity.ok()
            .body(Response.success(data))
    }

    /**
     * 팀 게시글 수정
     * @param updateRequest
     * @return
     */
    @PatchMapping
    fun updateTeamBoard(
        @RequestBody updateRequest: @Valid TeamBoardUpdateRequest?
    ): ResponseEntity<Response<Message>> {
        val message = teamBoardService.updateTeamBoard(updateRequest!!)
        val uri = URI.create(BASE_URL + "/" + updateRequest.teamBoardId)

        return ResponseEntity.created(uri)
            .body(Response.success(message))
    }

    /**
     * 코스 내 게시글 삭제
     * @param deleteRequest
     * @return
     */
    @DeleteMapping
    fun deleteCourseBoardById(
        @RequestBody deleteRequest: @Valid BoardDeleteRequest?
    ): ResponseEntity<Response<Message>> {
        val message = teamBoardService.deleteTeamBoardById(deleteRequest!!)
        return ResponseEntity.ok()
            .body(Response.success(message))
    }

    companion object {
        private const val BASE_URL = "/api/team/board"
    }
}
