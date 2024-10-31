package org.team1.nbe1_3_team01.domain.board.repository;

import org.team1.nbe1_3_team01.domain.board.service.response.CategoryResponse;

import java.util.List;

public interface CustomCategoryRepository {


    List<CategoryResponse> getAllCategoryByTeamId(Long teamId);
}
