package org.team1.nbe1_3_team01.domain.board.service

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.board.constants.CommonBoardType
import org.team1.nbe1_3_team01.domain.board.repository.CourseBoardRepository
import org.team1.nbe1_3_team01.domain.board.service.response.MainCourseBoardListResponse
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import org.team1.nbe1_3_team01.global.util.SecurityUtil

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
open class MainBoardServiceImpl(
    private val userRepository: UserRepository,
    private val courseBoardRepository: CourseBoardRepository
) : MainBoardService {


    override fun courseBoardListForMain(): MainCourseBoardListResponse {
        val courseId = currentUser().course?.id!!
        return getMainCourseBoardListResponse(courseId)
    }

    override fun getMainCourseBoardForAdmin(courseId: Long): MainCourseBoardListResponse {
        return getMainCourseBoardListResponse(courseId)
    }



    private fun currentUser(): User {
        val currentUsername = SecurityUtil.getCurrentUsername()
        return userRepository.findByUsername(currentUsername)
            ?: throw AppException(ErrorCode.USER_NOT_FOUND)
    }

    private fun getMainCourseBoardListResponse(courseId: Long): MainCourseBoardListResponse {
        val noticeBoards = courseBoardRepository.findAllCourseBoard(
            CommonBoardType.NOTICE,
            courseId,
            null
        )

        val studyBoards = courseBoardRepository.findAllCourseBoard(
            CommonBoardType.STUDY,
            courseId,
            null
        )

        return MainCourseBoardListResponse.of(
            noticeBoards,
            studyBoards,
            courseId
        )
    }
}
