package org.team1.nbe1_3_team01.domain.user.service.response

import org.team1.nbe1_3_team01.domain.user.entity.Course


data class CourseDetailsResponse(
    val id: Long,
    val name: String
){
    companion object {
        fun from(course: Course): CourseDetailsResponse {
            return CourseDetailsResponse(
                id = course.id!!,
                name = course.name
            )
        }
    }
}
