package org.team1.nbe1_3_team01.domain.user.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.jayway.jsonpath.JsonPath
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.http.MediaType
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.IntegrationTestSupport
import org.team1.nbe1_3_team01.domain.user.controller.request.UserDeleteRequest
import org.team1.nbe1_3_team01.domain.user.controller.request.UserSignUpRequest
import org.team1.nbe1_3_team01.domain.user.controller.request.UserUpdateRequest
import org.team1.nbe1_3_team01.domain.user.entity.Course
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.CourseRepository
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.domain.user.service.UserService
import org.team1.nbe1_3_team01.global.auth.jwt.respository.RefreshTokenRepository
import org.team1.nbe1_3_team01.global.auth.jwt.service.JwtService
import java.lang.Exception

@AutoConfigureMockMvc
@Transactional
internal class UserControllerTest : IntegrationTestSupport() {
    @Autowired
    private val mockMvc: MockMvc? = null

    @Autowired
    private val userService: UserService? = null

    @Autowired
    private val jwtService: JwtService? = null

    @Autowired
    private val objectMapper: ObjectMapper? = null

    @Autowired
    private val passwordEncoder: PasswordEncoder? = null

    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val courseRepository: CourseRepository? = null

    @Autowired
    private val refreshTokenRepository: RefreshTokenRepository? = null

    private val course: Course = Course.of(
        name = "데브코스 1기 백엔드"
    )

    @BeforeEach
    fun setUp() {
        courseRepository?.save(course)
    }

    @Test
    @Throws(Exception::class)
    fun 회원가입_성공() {
        // 요청과 응답 데이터 설정
        val request = UserSignUpRequest(
            "userA",
            "1234abcd",
            "user@gmail.com",
            "김철수",
            course.id
        )
        // 응답 검증
        val result = mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper!!.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Success"))
            .andReturn()
        val jsonResponse = result.response.contentAsString
        val resultId = JsonPath.parse(jsonResponse).read<Long?>("$.result.id", Long::class.java)

        val savedUser: User = userRepository!!.findByUsername(request.username)
            ?: throw UsernameNotFoundException("해당 사용자 없음")
        assertThat(savedUser.id).isEqualTo(resultId)
    }

    @Test
    @Throws(Exception::class)
    fun 회원가입_실패_아이디는_5이상_20이하여야한다() {
        val request = UserSignUpRequest(
            "user",
            "1234abcd",
            "user@gmail.com",
            "김철수",
            course.id
        )
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper!!.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message.username").value("아이디는 5자 이상, 20자 이하여야 합니다."))
    }

    @Test
    @Throws(Exception::class)
    fun 회원가입_실패_아이디는_영문과숫자여야한다() {
        val request = UserSignUpRequest(
            "가나다라마바",
            "1234abcd",
            "user@gmail.com",
            "김철수",
            course.id
        )
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper!!.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message.username").value("아이디는 영문과 숫자만 사용할 수 있습니다."))
    }

    @Test
    @Throws(Exception::class)
    fun 회원가입_실패_비밀번호는_8자리이상이어야한다() {
        val request = UserSignUpRequest(
            "userA",
            "1234abc",
            "user@gmail.com",
            "김철수",
            course.id
        )
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper!!.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message.password").value("비밀번호는 8자 이상이어야 합니다."))
    }

    @Test
    @Throws(Exception::class)
    fun 회원가입_실패_비밀번호는_영문을_포함해야한다() {
        val request = UserSignUpRequest(
            "userA",
            "12341234",
            "user@gmail.com",
            "김철수",
            course.id
        )
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper!!.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message.password").value("비밀번호는 영문과 숫자를 포함해야 합니다."))
    }

    @Test
    @Throws(Exception::class)
    fun 회원가입_실패_비밀번호는_숫자를_포함해야한다() {
        val request = UserSignUpRequest(
            "userA",
            "abcdabcd",
            "user@gmail.com",
            "김철수",
            course.id
        )
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper!!.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message.password").value("비밀번호는 영문과 숫자를 포함해야 합니다."))
    }

    @Test
    @Throws(Exception::class)
    fun 회원가입_실패_이메일은_이메일형식이어야한다() {
        val request = UserSignUpRequest(
            "userA",
            "1234abcd",
            "usergmail.com",
            "김철수",
            course.id
        )
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper!!.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message.email").value("유효하지 않은 이메일 형식입니다."))
    }

    @Test
    @Throws(Exception::class)
    fun 회원가입_실패_이름은_2글자이상이어야한다() {
        val request = UserSignUpRequest(
            "userA",
            "1234abcd",
            "user@gmail.com",
            "김",
            course.id
        )
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/user/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper!!.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(400))
            .andExpect(MockMvcResultMatchers.jsonPath("$.message.name").value("이름은 2자 이상이어야 합니다."))
    }

    @Test
    @Throws(Exception::class)
    fun 로그인_성공() {
        val request = UserSignUpRequest(
            "userA",
            "1234abcd",
            "user@gmail.com",
            "김철수",
            course.id
        )
        userService!!.signUp(request)
        val result = mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"userA\", \"password\": \"1234abcd\"}")
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andReturn()
        val accessToken = result.response.getHeader("Authorization")
        val extractUsername: String? = jwtService!!.extractUsername(accessToken)
        val refreshToken = refreshTokenRepository!!.findById("userA")
        assertThat(extractUsername).isEqualTo("userA")
        assertThat(refreshToken).isPresent()
    }

    @Test
    @Throws(Exception::class)
    fun 로그인_실패_비밀번호_틀림() {
        val request = UserSignUpRequest(
            "userA",
            "1234abcd",
            "user@gmail.com",
            "김철수",
            course.id
        )
        userService!!.signUp(request)
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"userA\", \"password\": \"1234abce\"}")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("로그인에 실패 했습니다 아이디나 비밀번호를 확인해주세요."))
    }

    @Test
    @Throws(Exception::class)
    fun 로그인_실패_아이디_틀림() {
        val request = UserSignUpRequest(
            "userA",
            "1234abcd",
            "user@gmail.com",
            "김철수",
            course.id
        )
        userService!!.signUp(request)
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"userB\", \"password\": \"1234abcd\"}")
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().string("로그인에 실패 했습니다 아이디나 비밀번호를 확인해주세요."))
    }

    @Test
    @WithMockUser(username = "userA")
    @Throws(Exception::class)
    fun 로그아웃_성공() {
        val request = UserSignUpRequest(
            "userA",
            "1234abcd",
            "user@gmail.com",
            "김철수",
            course.id
        )
        userService!!.signUp(request)
        jwtService!!.createRefreshToken("userA")
        mockMvc!!.perform(
            MockMvcRequestBuilders.post("/api/user/logout")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent())
        assertThat(refreshTokenRepository!!.findById("userA")).isNotPresent()
    }

//    @Test
//    @WithMockUser(username = "userA",roles = ["USER"])
//    @Throws(Exception::class)
//    fun 회원탈퇴() {
//        val signUpRequest = UserSignUpRequest(
//            "userA",
//            "1234abcd",
//            "user@gmail.com",
//            "김철수",
//            course.id
//        )
//        userService!!.signUp(signUpRequest)
//        val deleteRequest = UserDeleteRequest("1234abcd")
//        mockMvc!!.perform(
//            MockMvcRequestBuilders.delete("/api/user")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper!!.writeValueAsString(deleteRequest))
//        )
//            .andExpect(MockMvcResultMatchers.status().isNoContent())
//        assertThat(userRepository!!.findByUsername("userA")).isNull()
//    }


    @Test
    @WithMockUser(username = "userA", roles = ["USER"])
    @Throws(Exception::class)
    fun 회원수정_성공() {
        //가짜 사용자를 쓰더라도 DB에 사용자가 없으면 현재 로그인된 사용자를 찾는 과정에서 에러가 발생하기때문에 DB에 넣어준다

        val user: User = User.ofUser(
            username = "userA",
            password = "1234abcd",
            email = "user@gmail.com",
            name = "김철수",
            course = course
        )

        userRepository!!.save(user)
        val request = UserUpdateRequest(
            "userB",
            "abcd1234"
        )

        mockMvc!!.perform(
            MockMvcRequestBuilders.patch("/api/user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper!!.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Success"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result.id").value(user.id))

        val updatedUser: User = userRepository.findByUsername("userA")
            ?:throw UsernameNotFoundException("해당 사용자 없음")

        assertThat(updatedUser.name).isEqualTo(request.name)
        assertThat(passwordEncoder!!.matches(request.password, updatedUser.password)).isTrue()
    }

    @Test
    @WithMockUser(username = "userA")
    @Throws(Exception::class)
    fun 회원정보조회_성공() {
        val user: User = User.ofUser(
            username = "userA",
            password = "1234abcd",
            email = "user@gmail.com",
            name = "김철수",
            course = course
        )
        course.addUser(user)
        val savedUser = userRepository!!.save(user)

        mockMvc!!.perform(MockMvcRequestBuilders.get("/api/user"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Success"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result.id").value(savedUser.id))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result.username").value(savedUser.username))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result.email").value(savedUser.email))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result.name").value(savedUser.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result.courseName").value(savedUser.course!!.name))
    }

    @Test
    @WithMockUser(username = "userA", roles = ["USER"])
    @Throws(Exception::class)
    fun 관리자가_아니면_false_반환() {
        val user: User = User.ofUser(
            username = "userA",
            password = "1234abcd",
            email = "user@gmail.com",
            name = "김철수",
            course = course
        )

        userRepository!!.save(user)

        mockMvc!!.perform(MockMvcRequestBuilders.get("/api/user/role"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Success"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result.admin").value(false))
    }

    @Test
    @WithMockUser(username = "adminA", roles = ["ADMIN"])
    @Throws(Exception::class)
    fun 관리자면_true_반환() {
        val user: User = User.ofAdmin(
            username = "adminA",
            password = "1234abcd",
            email = "user@gmail.com",
            name = "김철수"
        )

        userRepository!!.save(user)
        mockMvc!!.perform(MockMvcRequestBuilders.get("/api/user/role"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("Success"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.result.admin").value(true))
    }
}
