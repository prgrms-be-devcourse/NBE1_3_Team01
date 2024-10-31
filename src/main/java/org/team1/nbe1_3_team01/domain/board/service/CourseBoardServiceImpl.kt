package org.team1.nbe1_3_team01.domain.board.service

import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.board.constants.CommonBoardType
import org.team1.nbe1_3_team01.domain.board.constants.MessageContent
import org.team1.nbe1_3_team01.domain.board.controller.dto.BoardDeleteRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardListRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.CourseBoardUpdateRequest
import org.team1.nbe1_3_team01.domain.board.entity.CourseBoard
import org.team1.nbe1_3_team01.domain.board.repository.CourseBoardRepository
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse
import org.team1.nbe1_3_team01.domain.board.service.response.CourseBoardResponse
import org.team1.nbe1_3_team01.domain.board.service.response.PagingResponse
import org.team1.nbe1_3_team01.domain.board.service.valid.CourseBoardValidator
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.CourseRepository
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import org.team1.nbe1_3_team01.global.util.Message
import org.team1.nbe1_3_team01.global.util.SecurityUtil

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
class CourseBoardServiceImpl(
    private val courseBoardRepository: CourseBoardRepository,
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository
) : CourseBoardService {

    @Transactional(readOnly = true)
    override fun getCourseBoardList(request: CourseBoardListRequest): List<CourseBoardResponse> {
        val boardType = CommonBoardType.getType(request.isNotice)
        return courseBoardRepository.findAllCourseBoard(
            boardType,
            request.courseId,
            request.boardId
        )
    }

    override fun addCourseBoard(request: CourseBoardRequest): Message {
        val user = currentUser
        CourseBoardValidator.validateAdminForNotice(user, request.isNotice)
        val course =
            courseRepository.findById(request.courseId)
                .orElseThrow { AppException(ErrorCode.COURSE_NOT_FOUND) }

        //로그 출력
//        log.info("course = {}", course)

        val newBoard = request.toEntity(user, course)
        courseBoardRepository.save(newBoard)

        return Message(MessageContent.getAddMessage(request.isNotice))
    }

    @Transactional(readOnly = true)
    override fun getCourseBoardDetailById(courseBoardId: Long): BoardDetailResponse {
        return courseBoardRepository.findCourseBoardDetailById(courseBoardId)
            .orElseThrow { AppException(ErrorCode.BOARD_NOT_FOUND) }
    }

    override fun updateCourseBoard(request: CourseBoardUpdateRequest): Message {
        val findBoard = getBoardWithValidateUser(
            request.courseBoardId,
            request.isNotice
        )

        findBoard.updateBoard(request)

        val updateMessage = MessageContent.getUpdateMessage(request.isNotice)
        return Message(updateMessage)
    }

    override fun deleteCourseBoardById(request: BoardDeleteRequest): Message {
        val findBoard = getBoardWithValidateUser(
            request.boardId,
            request.isNotice
        )

        courseBoardRepository.delete(findBoard)

        val deleteMessage = MessageContent.getDeleteMessage(request.isNotice)
        return Message(deleteMessage)
    }

    override fun getPaginationInfo(request: CourseBoardListRequest): List<PagingResponse?>? {
        val boardType = CommonBoardType.getType(request.isNotice)
        return courseBoardRepository.findPaginationInfo(
            request.courseId,
            boardType
        )
    }

    private val currentUser: User
        get() {
            val currentUsername = SecurityUtil.getCurrentUsername() //id를 반환
            return userRepository.findByUsername(currentUsername)
                .orElseThrow { AppException(ErrorCode.USER_NOT_FOUND) }
        }

    private fun getBoardWithValidateUser(boardId: Long, isNotice: Boolean): CourseBoard {
        val findBoard = getCourseBoardById(boardId)
        val user = currentUser

        CourseBoardValidator.validateWriter(findBoard, user)
        CourseBoardValidator.validateAdminForNotice(user, isNotice)
        return findBoard
    }

    private fun getCourseBoardById(boardId: Long): CourseBoard {
        return courseBoardRepository.findById(boardId)
            .orElseThrow { AppException(ErrorCode.BOARD_NOT_FOUND) }
    }
}
