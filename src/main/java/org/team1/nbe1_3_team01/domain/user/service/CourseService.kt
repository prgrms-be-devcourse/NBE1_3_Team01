package org.team1.nbe1_3_team01.domain.user.service
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.team1.nbe1_3_team01.domain.user.controller.request.CourseCreateRequest
import org.team1.nbe1_3_team01.domain.user.controller.request.CourseUpdateRequest
import org.team1.nbe1_3_team01.domain.user.entity.Course
import org.team1.nbe1_3_team01.domain.user.entity.User
import org.team1.nbe1_3_team01.domain.user.repository.CourseRepository
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.domain.user.service.response.CourseDetailsResponse
import org.team1.nbe1_3_team01.domain.user.service.response.CourseIdResponse
import org.team1.nbe1_3_team01.domain.user.service.response.UserBriefResponse
import org.team1.nbe1_3_team01.domain.user.service.response.UserBriefWithRoleResponse
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import org.team1.nbe1_3_team01.global.util.SecurityUtil

@Service
@Transactional(readOnly = true)
class CourseService(
    private val courseRepository: CourseRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun createCourse(courseCreateRequest: CourseCreateRequest): CourseIdResponse {
        courseRepository.findByName(courseCreateRequest.name)?.let {
            throw AppException(ErrorCode.COURSE_ALREADY_EXISTS)
        }
        val course: Course = Course.of(name = courseCreateRequest.name)
        return CourseIdResponse.from(courseRepository.save(course))
    }

    fun allCourses(): List<CourseDetailsResponse> {
        return courseRepository.findAll()
            .map(CourseDetailsResponse::from)
    }

    @Transactional
    fun updateCourse(courseUpdateRequest: CourseUpdateRequest): CourseIdResponse {
        val course: Course = findById(courseUpdateRequest.id)
        course.updateName(courseUpdateRequest.name)
        return CourseIdResponse.from(course)
    }

    @Transactional
    fun deleteCourse(courseId: Long) {
        findById(courseId).delete()
    }


    fun getCourseUsers(courseId: Long): List<UserBriefResponse> {
        val course: Course = findById(courseId)
        return userRepository.findByCourse(course)
            .map(UserBriefResponse::from)
    }

    private fun findById(courseId: Long): Course {
        return courseRepository.findByIdOrNull(courseId)
            ?: throw AppException(ErrorCode.COURSE_NOT_FOUND)
    }

    fun myCourseUsers(): List<UserBriefResponse> {
        val user: User = getCurrentUser()
        val course: Course = user.course ?: throw AppException(ErrorCode.COURSE_NOT_FOUND)

        return userRepository.findByCourse(course)
            .map(UserBriefResponse::from)
    }


    fun getCourseUsersWithAdmins(courseId: Long): List<UserBriefWithRoleResponse> {
        return userRepository.findUsersAndAdminsByCourseId(courseId)
            .map(UserBriefWithRoleResponse::from)
    }

    fun myCourseUsersWithAdmins(): List<UserBriefWithRoleResponse> {
        val course: Course = getCurrentUser().course
            ?: throw AppException(ErrorCode.COURSE_NOT_FOUND)

        return userRepository.findUsersAndAdminsByCourseId(course.id!!)
            .map(UserBriefWithRoleResponse::from)
    }

    private fun getCurrentUser(): User {
        val username = SecurityUtil.getCurrentUsername()
        return userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("해당 사용자가 존재하지 않습니다.")
    }

}
