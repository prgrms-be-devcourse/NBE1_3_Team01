package org.team1.nbe1_3_team01.domain.board.service

import org.team1.nbe1_3_team01.domain.board.controller.dto.BoardDeleteRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.TeamBoardListRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.TeamBoardRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.TeamBoardUpdateRequest
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse
import org.team1.nbe1_3_team01.domain.board.service.response.PagingResponse
import org.team1.nbe1_3_team01.domain.board.service.response.TeamBoardResponse
import org.team1.nbe1_3_team01.global.util.Message

interface TeamBoardService {
    fun getTeamBoardListByType(request: TeamBoardListRequest): List<TeamBoardResponse>

    fun addTeamBoard(request: TeamBoardRequest): Message

    fun getTeamBoardDetailById(teamBoardId: Long): BoardDetailResponse

    fun updateTeamBoard(updateRequest: TeamBoardUpdateRequest): Message

    fun deleteTeamBoardById(deleteRequest: BoardDeleteRequest): Message

    fun getPaginationInfo(request: TeamBoardListRequest): List<PagingResponse>
}
