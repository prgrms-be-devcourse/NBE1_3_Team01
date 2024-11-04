package org.team1.nbe1_3_team01.domain.board.service

import org.team1.nbe1_3_team01.domain.board.service.response.MainCourseBoardListResponse

interface MainBoardService {

    fun courseBoardListForMain(): MainCourseBoardListResponse

    fun getMainCourseBoardForAdmin(courseId: Long): MainCourseBoardListResponse
}
