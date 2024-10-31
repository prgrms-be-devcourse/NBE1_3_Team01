package org.team1.nbe1_3_team01.domain.board.controller.dto

data class CourseBoardListRequest(
    var courseId: Long,
    val boardId: Long? = null,
    var isNotice: String
) {

}
