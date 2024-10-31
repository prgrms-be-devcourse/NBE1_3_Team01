package org.team1.nbe1_3_team01.domain.user.controller.api

import jakarta.validation.Valid
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team1.nbe1_3_team01.domain.user.controller.request.UserDeleteRequest
import org.team1.nbe1_3_team01.domain.user.controller.request.UserSignUpRequest
import org.team1.nbe1_3_team01.domain.user.controller.request.UserUpdateRequest
import org.team1.nbe1_3_team01.domain.user.service.UserService
import org.team1.nbe1_3_team01.domain.user.service.response.UserAdminCheckResponse
import org.team1.nbe1_3_team01.domain.user.service.response.UserDetailsResponse
import org.team1.nbe1_3_team01.domain.user.service.response.UserIdResponse
import org.team1.nbe1_3_team01.global.auth.email.service.EmailService
import org.team1.nbe1_3_team01.global.util.Response

@RestController
@RequestMapping("/api/user")
class UserController(
    private val userService: UserService,
    private val emailService: EmailService
) {

    /**
     * 회원가입
     */
    @PostMapping("/sign-up")
    fun signUp(@RequestBody userSignUpRequest: @Valid UserSignUpRequest): ResponseEntity<Response<UserIdResponse>> {
        val userIdResponse = userService.signUp(userSignUpRequest)
        emailService.deleteByEmail(userSignUpRequest.email)
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(userIdResponse))
    }


    /**
     * 로그 아웃
     */
    @PostMapping("/logout")
    fun logout(): ResponseEntity<Void> {
        userService.logout()
        return ResponseEntity.noContent().build()
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping
    fun delete(@RequestBody userDeleteRequest: UserDeleteRequest): ResponseEntity<Unit> {
        userService.delete(userDeleteRequest)
        return ResponseEntity.noContent().build()
    }

    /**
     * 내 정보 조회하기
     */
    @GetMapping
    fun details(): ResponseEntity<Response<UserDetailsResponse>> {
        val userDetailsResponse: UserDetailsResponse = userService.currentUserDetails()
        return ResponseEntity.ok().body(
            Response.success(userDetailsResponse)
        )
    }

    /**
     * 회원 정보 수정
     */
    @PatchMapping
    fun update(@RequestBody userUpdateRequest: @Valid UserUpdateRequest): ResponseEntity<Response<UserIdResponse>> {
        val userIdResponse = userService.update(userUpdateRequest)
        return ResponseEntity.ok().body(Response.success(userIdResponse))
    }

    /**
     * 관리자 인지 체크
     */
    @GetMapping("/role")
    fun isAdmin(): ResponseEntity<Response<UserAdminCheckResponse>> {
        return ResponseEntity.ok().body(
            Response.success(userService.isAdmin())
        )
    }
}
