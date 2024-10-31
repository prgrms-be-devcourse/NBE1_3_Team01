package org.team1.nbe1_3_team01.domain.board.service;

import org.team1.nbe1_3_team01.domain.board.controller.dto.*;
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_3_team01.domain.board.service.response.TeamBoardResponse;
import org.team1.nbe1_3_team01.global.util.Message;

import java.util.List;

public interface TeamBoardService {
    List<TeamBoardResponse> getTeamBoardListByType(TeamBoardListRequest request);

    Message addTeamBoard(TeamBoardRequest request);

    BoardDetailResponse getTeamBoardDetailById(Long teamBoardId);

    Message updateTeamBoard(TeamBoardUpdateRequest updateRequest);

    Message deleteTeamBoardById(BoardDeleteRequest deleteRequest);
}
