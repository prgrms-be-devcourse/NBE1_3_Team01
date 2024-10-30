package org.team1.nbe1_3_team01.domain.board.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.team1.nbe1_3_team01.domain.board.service.MainBoardService;
import org.team1.nbe1_3_team01.domain.board.service.response.MainCourseBoardListResponse;
import org.team1.nbe1_3_team01.global.auth.interceptor.GroupAuth;
import org.team1.nbe1_3_team01.global.util.Response;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/main/board")
public class MainBoardController {

    private final MainBoardService mainBoardService;

    @GetMapping
    public ResponseEntity<Response<MainCourseBoardListResponse>> getCourseBoardListForMain(

    ) {
        MainCourseBoardListResponse courseBoardListForMain = mainBoardService.getCourseBoardListForMain();

        return ResponseEntity.ok()
                .body(Response.success(courseBoardListForMain));
    }

    @GetMapping("/admin")
    @GroupAuth(role = GroupAuth.Role.ADMIN)
    public ResponseEntity<Response<MainCourseBoardListResponse>> getCourseBoardListForMain(
            Long courseId
    ) {
        MainCourseBoardListResponse courseBoardListForMain = mainBoardService.getMainCourseBoardForAdmin(courseId);
        return ResponseEntity.ok()
                .body(Response.success(courseBoardListForMain));
    }
}
