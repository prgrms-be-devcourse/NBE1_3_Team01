package org.team1.nbe1_3_team01.domain.board.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.team1.nbe1_3_team01.domain.board.service.response.CategoryResponse;

import java.util.List;
import java.util.Optional;

import static org.team1.nbe1_3_team01.domain.board.entity.QCategory.category;
import static org.team1.nbe1_3_team01.domain.board.entity.QTeamBoard.teamBoard;

@RequiredArgsConstructor
public class CustomCategoryRepositoryImpl implements CustomCategoryRepository {

    private final JPAQueryFactory queryFactory;
    @Override
    public List<CategoryResponse> getAllCategoryByTeamId(Long teamId) {
        List<Tuple> fetchedList = queryFactory.select(
                        category.id,
                        category.name,
                        teamBoard.count().as("boardCount")
                )
                .from(category)
                .leftJoin(teamBoard).on(teamBoard.categoryId.eq(category.id))
                .where(category.team.id.eq(teamId))
                .groupBy(category.id, category.name)
                .orderBy(category.id.asc())
                .fetch();

        return convertToResponse(fetchedList);
    }

    private List<CategoryResponse> convertToResponse(List<Tuple> fetchedList) {
        return fetchedList.stream().map(tuple -> CategoryResponse.of(
                tuple.get(category.id),
                tuple.get(category.name),
                Optional.ofNullable(tuple.get(teamBoard.count())).orElse(0L)
        )).toList();
    }
}
