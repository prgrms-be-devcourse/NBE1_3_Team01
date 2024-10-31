package org.team1.nbe1_3_team01.domain.board.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.team1.nbe1_3_team01.domain.board.entity.TeamBoard

@Repository
interface TeamBoardRepository : CrudRepository<TeamBoard, Long?>, CustomTeamBoardRepository
