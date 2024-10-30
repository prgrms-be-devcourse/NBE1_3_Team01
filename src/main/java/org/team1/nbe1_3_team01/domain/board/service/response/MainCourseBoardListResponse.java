package org.team1.nbe1_3_team01.domain.board.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter @ToString
public class MainCourseBoardListResponse {
    private final List<CourseBoardResponse> notices;
    private final List<CourseBoardResponse> studies;
    private final Long courseId;

    @Builder
    private MainCourseBoardListResponse(
            List<CourseBoardResponse> notices,
            List<CourseBoardResponse> studies,
            Long courseId
    ) {
        int noticeLastIndex = Math.min(5, notices.size());
        int studyLastIndex = Math.min(5, studies.size());
        this.notices = notices.subList(0, noticeLastIndex);
        this.studies = studies.subList(0, studyLastIndex);
        this.courseId = courseId;
    }

    public static MainCourseBoardListResponse of(
            List<CourseBoardResponse> notices,
            List<CourseBoardResponse> studies,
            Long courseId
    ) {
        return MainCourseBoardListResponse.builder()
                .notices(notices)
                .studies(studies)
                .courseId(courseId)
                .build();
    }
}
