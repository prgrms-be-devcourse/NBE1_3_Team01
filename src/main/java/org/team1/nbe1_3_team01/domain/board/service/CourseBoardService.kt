package org.team1.nbe1_3_team01.domain.board.service;

import org.team1.nbe1_3_team01.domain.board.controller.dto.BoardDeleteRequest;
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardListRequest;
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardRequest;
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardUpdateRequest;
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_3_team01.domain.board.service.response.CourseBoardResponse;
import org.team1.nbe1_3_team01.domain.board.service.response.PagingResponse;
import org.team1.nbe1_3_team01.global.util.Message;

import java.util.List;

public interface CourseBoardService {
    List<CourseBoardResponse> getCourseBoardList(CourseBoardListRequest request);

    Message addCourseBoard(CourseBoardRequest boardRequest);

    BoardDetailResponse getCourseBoardDetailById(Long id);

    Message updateCourseBoard(CourseBoardUpdateRequest request);

    Message deleteCourseBoardById(BoardDeleteRequest deleteRequest);

    List<PagingResponse> getPaginationInfo(CourseBoardListRequest request);
}
