package org.team1.nbe1_3_team01.domain.board.repository;

import org.team1.nbe1_3_team01.domain.board.constants.CommonBoardType;
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_3_team01.domain.board.service.response.CourseBoardResponse;
import org.team1.nbe1_3_team01.domain.board.service.response.PagingResponse;

import java.util.List;
import java.util.Optional;

public interface CustomCourseBoardRepository {

    List<CourseBoardResponse> findAllCourseBoard(CommonBoardType type, Long courseId, Long boardId);

    Optional<BoardDetailResponse> findCourseBoardDetailById(Long id);

    List<PagingResponse> findPaginationInfo(Long courseId, CommonBoardType boardType);

}
