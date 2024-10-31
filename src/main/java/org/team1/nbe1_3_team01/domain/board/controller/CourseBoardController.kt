package org.team1.nbe1_3_team01.domain.board.controller

import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team1.nbe1_3_team01.domain.board.controller.dto.BoardDeleteRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardListRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardUpdateRequest
import org.team1.nbe1_3_team01.domain.board.service.CourseBoardService
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse
import org.team1.nbe1_3_team01.domain.board.service.response.CourseBoardResponse
import org.team1.nbe1_3_team01.domain.board.service.response.PagingResponse
import org.team1.nbe1_3_team01.global.util.Message
import org.team1.nbe1_3_team01.global.util.Response
import java.net.URI


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/course/board")
class CourseBoardController(
    private val courseBoardService: CourseBoardService
) {
    /**
     * 공지사항 || 스터디 모집글 목록 조회 api
     * 관리자가 작성하고, 해당 코스 번호에 해당하는 게시글 리스트
     * @param request
     * @return
     */
    @GetMapping
    fun getCourseBoardList(
        @ModelAttribute request: CourseBoardListRequest
    ): ResponseEntity<Response<List<CourseBoardResponse>>> {
        val commonBoardList = courseBoardService.getCourseBoardList(request)
        return ResponseEntity.ok()
            .body(Response.success(commonBoardList))
    }

    /**
     * 페이징 데이터 전달하는 쿼리
     * 각 페이지와 마지막 boardId값 반환
     * @param request
     * @return
     */
    @GetMapping("/page")
    fun getPaginationInfo(
        @ModelAttribute request: CourseBoardListRequest
    ): ResponseEntity<Response<List<PagingResponse?>?>> {
        val response = courseBoardService.getPaginationInfo(request)
        return ResponseEntity.ok()
            .body(Response.success(response))
    }


    /**
     * 공지사항 & 스터디 모집글 작성
     * (나중에 게시글 작성으로 확장해볼 수 있을 것 같음)
     * @param request 등록할 공지사항 정보
     * @return
     */
    @PostMapping
    fun addCommonBoard(
        @RequestBody request: @Valid CourseBoardRequest
    ): ResponseEntity<Response<Message>> {
        val message = courseBoardService.addCourseBoard(request)
        return ResponseEntity.ok()
            .body(Response.success(message))
    }

    /**
     * 코스 내 게시글 상세 조회
     * @param courseBoardId
     * @return
     */
    @GetMapping("/{courseBoardId}")
    fun getBoardDetailById(
        @PathVariable courseBoardId: Long
    ): ResponseEntity<Response<BoardDetailResponse?>> {
        val data = courseBoardService.getCourseBoardDetailById(courseBoardId)
        return ResponseEntity.ok()
            .body(Response.success(data))
    }

    /**
     * 코스 내 게시글 수정 기능
     * @param request
     * @return
     */
    @PatchMapping
    fun updateCourseBoard(
        @RequestBody request: @Valid CourseBoardUpdateRequest?
    ): ResponseEntity<Response<Message>> {
        val uri = URI.create(BASE_URL + "/" + request!!.courseBoardId)
        val message = courseBoardService.updateCourseBoard(request)

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
        @RequestBody deleteRequest: @Valid BoardDeleteRequest
    ): ResponseEntity<Response<Message>> {
        val message = courseBoardService.deleteCourseBoardById(deleteRequest)
        return ResponseEntity.ok()
            .body(Response.success(message))
    }

    companion object {
        private const val BASE_URL = "/api/course/board"
    }
}
