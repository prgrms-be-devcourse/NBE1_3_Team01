package org.team1.nbe1_3_team01.domain.board.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_3_team01.domain.board.controller.dto.*;
import org.team1.nbe1_3_team01.domain.board.service.CourseBoardService;
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_3_team01.domain.board.service.response.CourseBoardResponse;
import org.team1.nbe1_3_team01.domain.board.service.response.PagingResponse;
import org.team1.nbe1_3_team01.global.util.Message;
import org.team1.nbe1_3_team01.global.util.Response;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/board")
public class CourseBoardController {

    private final CourseBoardService courseBoardService;
    private static final String BASE_URL = "/api/course/board";

    /**
     * 공지사항 || 스터디 모집글 목록 조회 api
     * 관리자가 작성하고, 해당 코스 번호에 해당하는 게시글 리스트
     * @param request
     * @return
     */
    @GetMapping
    public ResponseEntity<Response<List<CourseBoardResponse>>> getCourseBoardList(
            @ModelAttribute CourseBoardListRequest request
    ) {
        List<CourseBoardResponse> commonBoardList = courseBoardService.getCourseBoardList(request);
        return ResponseEntity.ok()
                .body(Response.success(commonBoardList));
    }

    /**
     * 페이징 데이터 전달하는 쿼리
     * 각 페이지와 마지막 boardId값 반환
     * @param request
     * @return
     */
    @GetMapping("/page")
    public ResponseEntity<Response<List<PagingResponse>>> getPaginationInfo(
            @ModelAttribute CourseBoardListRequest request
    ) {
        List<PagingResponse> response = courseBoardService.getPaginationInfo(request);
        return ResponseEntity.ok()
                .body(Response.success(response));
    }


    /**
     * 공지사항 & 스터디 모집글 작성
     * (나중에 게시글 작성으로 확장해볼 수 있을 것 같음)
     * @param request 등록할 공지사항 정보
     * @return
     */
    @PostMapping
    public ResponseEntity<Response<Message>> addCommonBoard(
            @RequestBody @Valid CourseBoardRequest request
    ) {
        Message message = courseBoardService.addCourseBoard(request);
        return ResponseEntity.ok()
                .body(Response.success(message));
    }

    /**
     * 코스 내 게시글 상세 조회
     * @param courseBoardId
     * @return
     */
    @GetMapping("/{courseBoardId}")
    public ResponseEntity<Response<BoardDetailResponse>> getBoardDetailById(
            @PathVariable Long courseBoardId
    ) {
        BoardDetailResponse data = courseBoardService.getCourseBoardDetailById(courseBoardId);
        return ResponseEntity.ok()
                .body(Response.success(data));
    }

    /**
     * 코스 내 게시글 수정 기능
     * @param request
     * @return
     */
    @PatchMapping
    public ResponseEntity<Response<Message>> updateCourseBoard(
            @RequestBody @Valid CourseBoardUpdateRequest request
    ) {
        URI uri = URI.create(BASE_URL + "/" + request.courseBoardId());
        Message message = courseBoardService.updateCourseBoard(request);

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
        Message message = courseBoardService.deleteCourseBoardById(deleteRequest);
        return ResponseEntity.ok()
                .body(Response.success(message));
    }
}
