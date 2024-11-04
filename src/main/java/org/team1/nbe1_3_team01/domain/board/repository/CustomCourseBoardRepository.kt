package org.team1.nbe1_3_team01.domain.board.repository

import org.team1.nbe1_3_team01.domain.board.constants.CommonBoardType
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse
import org.team1.nbe1_3_team01.domain.board.service.response.CourseBoardResponse
import org.team1.nbe1_3_team01.domain.board.service.response.PagingResponse
import java.util.*

interface CustomCourseBoardRepository {
    fun findAllCourseBoard(type: CommonBoardType, courseId: Long, boardId: Long?): List<CourseBoardResponse>

    fun findCourseBoardDetailById(courseId: Long): BoardDetailResponse?

    fun findPaginationInfo(courseId: Long, boardType: CommonBoardType): List<PagingResponse?>?
}
