package org.team1.nbe1_3_team01.domain.board.service

import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.board.constants.MessageContent
import org.team1.nbe1_3_team01.domain.board.controller.dto.CategoryDeleteRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.CategoryRequest
import org.team1.nbe1_3_team01.domain.board.repository.CategoryRepository
import org.team1.nbe1_3_team01.domain.board.service.response.CategoryResponse
import org.team1.nbe1_3_team01.domain.board.service.valid.CategoryValidator
import org.team1.nbe1_3_team01.domain.group.repository.BelongingRepository
import org.team1.nbe1_3_team01.domain.group.repository.TeamRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import org.team1.nbe1_3_team01.global.util.Message
import org.team1.nbe1_3_team01.global.util.SecurityUtil

@Service
@Transactional
@RequiredArgsConstructor
class CategoryServiceImpl(
    private val categoryRepository: CategoryRepository,
    private val teamRepository: TeamRepository,
    private val belongingRepository: BelongingRepository,

) : CategoryService {

    @Transactional(readOnly = true)
    override fun getAllCategoryByBelongings(teamId: Long): List<CategoryResponse> {
        return categoryRepository.getAllCategoryByTeamId(teamId)
    }

    override fun addCategory(categoryRequest: CategoryRequest): Message {
        val teamId = categoryRequest.teamId
        validTeamLeader(teamId)

        val team = teamRepository.findById(teamId)
            .orElseThrow { AppException(ErrorCode.TEAM_NOT_FOUND) }

        val newCategory = categoryRequest.toEntity(team)
        categoryRepository.save(newCategory)

        val addMessage = MessageContent.ADD_CATEGORY_COMPLETED.message
        return Message(addMessage)
    }

    override fun deleteCategory(request: CategoryDeleteRequest): Message {
        //요청한 사용자가 팀장인지?
        val teamId = request.teamId
        val categoryId = request.categoryId

        validTeamLeader(teamId)
        val result = categoryRepository.deleteByIdAndTeam_Id(categoryId, teamId)

        if (result == 0) {
            throw AppException(ErrorCode.CATEGORY_NOT_DELETED)
        }

        val deleteMessage = MessageContent.DELETE_CATEGORY_COMPLETED.message
        return Message(deleteMessage)
    }

    /**
     * 요청한 사용자가 팀장인지 검증하고
     * 소속 엔티티를 반환하는 코드
     * @param teamId
     * @return
     */
    private fun validTeamLeader(teamId: Long) {
        val currentUsername = SecurityUtil.getCurrentUsername()
        val belonging = belongingRepository.findByTeam_IdAndUser_Username(
            teamId,
            currentUsername
        ).orElseThrow { AppException(ErrorCode.BELONGING_NOT_FOUND) }

        CategoryValidator.validateTeamLeader(belonging)
    }
}
