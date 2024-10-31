package org.team1.nbe1_3_team01.domain.user.controller.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.team1.nbe1_3_team01.domain.user.controller.request.CourseCreateRequest
import org.team1.nbe1_3_team01.domain.user.controller.request.CourseUpdateRequest
import org.team1.nbe1_3_team01.domain.user.service.CourseService
import org.team1.nbe1_3_team01.domain.user.service.response.CourseDetailsResponse
import org.team1.nbe1_3_team01.domain.user.service.response.CourseIdResponse
import org.team1.nbe1_3_team01.domain.user.service.response.UserBriefResponse
import org.team1.nbe1_3_team01.domain.user.service.response.UserBriefWithRoleResponse
import org.team1.nbe1_3_team01.global.util.Response

@RestController
@RequestMapping("/api/course")
class CourseController(private val courseService: CourseService) {

    /**
     * 코스 등록
     */
    @PostMapping("/admin")
    fun createCourse(@RequestBody courseCreateRequest: CourseCreateRequest): ResponseEntity<Response<CourseIdResponse>> {
        val courseIdResponse = courseService.createCourse(courseCreateRequest)
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                Response.success(courseIdResponse)
            )
    }

    /**
     * 전체 코스 조회
     */

    @GetMapping("/admin/all")
    fun allCourses(): ResponseEntity<Response<List<CourseDetailsResponse>>> {
        return ResponseEntity.ok()
            .body(
                Response.success(
                    courseService.allCourses()
                )
            )
    }

    /**
     * 특정 코스 이름 수정
     */
    @PatchMapping("/admin")
    fun updateCourse(@RequestBody courseUpdateRequest: CourseUpdateRequest): ResponseEntity<Response<CourseIdResponse>> {
        val courseIdResponse = courseService.updateCourse(courseUpdateRequest)
        return ResponseEntity.ok()
            .body(
                Response.success(courseIdResponse)
            )
    }

    /**
     * 특정 코스 삭제
     */
    @DeleteMapping("/admin/{courseId}")
    fun deleteCourse(@PathVariable courseId: Long): ResponseEntity<Unit> {
        courseService.deleteCourse(courseId)
        return ResponseEntity.noContent().build()
    }

    /**
     * 특정 코스에 속한 유저(USER) 조회
     * 관리자 전용
     */
    @GetMapping("/admin/{courseId}/users")
    fun getCourseUsers(@PathVariable courseId: Long): ResponseEntity<Response<List<UserBriefResponse>>> {
        return ResponseEntity.ok()
            .body(
                Response.success(
                    courseService.getCourseUsers(courseId)
                )
            )
    }

    /**
     * 특정 코스에 속한 유저(USER) + 관리자 조회
     * 관리자 전용
     */
    @GetMapping("/admin/{courseId}/users/all")
    fun getCourseUsersWithAdmins(@PathVariable courseId: Long): ResponseEntity<Response<List<UserBriefWithRoleResponse>>> {
        return ResponseEntity.ok()
            .body(
                Response.success(
                    courseService.getCourseUsersWithAdmins(courseId)
                )
            )
    }

    /**
     * 자신의 코스에 속한 유저(USER) 조회
     * 사용자 전용
     */
    @GetMapping("/users")
    fun getMyCourseUsers(): ResponseEntity<Response<List<UserBriefResponse>>> {
        return ResponseEntity.ok()
            .body(
                Response.success(
                    courseService.myCourseUsers()
                )
            )
    }

    /**
     * 자신의 코스에 속한 유저(USER) + 관리자 조회
     * 사용자 전용
     */
    @GetMapping("/users/all")
    fun myCourseUsersWithAdmins(): ResponseEntity<Response<List<UserBriefWithRoleResponse>>> {
        return ResponseEntity.ok()
            .body(
                Response.success(courseService.myCourseUsersWithAdmins())
            )
    }
}
