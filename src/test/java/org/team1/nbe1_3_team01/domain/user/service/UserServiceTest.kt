package org.team1.nbe1_3_team01.domain.user.service

import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.IntegrationTestSupport
import org.team1.nbe1_3_team01.domain.user.controller.request.UserSignUpRequest
import org.team1.nbe1_3_team01.domain.user.controller.request.UserUpdateRequest
import org.team1.nbe1_3_team01.domain.user.entity.Course
import org.team1.nbe1_3_team01.domain.user.entity.Role
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.CourseRepository
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.domain.user.service.response.UserDetailsResponse
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode

@Transactional
internal class UserServiceTest : IntegrationTestSupport() {
    @Autowired
    var userRepository: UserRepository? = null

    @Autowired
    var passwordEncoder: PasswordEncoder? = null

    @Autowired
    var courseRepository: CourseRepository? = null

    @Autowired
    private val userService: UserService? = null

    private val username = "user"
    private val password = "1234abcd"
    private val email = "user@gmail.com"
    private val name = "김철수"
    private val course: Course = Course.of(
        name = "데브코스 1기 백엔드"
    )


    /**
     * 모든 테스트 이전에 회원가입 실행
     */
    @BeforeEach
    fun setUp() {
        courseRepository!!.save(course)
        val request = UserSignUpRequest(
            username,
            password,
            email,
            name,
            course.id
        )
        userService!!.signUp(request)
    }

    @Test
    fun 회원가입_성공() {
        // Then
        val user: User = userRepository!!.findByUsername(username)
            ?: throw UsernameNotFoundException("해당 사용자 없음")
        assertThat(user.username).isEqualTo(username)
        assertThat(passwordEncoder!!.matches(password, user.password)).isTrue()
        assertThat(user.email).isEqualTo(email)
        assertThat(user.name).isEqualTo(name)
        assertThat<Role?>(user.role).isEqualTo(Role.USER)
        assertThat<Course?>(user.course).isEqualTo(course)
    }

    @Test
    fun 회원가입_실패_아이디_중복() {
        //when
        val userSignUpRequest = UserSignUpRequest(
            "user",
            "abcd1234",
            "user@naver.com",
            "김영희",
            course.id
        )
        // then
        val exception = assertThrows<AppException> { userService?.signUp(userSignUpRequest) }
        assertThat(exception.errorCode).isEqualTo(ErrorCode.USERNAME_ALREADY_EXISTS)
    }

    @Test
    fun 회원가입_실패_이메일_중복() {
        //when
        val userSignUpRequest = UserSignUpRequest(
            "usre",
            "1234",
            "user@gmail.com",
            "김영희",
            course.id
        )

        // then
        val exception = assertThrows<AppException> { userService?.signUp(userSignUpRequest) }
        assertThat(exception.errorCode).isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS)
    }

    @Test
    @WithMockUser(username = "user")
    fun 회원수정_이름_변경() {
        //given
        val userUpdateRequest = UserUpdateRequest(
            "김미희",
            null
        )
        //when
        val userIdResponse = userService!!.update(userUpdateRequest)

        //then
        val user = userRepository!!.findByIdOrNull(userIdResponse.id)
            ?: throw AppException(ErrorCode.USER_NOT_FOUND)
        assertThat(user.name).isEqualTo(userUpdateRequest.name)
    }

    @Test
    @WithMockUser(username = "user")
    fun 회원정보_조회() {
        //when
        val userDetailsResponse: UserDetailsResponse = userService!!.currentUserDetails()
        //then
        val user: User = userRepository!!.findByUsername(username)
            ?: throw UsernameNotFoundException("해당 사용자가 없습니다")
        assertThat(user.id).isEqualTo(userDetailsResponse.id)
        assertThat(user.name).isEqualTo(userDetailsResponse.name)
        assertThat(user.username).isEqualTo(userDetailsResponse.username)
        assertThat(user.email).isEqualTo(userDetailsResponse.email)
    }
}
