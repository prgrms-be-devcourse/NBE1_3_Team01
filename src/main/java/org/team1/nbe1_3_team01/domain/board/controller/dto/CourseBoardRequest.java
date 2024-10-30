package org.team1.nbe1_3_team01.domain.board.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.team1.nbe1_3_team01.domain.board.entity.CourseBoard;
import org.team1.nbe1_3_team01.domain.user.entity.Course;
import org.team1.nbe1_3_team01.domain.user.entity.User;

public record CourseBoardRequest(
        @NotNull(message = "필수 파라미터 누락")
        @Positive(message = "필수 파라미터 형식 오류")
        Long courseId,

        @NotBlank(message = "제목은 필수 입력값입니다.")
        String title,

        @NotBlank(message = "내용은 필수 입력값입니다.")
        String content,

        @NotNull(message = "필수 파라미터 누락")
        boolean isNotice
) {
        public CourseBoard toEntity(User user, Course course) {
                return CourseBoard.builder()
                        .title(title)
                        .content(content)
                        .course(course)
                        .user(user)
                        .build();
        }
}
