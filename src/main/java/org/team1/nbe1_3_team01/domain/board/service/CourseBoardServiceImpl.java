package org.team1.nbe1_3_team01.domain.board.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.board.constants.CommonBoardType;
import org.team1.nbe1_3_team01.domain.board.constants.MessageContent;
import org.team1.nbe1_3_team01.domain.board.controller.dto.BoardDeleteRequest;
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardListRequest;
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardRequest;
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardUpdateRequest;
import org.team1.nbe1_3_team01.domain.board.entity.CourseBoard;
import org.team1.nbe1_3_team01.domain.board.repository.CourseBoardRepository;
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_3_team01.domain.board.service.response.CourseBoardResponse;
import org.team1.nbe1_3_team01.domain.board.service.response.PagingResponse;
import org.team1.nbe1_3_team01.domain.board.service.valid.CourseBoardValidator;
import org.team1.nbe1_3_team01.domain.user.entity.Course;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.domain.user.repository.CourseRepository;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.ErrorCode;
import org.team1.nbe1_3_team01.global.util.Message;
import org.team1.nbe1_3_team01.global.util.SecurityUtil;

import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CourseBoardServiceImpl implements CourseBoardService {

    private final CourseBoardRepository courseBoardRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CourseBoardResponse> getCourseBoardList(CourseBoardListRequest request) {
        CommonBoardType boardType = CommonBoardType.getType(request.isNotice());
        return courseBoardRepository.findAllCourseBoard(
                boardType,
                request.courseId(),
                request.boardId()
        );
    }

    @Override
    public Message addCourseBoard(CourseBoardRequest request) {
        User user = getCurrentUser();
        CourseBoardValidator.validateAdminForNotice(user, request.isNotice());
        Course course = courseRepository.findById(request.courseId()).orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_FOUND));

        log.info("course = {}", course);

        CourseBoard newBoard = request.toEntity(user, course);
        courseBoardRepository.save(newBoard);

        return new Message(MessageContent.getAddMessage(request.isNotice()));
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDetailResponse getCourseBoardDetailById(Long courseBoardId) {
        return courseBoardRepository.findCourseBoardDetailById(courseBoardId)
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));
    }

    @Override
    public Message updateCourseBoard(CourseBoardUpdateRequest request) {
        CourseBoard findBoard = getBoardWithValidateUser(
                request.courseBoardId(),
                request.isNotice()
        );

        findBoard.updateBoard(request);

        String updateMessage = MessageContent.getUpdateMessage(request.isNotice());
        return new Message(updateMessage);
    }

    @Override
    public Message deleteCourseBoardById(BoardDeleteRequest request) {
        CourseBoard findBoard = getBoardWithValidateUser(
                request.boardId(),
                request.isNotice()
        );

        courseBoardRepository.delete(findBoard);

        String deleteMessage = MessageContent.getDeleteMessage(request.isNotice());
        return new Message(deleteMessage);
    }

    @Override
    public List<PagingResponse> getPaginationInfo(CourseBoardListRequest request) {
        CommonBoardType boardType = CommonBoardType.getType(request.isNotice());
        return courseBoardRepository.findPaginationInfo(
                request.courseId(),
                boardType
        );
    }

    private User getCurrentUser() {
        String currentUsername = SecurityUtil.getCurrentUsername(); //id를 반환
        return userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private CourseBoard getBoardWithValidateUser(Long boardId, boolean isNotice) {
        CourseBoard findBoard = getCourseBoardById(boardId);
        User user = getCurrentUser();

        CourseBoardValidator.validateWriter(findBoard, user);
        CourseBoardValidator.validateAdminForNotice(user, isNotice);
        return findBoard;
    }

    private CourseBoard getCourseBoardById(Long boardId) {
        return courseBoardRepository.findById(boardId)
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));
    }
}
