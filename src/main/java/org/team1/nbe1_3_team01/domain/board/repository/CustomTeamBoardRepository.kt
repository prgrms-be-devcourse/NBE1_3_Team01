package org.team1.nbe1_3_team01.domain.board.repository

import org.team1.nbe1_3_team01.domain.board.constants.CommonBoardType
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse
import org.team1.nbe1_3_team01.domain.board.service.response.PagingResponse
import org.team1.nbe1_3_team01.domain.board.service.response.TeamBoardResponse
import java.util.*

interface CustomTeamBoardRepository {
    fun findAllTeamBoardByType(teamId: Long, categoryId: Long, boardId: Long?): List<TeamBoardResponse>

    fun findTeamBoardDetailById(teamBoardId: Long?): Optional<BoardDetailResponse>

    fun findPaginationInfo(teamId: Long, categoryId: Long): List<PagingResponse?>?
}
