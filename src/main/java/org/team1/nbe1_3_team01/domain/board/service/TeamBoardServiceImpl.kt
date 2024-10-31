package org.team1.nbe1_3_team01.domain.board.service

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.board.constants.MessageContent
import org.team1.nbe1_3_team01.domain.board.controller.dto.BoardDeleteRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.TeamBoardListRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.TeamBoardRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.TeamBoardUpdateRequest
import org.team1.nbe1_3_team01.domain.board.entity.TeamBoard
import org.team1.nbe1_3_team01.domain.board.repository.TeamBoardRepository
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse
import org.team1.nbe1_3_team01.domain.board.service.response.TeamBoardResponse
import org.team1.nbe1_3_team01.domain.board.service.valid.CourseBoardValidator
import org.team1.nbe1_3_team01.domain.board.service.valid.TeamBoardValidator
import org.team1.nbe1_3_team01.domain.group.repository.TeamRepository
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import org.team1.nbe1_3_team01.global.util.Message
import org.team1.nbe1_3_team01.global.util.SecurityUtil

@Service
@Transactional
@RequiredArgsConstructor
open class TeamBoardServiceImpl (
    private val userRepository: UserRepository,
    private val teamRepository: TeamRepository,
    private val teamBoardRepository: TeamBoardRepository
) : TeamBoardService {

    override fun getTeamBoardListByType(request: TeamBoardListRequest): List<TeamBoardResponse> {
        val teamId = request.teamId
        val categoryId = request.categoryId
        val boardId = request.boardId

        return teamBoardRepository.findAllTeamBoardByType(
            teamId,
            categoryId,
            boardId
        )
    }

    override fun addTeamBoard(request: TeamBoardRequest): Message {
        val currentUser = currentUser
        val teamId = request.teamId
        val team = teamRepository.findById(teamId)
            .orElseThrow { AppException(ErrorCode.TEAM_NOT_FOUND) }

        val teamBoard = request.toEntity(currentUser, team)

        teamBoardRepository.save(teamBoard)
        return Message(MessageContent.getAddMessage(request.isNotice))
    }

    @Transactional(readOnly = true)
    override fun getTeamBoardDetailById(teamBoardId: Long): BoardDetailResponse {
        return teamBoardRepository.findTeamBoardDetailById(teamBoardId)
            .orElseThrow { AppException(ErrorCode.BOARD_NOT_FOUND) }
    }

    override fun updateTeamBoard(updateRequest: TeamBoardUpdateRequest): Message {
        val teamBoardId = updateRequest.teamBoardId
        val findTeamBoard = getBoardWithValidateUser(teamBoardId, false)
        findTeamBoard.updateTeamBoard(updateRequest)

        return Message(MessageContent.getUpdateMessage(false))
    }

    override fun deleteTeamBoardById(deleteRequest: BoardDeleteRequest): Message {
        val teamBoardId = deleteRequest.boardId
        val findTeamBoard = getBoardWithValidateUser(teamBoardId, false)

        teamBoardRepository.delete(findTeamBoard)
        return Message(MessageContent.getDeleteMessage(false))
    }

    private val currentUser: User
        get() {
            val currentUsername = SecurityUtil.getCurrentUsername()
            return userRepository.findByUsername(currentUsername)
                .orElseThrow { AppException(ErrorCode.USER_NOT_FOUND) }
        }

    private fun getBoardWithValidateUser(boardId: Long, isNotice: Boolean): TeamBoard {
        val findBoard = getTeamBoardById(boardId)!!
        val user = currentUser

        TeamBoardValidator.validateWriter(findBoard, user)
        CourseBoardValidator.validateAdminForNotice(user, isNotice)
        return findBoard
    }

    private fun getTeamBoardById(boardId: Long): TeamBoard? {
        return teamBoardRepository.findById(boardId)
            .orElseThrow { AppException(ErrorCode.BOARD_NOT_FOUND) }
    }

}
