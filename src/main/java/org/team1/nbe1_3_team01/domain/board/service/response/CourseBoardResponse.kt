package org.team1.nbe1_3_team01.domain.board.service.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.team1.nbe1_3_team01.domain.board.service.converter.DateTimeToStringConverter;

import java.time.LocalDateTime;

@Getter @ToString
public class CourseBoardResponse {

    private final Long id;
    private final String title;
    private final String writer;
    private final String createdAt;
    private final Long readCount;

    @Builder
    private CourseBoardResponse(Long id,
                                String title,
                                String writer,
                                LocalDateTime createdAt, Long readCount) {
        this.id = id;
        this.title = title;
        this.writer = writer;
        this.createdAt = DateTimeToStringConverter.convert(createdAt);
        this.readCount = readCount;
    }

    public static CourseBoardResponse of(Long id,
                                         String title,
                                         String writer,
                                         LocalDateTime createdAt,
                                         Long readCount) {
        return CourseBoardResponse.builder()
                .id(id)
                .title(title)
                .writer(writer)
                .readCount(readCount)
                .createdAt(createdAt)
                .build();
    }
}
