package org.team1.nbe1_3_team01.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.board.constants.CommonBoardType;
import org.team1.nbe1_3_team01.domain.board.repository.CourseBoardRepository;
import org.team1.nbe1_3_team01.domain.board.service.response.CourseBoardResponse;
import org.team1.nbe1_3_team01.domain.board.service.response.MainCourseBoardListResponse;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.ErrorCode;
import org.team1.nbe1_3_team01.global.util.SecurityUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MainBoardServiceImpl implements MainBoardService {

    private final UserRepository userRepository;
    private final CourseBoardRepository courseBoardRepository;

    @Override
    public MainCourseBoardListResponse getCourseBoardListForMain() {
        User currentUser = getCurrentUser();
        Long courseId = currentUser.getCourse().getId();

        return getMainCourseBoardListResponse(courseId);
    }

    @Override
    public MainCourseBoardListResponse getMainCourseBoardForAdmin(Long courseId) {
        return getMainCourseBoardListResponse(courseId);
    }

    private User getCurrentUser() {
        String currentUsername = SecurityUtil.getCurrentUsername();
        return userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private MainCourseBoardListResponse getMainCourseBoardListResponse(Long courseId) {
        List<CourseBoardResponse> noticeBoards = courseBoardRepository.findAllCourseBoard(
                CommonBoardType.NOTICE,
                courseId,
                null
        );

        List<CourseBoardResponse> studyBoards = courseBoardRepository.findAllCourseBoard(
                CommonBoardType.STUDY,
                courseId,
                null
        );

        return MainCourseBoardListResponse.of(
                noticeBoards,
                studyBoards,
                courseId
        );
    }

}
