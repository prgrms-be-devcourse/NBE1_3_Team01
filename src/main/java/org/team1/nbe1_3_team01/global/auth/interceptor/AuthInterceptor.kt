package org.team1.nbe1_3_team01.global.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import org.team1.nbe1_3_team01.domain.group.repository.BelongingRepository
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository
import org.team1.nbe1_3_team01.global.auth.interceptor.GroupAuth
import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode
import org.team1.nbe1_3_team01.global.util.SecurityUtil

@Component
class AuthInterceptor(
    private val userRepository: UserRepository,
    private val belongingRepository: BelongingRepository
) : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        if (handler !is HandlerMethod) return true

        val annotation = handler.getMethodAnnotation(GroupAuth::class.java) ?: return true

        val username = SecurityUtil.getCurrentUsername()
        val user = userRepository.findByUsername(username)
            ?: throw AppException(ErrorCode.USER_NOT_FOUND)

        when (annotation.role) {
            GroupAuth.Role.ADMIN -> {
                return true
            }
            GroupAuth.Role.COURSE -> {
                val courseId = request.getParameter("course-id")?.toLongOrNull()
                    ?: throw AppException(ErrorCode.INVALID_COURSE_ID)
                if (user.course?.id != courseId) {
                    throw AppException(ErrorCode.COURSE_AUTH_DENIED)
                }
            }
            GroupAuth.Role.TEAM -> {
                val teamId = request.getParameter("team-id")?.toLongOrNull()
                    ?: throw AppException(ErrorCode.INVALID_TEAM_ID)
                if (!belongingRepository.existsByTeamIdAndUserId(teamId, user.id)) {
                    throw AppException(ErrorCode.TEAM_AUTH_DENIED)
                }
            }
        }

        return true
    }
}
