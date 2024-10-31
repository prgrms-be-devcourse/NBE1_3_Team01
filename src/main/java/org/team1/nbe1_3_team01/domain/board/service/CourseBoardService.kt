package org.team1.nbe1_3_team01.domain.board.service

import org.team1.nbe1_3_team01.domain.board.controller.dto.BoardDeleteRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardListRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardUpdateRequest
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse
import org.team1.nbe1_3_team01.domain.board.service.response.CourseBoardResponse
import org.team1.nbe1_3_team01.domain.board.service.response.PagingResponse
import org.team1.nbe1_3_team01.global.util.Message

interface CourseBoardService {
    fun getCourseBoardList(request: CourseBoardListRequest): List<CourseBoardResponse>

    fun addCourseBoard(request: CourseBoardRequest): Message

    fun getCourseBoardDetailById(courseBoardId: Long): BoardDetailResponse?

    fun updateCourseBoard(request: CourseBoardUpdateRequest): Message

    fun deleteCourseBoardById(request: BoardDeleteRequest): Message

    fun getPaginationInfo(request: CourseBoardListRequest): List<PagingResponse?>?
}
