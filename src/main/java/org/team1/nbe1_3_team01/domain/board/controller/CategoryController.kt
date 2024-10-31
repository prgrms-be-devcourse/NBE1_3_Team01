package org.team1.nbe1_3_team01.domain.board.controller

import jakarta.validation.Valid
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team1.nbe1_3_team01.domain.board.controller.dto.CategoryDeleteRequest
import org.team1.nbe1_3_team01.domain.board.controller.dto.CategoryRequest
import org.team1.nbe1_3_team01.domain.board.service.CategoryService
import org.team1.nbe1_3_team01.domain.board.service.response.CategoryResponse
import org.team1.nbe1_3_team01.global.util.Message
import org.team1.nbe1_3_team01.global.util.Response

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
class CategoryController(
    private val categoryService: CategoryService
) {
    /**
     * 카테고리 소속에 맞는 카테고리 가져오기
     * (이 부분은 나중에 한번 확인하고 수정 필요)
     * @return
     */
    @GetMapping
    fun getCategories(
        @RequestParam teamId: Long
    ): ResponseEntity<Response<List<CategoryResponse>>> {
        val categoryList = categoryService.getAllCategoryByBelongings(teamId)
        return ResponseEntity.ok()
            .body(Response.success(categoryList))
    }

    /**
     * 새로운 카테고리 생성
     * @param categoryRequest
     * @return
     */
    @PostMapping
    fun addCategory(
        @RequestBody categoryRequest: @Valid CategoryRequest
    ): ResponseEntity<Response<Message>> {
        val message = categoryService.addCategory(categoryRequest)
        return ResponseEntity.ok()
            .body(Response.success(message))
    }

    @DeleteMapping
    fun deleteCategory(
        @RequestBody request: CategoryDeleteRequest
    ): ResponseEntity<Response<Message>> {
        val message = categoryService.deleteCategory(request)
        return ResponseEntity.ok()
            .body(Response.success(message))
    }
}
