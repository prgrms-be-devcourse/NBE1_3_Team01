package org.team1.nbe1_3_team01.domain.user.service;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.team1.nbe1_3_team01.domain.user.controller.request.CourseCreateRequest;
import org.team1.nbe1_3_team01.domain.user.controller.request.CourseUpdateRequest;
import org.team1.nbe1_3_team01.domain.user.entity.Course;
import org.team1.nbe1_3_team01.domain.user.repository.CourseRepository;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;
import org.team1.nbe1_3_team01.domain.user.service.response.CourseDetailsResponse;
import org.team1.nbe1_3_team01.domain.user.service.response.CourseIdResponse;
import org.team1.nbe1_3_team01.domain.user.service.response.UserBriefResponse;
import org.team1.nbe1_3_team01.domain.user.service.response.UserBriefWithRoleResponse;
import org.team1.nbe1_3_team01.domain.user.util.UserConverter;
import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.SecurityUtil;

import java.util.List;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.*;
import static org.team1.nbe1_3_team01.global.util.ErrorCode.COURSE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Transactional
    public CourseIdResponse createCourse(CourseCreateRequest courseCreateRequest) {
        if (courseRepository.findByName(courseCreateRequest.name()).isPresent()) {
            throw new AppException(COURSE_ALREADY_EXISTS);
        }
        Course course = Course.builder()
                .name(courseCreateRequest.name())
                .build();
        Long id = courseRepository.save(course).getId();
        return new CourseIdResponse(id);
    }

    public List<CourseDetailsResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(course -> new CourseDetailsResponse(course.getId(), course.getName()))
                .toList();
    }

    @Transactional
    public CourseIdResponse updateCourse(CourseUpdateRequest courseUpdateRequest) {
        Course course = courseRepository.findById(courseUpdateRequest.id())
                .orElseThrow(() -> new AppException(COURSE_NOT_FOUND));
        course.updateName(courseUpdateRequest.name());
        return new CourseIdResponse(course.getId());
    }

    @Transactional
    public void deleteCourse(Long courseId){
        findById(courseId).delete();
    }

    public List<UserBriefResponse> getMyCourseUsers(){
        Course course = userRepository.findByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."))
                .getCourse();

        return mapToUserBriefResponse(course);
    }
    public List<UserBriefResponse> getCourseUsers(Long courseId) {
        Course course = findById(courseId);
        return mapToUserBriefResponse(course);
    }

    private List<UserBriefResponse> mapToUserBriefResponse(Course course){
        return userRepository.findByCourse(course)
                .stream()
                .map(UserConverter::toUserBriefResponse)
                .toList();
    }

    public Course findById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(COURSE_NOT_FOUND));
    }

    public List<UserBriefWithRoleResponse> getCourseUsersWithAdmins(Long courseId) {
        return mapToUserBriefWithRoleResponse(courseId);
    }

    public List<UserBriefWithRoleResponse> getMyCourseUsersWithAdmins(){
        Long courseId = userRepository.findByUsername(SecurityUtil.getCurrentUsername())
                .orElseThrow(() -> new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다."))
                .getCourse()
                .getId();
        return mapToUserBriefWithRoleResponse(courseId);
    }

    private List<UserBriefWithRoleResponse> mapToUserBriefWithRoleResponse(Long courseId){
        return userRepository.findUsersAndAdminsByCourseId(courseId)
                .stream()
                .map(UserConverter::toUserBriefWithRoleResponse)
                .toList();
    }

}
