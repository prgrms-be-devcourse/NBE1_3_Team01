package org.team1.nbe1_3_team01.domain.board.controller.dto

class TeamBoardListRequest(
    var teamId: Long,
    var categoryId: Long,
    var boardId: Long? = null
) {


}
