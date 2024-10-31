package org.team1.nbe1_3_team01.domain.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.IntegrationTestSupport;
import org.team1.nbe1_3_team01.domain.user.controller.request.UserSignUpRequest;
import org.team1.nbe1_3_team01.domain.user.controller.request.UserUpdateRequest;
import org.team1.nbe1_3_team01.domain.user.entity.Course;
import org.team1.nbe1_3_team01.domain.user.entity.Role;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.domain.user.repository.CourseRepository;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_3_team01.domain.user.service.response.UserDetailsResponse;
import org.team1.nbe1_3_team01.domain.user.service.response.UserIdResponse;
import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.ErrorCode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
class UserServiceTest extends IntegrationTestSupport {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    private UserService userService;

    private final String username = "user";
    private final String password = "1234abcd";
    private final String email = "user@gmail.com";
    private final String name = "김철수";
    private final Course course = Course.builder()
            .name("데브코스 1기 백엔드")
            .build();


    /**
     * 모든 테스트 이전에 회원가입 실행
     */
    @BeforeEach
    void setUp() {
        courseRepository.save(course);
        UserSignUpRequest request = new UserSignUpRequest(
                username,
                password,
                email,
                name,
                course.getId());
        userService.signUp(request);

    }

    @Test
    void 회원가입_성공() {
        // Then
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자 없음"));
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(passwordEncoder.matches(password, user.getPassword())).isTrue();
        assertThat(user.getEmail()).isEqualTo(email);
        assertThat(user.getName()).isEqualTo(name);
        assertThat(user.getRole()).isEqualTo(Role.USER);
        assertThat(user.getCourse()).isEqualTo(course);
    }

    @Test
    void 회원가입_실패_아이디_중복() {
        //when
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest(
                "user",
                "abcd1234",
                "user@naver.com",
                "김영희",
                 course.getId());
        // then
        assertThat(assertThrows(AppException.class, () -> userService.signUp(userSignUpRequest)).getErrorCode())
                .isEqualTo(ErrorCode.USERNAME_ALREADY_EXISTS);

    }

    @Test
    void 회원가입_실패_이메일_중복() {
        //when
        UserSignUpRequest userSignUpRequest = new UserSignUpRequest(
                "usre",
                "1234",
                "user@gmail.com",
                "김영희",
                course.getId());

        // then
        assertThat(assertThrows(AppException.class, () -> userService.signUp(userSignUpRequest)).getErrorCode())
                .isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    @Test
    @WithMockUser(username = "user")
    void 회원수정_이름_변경() {
        //given
        UserUpdateRequest userUpdateRequest = new UserUpdateRequest(
                "김미희",
                null
        );
        //when
        UserIdResponse userIdResponse = userService.update(userUpdateRequest);

        //then
        User user = userRepository.findById(userIdResponse.id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        assertThat(user.getName()).isEqualTo(userUpdateRequest.name);
    }

    @Test
    @WithMockUser(username = "user")
    void 회원정보_조회() {
        //when
        UserDetailsResponse userDetailsResponse = userService.getCurrentUserDetails();
        //then
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자가 없습니다"));
        assertThat(user.getId()).isEqualTo(userDetailsResponse.id);
        assertThat(user.getName()).isEqualTo(userDetailsResponse.name);
        assertThat(user.getUsername()).isEqualTo(userDetailsResponse.username);
        assertThat(user.getEmail()).isEqualTo(userDetailsResponse.email);
    }
    


}
