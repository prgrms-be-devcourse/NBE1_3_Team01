package org.team1.nbe1_3_team01.domain.board.service.response

import kotlin.math.min

data class MainCourseBoardListResponse(
    val notices: List<CourseBoardResponse>,
    val studies: List<CourseBoardResponse>,
    val courseId: Long
) {

    companion object {
        fun of(
            notices: List<CourseBoardResponse>,
            studies: List<CourseBoardResponse>,
            courseId: Long
        ): MainCourseBoardListResponse {
            val noticeLastIndex = min(5.0, notices.size.toDouble()).toInt()
            val studyLastIndex = min(5.0, studies.size.toDouble()).toInt()
            return MainCourseBoardListResponse(
                notices = notices.subList(0, noticeLastIndex),
                studies = studies.subList(0, studyLastIndex),
                courseId = courseId
            )
        }
    }
}
