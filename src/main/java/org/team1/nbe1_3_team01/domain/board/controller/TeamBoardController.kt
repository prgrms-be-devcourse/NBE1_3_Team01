package org.team1.nbe1_3_team01.domain.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_3_team01.domain.board.controller.dto.*;
import org.team1.nbe1_3_team01.domain.board.service.TeamBoardService;
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_3_team01.domain.board.service.response.TeamBoardResponse;
import org.team1.nbe1_3_team01.global.util.Message;
import org.team1.nbe1_3_team01.global.util.Response;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/team/board")
public class TeamBoardController {

    private final TeamBoardService teamBoardService;
    private static final String BASE_URL = "/api/team/board";


    /**
     * 팀 내에서 카테고리에 맞는 게시글 리스트 조회
     * 만약 categoryId가 null이면, 전체 카테고리 데이터 가져옴
     * @param request
     * @return
     */
    @GetMapping
    public ResponseEntity<Response<List<TeamBoardResponse>>> getTeamBoardList(
            @ModelAttribute TeamBoardListRequest request
    ) {
        List<TeamBoardResponse> boardList = teamBoardService.getTeamBoardListByType(request);
        return ResponseEntity.ok()
                .body(Response.success(boardList));
    }

    /**
     * 팀 게시글 작성
     * @param request 등록할 팀 게시글 정보
     * @return
     */
    @PostMapping
    public ResponseEntity<Response<Message>> addTeamBoard(
            @RequestBody @Valid TeamBoardRequest request
    ) {
        Message message = teamBoardService.addTeamBoard(request);
        return ResponseEntity.ok()
                .body(Response.success(message));
    }

    /**
     * 팀 게시글 상세 조회
     * @param teamBoardId
     * @return
     */
    @GetMapping("/{teamBoardId}")
    public ResponseEntity<Response<BoardDetailResponse>> getBoardDetailById(
            @PathVariable Long teamBoardId
    ) {
        BoardDetailResponse data = teamBoardService.getTeamBoardDetailById(teamBoardId);
        return ResponseEntity.ok()
                .body(Response.success(data));
    }

    /**
     * 팀 게시글 수정
     * @param updateRequest
     * @return
     */
    @PatchMapping
    public ResponseEntity<Response<Message>> updateTeamBoard(
            @RequestBody @Valid TeamBoardUpdateRequest updateRequest
    ) {
        Message message = teamBoardService.updateTeamBoard(updateRequest);
        URI uri = URI.create(BASE_URL + "/" + updateRequest.teamBoardId());

        return ResponseEntity.created(uri)
                .body(Response.success(message));
    }

    /**
     * 코스 내 게시글 삭제
     * @param deleteRequest
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Response<Message>> deleteCourseBoardById(
            @RequestBody @Valid BoardDeleteRequest deleteRequest
    ) {
        Message message = teamBoardService.deleteTeamBoardById(deleteRequest);
        return ResponseEntity.ok()
                .body(Response.success(message));
    }
}
