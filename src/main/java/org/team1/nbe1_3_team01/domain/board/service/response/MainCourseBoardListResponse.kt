package org.team1.nbe1_3_team01.domain.board.service.response

import lombok.*
import kotlin.math.min

@Getter
@ToString
class MainCourseBoardListResponse @Builder private constructor(
    notices: List<CourseBoardResponse>,
    studies: List<CourseBoardResponse>,
    courseId: Long
) {
    private val notices: List<CourseBoardResponse>
    private val studies: List<CourseBoardResponse>
    private val courseId: Long

    init {
        val noticeLastIndex = min(5.0, notices.size.toDouble()).toInt()
        val studyLastIndex = min(5.0, studies.size.toDouble()).toInt()
        this.notices = notices.subList(0, noticeLastIndex)
        this.studies = studies.subList(0, studyLastIndex)
        this.courseId = courseId
    }

    companion object {
        fun of(
            notices: List<CourseBoardResponse>,
            studies: List<CourseBoardResponse>,
            courseId: Long
        ): MainCourseBoardListResponse {
            return MainCourseBoardListResponse(
                notices = notices,
                studies = studies,
                courseId = courseId
            )
        }
    }
}
