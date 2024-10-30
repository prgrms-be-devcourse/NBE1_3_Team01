package org.team1.nbe1_3_team01.domain.board.comment.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.board.comment.controller.dto.CommentRequest;
import org.team1.nbe1_3_team01.domain.board.comment.repository.CommentRepository;
import org.team1.nbe1_3_team01.domain.board.comment.service.response.CommentResponse;
import org.team1.nbe1_3_team01.domain.board.comment.service.valid.CommentValidator;
import org.team1.nbe1_3_team01.domain.board.entity.TeamBoard;
import org.team1.nbe1_3_team01.domain.board.entity.Comment;
import org.team1.nbe1_3_team01.domain.board.repository.TeamBoardRepository;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_3_team01.global.util.Message;
import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.ErrorCode;
import org.team1.nbe1_3_team01.global.util.SecurityUtil;

import java.util.List;

import static org.team1.nbe1_3_team01.domain.board.constants.MessageContent.*;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TeamBoardRepository teamBoardRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getReviewsByPage(Long boardId, Long lastCommentId) {
        return commentRepository.getCommentsByBoardId(boardId, lastCommentId);
    }

    @Override
    public Message deleteById(Long id) {
        User user = getUser();
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND));

        CommentValidator.validateCommenter(comment, user);
        commentRepository.delete(comment);
        String deleteCommentMessage = DELETE_COMMENT_COMPLETED.getMessage();
        return new Message(deleteCommentMessage);
    }

    @Override
    public Message addComment(CommentRequest commentRequest) {
        User currentUser = getUser();

        Long boardId = commentRequest.teamBoardId();
        TeamBoard findBoard = teamBoardRepository.findById(boardId)
                .orElseThrow(() -> new AppException(ErrorCode.BOARD_NOT_FOUND));

        Comment comment = commentRequest.toEntity(currentUser, findBoard);
        commentRepository.save(comment);

        String addCommentMessage = ADD_BOARD_COMPLETED.getMessage();
        return new Message(addCommentMessage);
    }

    private User getUser() {
        String currentUsername = SecurityUtil.getCurrentUsername();
        return userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }
}
