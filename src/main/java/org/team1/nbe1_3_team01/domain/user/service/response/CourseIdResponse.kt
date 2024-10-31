package org.team1.nbe1_3_team01.domain.user.service.response

import org.team1.nbe1_3_team01.domain.user.entity.Course


data class CourseIdResponse(val id: Long){
    companion object {
        fun from(course: Course): CourseIdResponse {
            return CourseIdResponse(
                id = course.id!!
            )
        }
    }
}
