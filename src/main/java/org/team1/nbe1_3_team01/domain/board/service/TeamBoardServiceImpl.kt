package org.team1.nbe1_3_team01.domain.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.board.constants.MessageContent;
import org.team1.nbe1_3_team01.domain.board.controller.dto.BoardDeleteRequest;
import org.team1.nbe1_3_team01.domain.board.controller.dto.TeamBoardListRequest;
import org.team1.nbe1_3_team01.domain.board.controller.dto.TeamBoardRequest;
import org.team1.nbe1_3_team01.domain.board.controller.dto.TeamBoardUpdateRequest;
import org.team1.nbe1_3_team01.domain.board.entity.TeamBoard;
import org.team1.nbe1_3_team01.domain.board.repository.TeamBoardRepository;
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_3_team01.domain.board.service.response.TeamBoardResponse;
import org.team1.nbe1_3_team01.domain.board.service.valid.CourseBoardValidator;
import org.team1.nbe1_3_team01.domain.board.service.valid.TeamBoardValidator;
import org.team1.nbe1_3_team01.domain.group.entity.Team;
import org.team1.nbe1_3_team01.domain.group.repository.TeamRepository;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.ErrorCode;
import org.team1.nbe1_3_team01.global.util.Message;
import org.team1.nbe1_3_team01.global.util.SecurityUtil;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class TeamBoardServiceImpl implements TeamBoardService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final TeamBoardRepository teamBoardRepository;
    @Override
    public List<TeamBoardResponse> getTeamBoardListByType(TeamBoardListRequest request) {
        Long teamId = request.teamId();
        Long categoryId = request.categoryId();
        Long boardId = request.boardId();

        return teamBoardRepository.findAllTeamBoardByType(
                teamId,
                categoryId,
                boardId
        );
    }

    @Override
    public Message addTeamBoard(TeamBoardRequest request) {
        User currentUser = getCurrentUser();
        Long teamId = request.teamId();
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new AppException(ErrorCode.TEAM_NOT_FOUND));

        TeamBoard teamBoard = request.toEntity(currentUser, team);

        teamBoardRepository.save(teamBoard);
        return new Message(MessageContent.getAddMessage(request.isNotice()));
    }

    @Override
    @Transactional(readOnly = true)
    public BoardDetailResponse getTeamBoardDetailById(Long teamBoardId) {
        return teamBoardRepository.findTeamBoardDetailById(teamBoardId)
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));
    }


    @Override
    public Message updateTeamBoard(TeamBoardUpdateRequest updateRequest) {
        Long teamBoardId = updateRequest.teamBoardId();
        TeamBoard findTeamBoard = getBoardWithValidateUser(
                teamBoardId,
                false
        );
        findTeamBoard.updateTeamBoard(updateRequest);

        String updateMessage = MessageContent.getUpdateMessage(false);
        return new Message(updateMessage);
    }
    @Override
    public Message deleteTeamBoardById(BoardDeleteRequest deleteRequest) {
        Long teamBoardId = deleteRequest.boardId();
        TeamBoard findTeamBoard = getBoardWithValidateUser(
                teamBoardId,
                false
        );

        teamBoardRepository.delete(findTeamBoard);
        String deleteMessage = MessageContent.getDeleteMessage(false);
        return new Message(deleteMessage);
    }

    private User getCurrentUser() {
        String currentUsername = SecurityUtil.getCurrentUsername();
        return userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    private TeamBoard getBoardWithValidateUser(Long boardId, boolean isNotice) {
        TeamBoard findBoard = getTeamBoardById(boardId);
        User user = getCurrentUser();

        TeamBoardValidator.validateWriter(findBoard, user);
        CourseBoardValidator.validateAdminForNotice(user, isNotice);
        return findBoard;
    }

    private TeamBoard getTeamBoardById(Long boardId) {
        return teamBoardRepository.findById(boardId)
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));
    }
}
