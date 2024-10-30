package org.team1.nbe1_3_team01.domain.board.repository;

import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_3_team01.domain.board.service.response.TeamBoardResponse;

import java.util.List;
import java.util.Optional;

public interface CustomTeamBoardRepository {
    List<TeamBoardResponse> findAllTeamBoardByType(Long teamId, Long categoryId, Long boardId);

    Optional<BoardDetailResponse> findTeamBoardDetailById(Long teamBoardId);
}
