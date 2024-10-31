package org.team1.nbe1_3_team01.domain.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_3_team01.domain.board.controller.dto.CategoryDeleteRequest;
import org.team1.nbe1_3_team01.domain.board.controller.dto.CategoryRequest;
import org.team1.nbe1_3_team01.domain.board.service.CategoryService;
import org.team1.nbe1_3_team01.domain.board.service.response.CategoryResponse;
import org.team1.nbe1_3_team01.global.util.Message;
import org.team1.nbe1_3_team01.global.util.Response;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * 카테고리 소속에 맞는 카테고리 가져오기
     * (이 부분은 나중에 한번 확인하고 수정 필요)
     * @return
     */
    @GetMapping
    public ResponseEntity<Response<List<CategoryResponse>>> getCategories(
            @RequestParam Long teamId
    ) {
        List<CategoryResponse> categoryList = categoryService.getAllCategoryByBelongings(teamId);
        return ResponseEntity.ok()
                .body(Response.success(categoryList));
    }

    /**
     * 새로운 카테고리 생성
     * @param categoryRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<Response<Message>> addCategory(
            @RequestBody @Valid CategoryRequest categoryRequest
    ) {
        Message message = categoryService.addCategory(categoryRequest);
        return ResponseEntity.ok()
                .body(Response.success(message));
    }

    @DeleteMapping
    public ResponseEntity<Response<Message>> deleteCategory(
            @RequestBody CategoryDeleteRequest request
            ) {
        Message message = categoryService.deleteCategory(request);
        return ResponseEntity.ok()
                .body(Response.success(message));
    }
}
