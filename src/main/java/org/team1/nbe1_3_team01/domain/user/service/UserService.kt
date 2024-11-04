package org.team1.nbe1_3_team01.domain.user.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.user.controller.request.UserDeleteRequest
import org.team1.nbe1_3_team01.domain.user.controller.request.UserSignUpRequest
import org.team1.nbe1_3_team01.domain.user.controller.request.UserUpdateRequest
import org.team1.nbe1_3_team01.domain.user.entity.Course
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.CourseRepository
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.domain.user.service.response.UserAdminCheckResponse
import org.team1.nbe1_3_team01.domain.user.service.response.UserDetailsResponse
import org.team1.nbe1_3_team01.domain.user.service.response.UserIdResponse
import org.team1.nbe1_3_team01.global.auth.jwt.respository.RefreshTokenRepository
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import org.team1.nbe1_3_team01.global.util.SecurityUtil

@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val courseRepository: CourseRepository,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val passwordEncoder: PasswordEncoder
) {

    @Transactional
    fun signUp(userSignUpRequest: UserSignUpRequest): UserIdResponse {
        userRepository.findByUsername(userSignUpRequest.username)?.let {
            throw AppException(ErrorCode.USERNAME_ALREADY_EXISTS.withArgs(userSignUpRequest.username))
        }
        userRepository.findByEmail(userSignUpRequest.email)?.let {
            throw AppException(ErrorCode.EMAIL_ALREADY_EXISTS.withArgs(userSignUpRequest.email))
        }

        val course: Course = courseRepository.findByIdOrNull(userSignUpRequest.courseId)
            ?: throw AppException(ErrorCode.COURSE_NOT_FOUND)

        val user: User = User.ofUser(
            username = userSignUpRequest.username,
            password = userSignUpRequest.password,
            email = userSignUpRequest.email,
            name = userSignUpRequest.name,
            course = course
        )
        course.addUser(user)
        user.passwordEncode(passwordEncoder)
        return UserIdResponse.from(userRepository.save(user))
    }

    @Transactional
    fun logout() {
        val currentUsername = SecurityUtil.getCurrentUsername()
        refreshTokenRepository.deleteById(currentUsername)
    }

    @Transactional
    fun update(userUpdateRequest: UserUpdateRequest): UserIdResponse {
        val user = getCurrentUser()
        if (userUpdateRequest.name != null) {
            user.updateName(userUpdateRequest.name)
        }
        if (userUpdateRequest.password != null) {
            user.updatePassword(userUpdateRequest.password)
            user.passwordEncode(passwordEncoder)
        }
        return UserIdResponse.from(user)
    }

    @Transactional
    fun delete(userDeleteRequest: UserDeleteRequest) {
        val user = getCurrentUser()
        if (!passwordEncoder.matches(userDeleteRequest.password, user.password)) {
            throw AppException(ErrorCode.PASSWORD_NOT_VALID)
        }
        user.delete()
    }

    fun currentUserDetails(): UserDetailsResponse {
            return UserDetailsResponse.from(getCurrentUser())
        }

    fun isAdmin(): UserAdminCheckResponse {
        return UserAdminCheckResponse.from(getCurrentUser())
    }

    private fun getCurrentUser(): User {
        val username = SecurityUtil.getCurrentUsername()
        return userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("해당 사용자가 존재하지 않습니다.")
    }

}
