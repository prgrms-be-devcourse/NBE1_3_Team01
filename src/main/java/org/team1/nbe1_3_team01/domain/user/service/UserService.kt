package org.team1.nbe1_3_team01.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.user.controller.request.UserDeleteRequest;
import org.team1.nbe1_3_team01.domain.user.controller.request.UserSignUpRequest;
import org.team1.nbe1_3_team01.domain.user.controller.request.UserUpdateRequest;
import org.team1.nbe1_3_team01.domain.user.entity.Course;
import org.team1.nbe1_3_team01.domain.user.entity.Role;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.domain.user.repository.CourseRepository;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_3_team01.domain.user.service.response.UserAdminCheckResponse;
import org.team1.nbe1_3_team01.domain.user.service.response.UserDetailsResponse;
import org.team1.nbe1_3_team01.domain.user.service.response.UserIdResponse;
import org.team1.nbe1_3_team01.domain.user.util.UserConverter;
import org.team1.nbe1_3_team01.global.auth.jwt.respository.RefreshTokenRepository;
import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.SecurityUtil;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.*;
import static org.team1.nbe1_3_team01.global.util.ErrorCode.EMAIL_ALREADY_EXISTS;
import static org.team1.nbe1_3_team01.global.util.ErrorCode.USERNAME_ALREADY_EXISTS;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserIdResponse signUp(UserSignUpRequest userSignUpRequest) {
        if (userRepository.findByUsername(userSignUpRequest.username()).isPresent()) {
            throw new AppException(USERNAME_ALREADY_EXISTS.withArgs(userSignUpRequest.username()));
        }
        if (userRepository.findByEmail(userSignUpRequest.email()).isPresent()) {
            throw new AppException(EMAIL_ALREADY_EXISTS.withArgs(userSignUpRequest.email()));
        }

        Course course = courseRepository.findById(userSignUpRequest.courseId())
                .orElseThrow(() -> new AppException(COURSE_NOT_FOUND));

        User user = User.builder()
                .username(userSignUpRequest.username())
                .password(userSignUpRequest.password())
                .email(userSignUpRequest.email())
                .name(userSignUpRequest.name())
                .role(Role.USER)
                .course(course)
                .build();
        course.addUser(user);
        user.passwordEncode(passwordEncoder);
        return UserConverter.toUserIdResponse(userRepository.save(user));
    }

    @Transactional
    public void logout() {
        String currentUsername = SecurityUtil.getCurrentUsername();
        refreshTokenRepository.deleteById(currentUsername);
    }

    @Transactional
    public UserIdResponse update(UserUpdateRequest userUpdateRequest) {
        User user = getcurrentuser();
        if (userUpdateRequest.name() != null) {
            user.updateName(userUpdateRequest.name());
        }
        if (userUpdateRequest.password() != null) {
            user.updatePassword(userUpdateRequest.password());
            user.passwordEncode(passwordEncoder);
        }
        return UserConverter.toUserIdResponse(user);
    }

    @Transactional
    public void delete(UserDeleteRequest userDeleteRequest){
        User user = getcurrentuser();
        if (!passwordEncoder.matches(userDeleteRequest.password(), user.getPassword())) {
            throw new AppException(PASSWORD_NOT_VALID);
        }
        user.delete();
    }

    public UserDetailsResponse getCurrentUserDetails() {
        User user = getcurrentuser();
        return UserConverter.toUserDetailsResponse(user);
    }

    private User getcurrentuser() {
        String username = SecurityUtil.getCurrentUsername();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자가 존재하지 않습니다."));
    }

    public UserAdminCheckResponse isAdmin() {
        return UserConverter.toUserAdminCheckResponse(getcurrentuser());
    }
}
