package org.team1.nbe1_3_team01.domain.board.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.team1.nbe1_3_team01.domain.board.service.response.BoardDetailResponse;
import org.team1.nbe1_3_team01.domain.board.service.response.TeamBoardResponse;
import org.team1.nbe1_3_team01.domain.user.entity.Role;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.global.util.SecurityUtil;

import static org.team1.nbe1_3_team01.domain.board.entity.QTeamBoard.teamBoard;


import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.team1.nbe1_3_team01.domain.board.entity.QCategory.category;
import static org.team1.nbe1_3_team01.domain.board.entity.QComment.comment;
import static org.team1.nbe1_3_team01.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class CustomTeamBoardRepositoryImpl implements CustomTeamBoardRepository {

    private final JPAQueryFactory queryFactory;
    private static final int PAGE_SIZE = 10;

    @Override
    public List<TeamBoardResponse> findAllTeamBoardByType(
            Long teamId,
            Long categoryId,
            Long boardId
    ) {
        JPAQuery<Tuple> teamBoardQuery = buildTeamBoardListQuery(teamId, categoryId);
        setPagingStart(teamBoardQuery, boardId);

        List<Tuple> tuples = teamBoardQuery
                .limit(PAGE_SIZE)
                .groupBy(teamBoard.id, teamBoard.title, user.name, category.name, teamBoard.createdAt)
                .orderBy(teamBoard.createdAt.desc(), teamBoard.id.desc())
                .fetch();

        return tuples.stream()
                .map(this::convertToTeamBoardResponse)
                .toList();
    }

    @Override
    public Optional<BoardDetailResponse> findTeamBoardDetailById(Long teamBoardId) {
        Tuple tuple = queryFactory.select(
                        teamBoard.id,
                        teamBoard.title,
                        teamBoard.readCount,
                        teamBoard.content,
                        user.name,
                        teamBoard.createdAt,
                        teamBoard.user.id
                )
                .from(teamBoard)
                .innerJoin(user).on(teamBoard.user.eq(user))
                .where(teamBoard.id.eq(teamBoardId))
                .fetchOne();

        if(tuple == null) {
            return Optional.empty();
        }

        User currentUser = findCurrentUser();
        BoardDetailResponse boardDetailResponse = convertToBoardDetailResponse(tuple, currentUser);

        return Optional.of(boardDetailResponse);
    }

    private JPAQuery<Tuple> buildTeamBoardListQuery(Long teamId, Long categoryId) {
        return queryFactory
                .select(
                        teamBoard.id,
                        teamBoard.title,
                        user.name,
                        category.name,
                        teamBoard.createdAt,
                        comment.count())
                .from(teamBoard)
                .innerJoin(user).on(teamBoard.user.eq(user))
                .innerJoin(category).on(category.id.eq(categoryId))
                .leftJoin(comment).on(comment.board.eq(teamBoard))
                .where(teamBoard.team.id.eq(teamId)
                        .and(teamBoard.categoryId.eq(categoryId)));
    }

    private void setPagingStart(JPAQuery<Tuple> commonQuery, Long boardId) {
        if (boardId != null) {
            commonQuery.where(teamBoard.id.lt(boardId));
        }
    }

    private User findCurrentUser() {
        String currentUsername = SecurityUtil.getCurrentUsername();
        return queryFactory.selectFrom(user).where(user.username.eq(currentUsername)).fetchOne();
    }

    private TeamBoardResponse convertToTeamBoardResponse(Tuple tuple) {
        return TeamBoardResponse.builder()
                .id(tuple.get(teamBoard.id))
                .categoryName(tuple.get(category.name))
                .title(tuple.get(teamBoard.title))
                .writer(tuple.get(user.name))
                .createdAt(tuple.get(teamBoard.createdAt))
                .commentCount(tuple.get(comment.count()))
                .build();
    }

    private BoardDetailResponse convertToBoardDetailResponse(Tuple tuple, User currentUser) {
        return BoardDetailResponse.builder()
                .id(tuple.get(teamBoard.id))
                .title(tuple.get(teamBoard.title))
                .readCount(Optional.ofNullable(tuple.get(teamBoard.readCount)).orElse(0L))
                .content(tuple.get(teamBoard.content))
                .writer(tuple.get(user.name))
                .createdAt(tuple.get(teamBoard.createdAt))
                .isAdmin(Objects.equals(currentUser.getRole(), Role.ADMIN))
                .isMine(Objects.equals(tuple.get(teamBoard.user.id), currentUser.getId()))
                .build();
    }
}
