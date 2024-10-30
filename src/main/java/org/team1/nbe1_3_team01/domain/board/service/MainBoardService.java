package org.team1.nbe1_3_team01.domain.board.service;

import org.team1.nbe1_3_team01.domain.board.service.response.MainCourseBoardListResponse;

public interface MainBoardService {

    MainCourseBoardListResponse getCourseBoardListForMain();

    MainCourseBoardListResponse getMainCourseBoardForAdmin(Long courseId);
}
