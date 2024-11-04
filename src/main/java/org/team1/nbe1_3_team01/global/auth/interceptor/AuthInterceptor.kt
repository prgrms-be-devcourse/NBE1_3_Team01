package org.team1.nbe1_3_team01.global.auth.interceptor;

import static org.team1.nbe1_3_team01.global.auth.interceptor.GroupAuth.Role.ADMIN;
import static org.team1.nbe1_3_team01.global.auth.interceptor.GroupAuth.Role.COURSE;
import static org.team1.nbe1_3_team01.global.auth.interceptor.GroupAuth.Role.TEAM;
import static org.team1.nbe1_3_team01.global.util.ErrorCode.COURSE_AUTH_DENIED;
import static org.team1.nbe1_3_team01.global.util.ErrorCode.TEAM_AUTH_DENIED;
import static org.team1.nbe1_3_team01.global.util.ErrorCode.USER_NOT_FOUND;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.team1.nbe1_3_team01.global.auth.interceptor.GroupAuth.Role;
import org.team1.nbe1_3_team01.domain.group.repository.BelongingRepository;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.SecurityUtil;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final UserRepository userRepository;
    private final BelongingRepository belongingRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1 - HandlerMethod 타입 체크
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 2 - @GroupAuth 적용 여부
        GroupAuth annotation = handlerMethod.getMethodAnnotation(GroupAuth.class);
        if (annotation == null) {
            return true;
        }

        // 3 - ADMIN 통과
        Role role = annotation.role();
        if (role.equals(ADMIN)) {
            return true;
        }

        // 4 - query parameter parsing
        String username = SecurityUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(USER_NOT_FOUND));

        if (role.equals(COURSE)) {
            Long courseId = Long.parseLong(request.getParameter("course-id"));

            if (!user.getCourse().getId().equals(courseId)) {
                throw new AppException(COURSE_AUTH_DENIED);
            }
        }

        if (role.equals(TEAM)) {
            Long teamId = Long.parseLong(request.getParameter("team-id"));

            if (!belongingRepository.existsByTeamIdAndUserId(teamId, user.getId())) {
                throw new AppException(TEAM_AUTH_DENIED);
            }
        }

        return true;
    }
}
