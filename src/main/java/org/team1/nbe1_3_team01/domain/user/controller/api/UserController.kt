package org.team1.nbe1_3_team01.domain.user.controller.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.team1.nbe1_3_team01.domain.user.controller.request.UserDeleteRequest;
import org.team1.nbe1_3_team01.domain.user.controller.request.UserSignUpRequest;
import org.team1.nbe1_3_team01.domain.user.controller.request.UserUpdateRequest;
import org.team1.nbe1_3_team01.domain.user.service.UserService;
import org.team1.nbe1_3_team01.domain.user.service.response.UserAdminCheckResponse;
import org.team1.nbe1_3_team01.domain.user.service.response.UserDetailsResponse;
import org.team1.nbe1_3_team01.domain.user.service.response.UserIdResponse;
import org.team1.nbe1_3_team01.global.auth.email.service.EmailService;
import org.team1.nbe1_3_team01.global.util.Response;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    private final UserService userService;
    private final EmailService emailService;

    /**
     * 회원가입
     */
    @PostMapping("/sign-up")
    public ResponseEntity<Response<UserIdResponse>> signUp(@Valid @RequestBody UserSignUpRequest userSignUpRequest) {
        UserIdResponse userIdResponse = userService.signUp(userSignUpRequest);
        emailService.deleteByEmail(userSignUpRequest.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(Response.success(userIdResponse));
    }


    /**
     * 로그 아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        userService.logout();
        return ResponseEntity.noContent().build();
    }

    /**
     * 회원 탈퇴
     */
    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody UserDeleteRequest userDeleteRequest){
        userService.delete(userDeleteRequest);
        return ResponseEntity.noContent().build();
    }

    /**
     * 내 정보 조회하기
     */
    @GetMapping
    public ResponseEntity<Response<UserDetailsResponse>> getDetails(){
        UserDetailsResponse userDetailsResponse = userService.getCurrentUserDetails();
        return ResponseEntity.ok().body(Response.success(userDetailsResponse));
    }

    /**
     * 회원 정보 수정
     */
    @PatchMapping
    public ResponseEntity<Response<UserIdResponse>> update(@Valid @RequestBody UserUpdateRequest userUpdateRequest){
        UserIdResponse userIdResponse = userService.update(userUpdateRequest);
        return ResponseEntity.ok().body(Response.success(userIdResponse));
    }

    /**
     * 관리자 인지 체크
     */
    @GetMapping("/role")
    public ResponseEntity<Response<UserAdminCheckResponse>> isAdmin(){
        return ResponseEntity.ok().body(Response.success(userService.isAdmin()));
    }
}
