package org.team1.nbe1_3_team01.domain.board.service;

import org.team1.nbe1_3_team01.domain.board.controller.dto.CategoryDeleteRequest;
import org.team1.nbe1_3_team01.domain.board.controller.dto.CategoryRequest;
import org.team1.nbe1_3_team01.domain.board.service.response.CategoryResponse;
import org.team1.nbe1_3_team01.global.util.Message;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getAllCategoryByBelongings(Long teamId);

    Message addCategory(CategoryRequest categoryRequest);

    Message deleteCategory(CategoryDeleteRequest id);
}
