package org.team1.nbe1_3_team01.domain.board.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.team1.nbe1_3_team01.domain.board.service.converter.DateTimeToStringConverter;

import java.time.LocalDateTime;

@Getter @ToString
public class TeamBoardResponse {

    private final Long id;
    private final String title;
    private final String writer;
    private final String categoryName;
    private final String createdAt;
    private final Long commentCount;

    @Builder
    private TeamBoardResponse(Long id,
                              String title,
                              String writer,
                              String categoryName,
                              LocalDateTime createdAt,
                              Long commentCount) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.categoryName = categoryName;
        this.createdAt = DateTimeToStringConverter.convert(createdAt);
        this.commentCount = commentCount;
    }

    public static TeamBoardResponse of(Long id,
                                       String title,
                                       String writer,
                                       String categoryName,
                                       LocalDateTime createdAt,
                                       Long commentCount) {
        return TeamBoardResponse.builder()
                .id(id)
                .title(title)
                .writer(writer)
                .categoryName(categoryName)
                .createdAt(createdAt)
                .commentCount(commentCount)
                .build();
    }
}
