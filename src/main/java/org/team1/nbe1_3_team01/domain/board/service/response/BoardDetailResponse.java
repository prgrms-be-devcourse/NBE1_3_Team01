package org.team1.nbe1_3_team01.domain.board.service.response;

import lombok.Builder;
import org.team1.nbe1_3_team01.domain.board.service.converter.DateTimeToStringConverter;

import java.time.LocalDateTime;

public record BoardDetailResponse(
        Long id,
        String title,
        String content,
        String writer,
        Long readCount,
        String createdAt,
        boolean isAdmin,
        boolean isMine
) {

    @Builder
    private BoardDetailResponse(
            Long id,
            String title,
            String content,
            String writer,
            Long readCount,
            LocalDateTime createdAt,
            boolean isAdmin,
            boolean isMine
    ) {
        this(id, title, content, writer, readCount, DateTimeToStringConverter.convert(createdAt), isAdmin, isMine);
    }

    public static BoardDetailResponse of(
            Long id,
            String title,
            String content,
            String writer,
            Long readCount,
            LocalDateTime createdAt,
            boolean isAdmin,
            boolean isMine
    ) {
        return BoardDetailResponse.builder()
                .id(id)
                .title(title)
                .content(content)
                .writer(writer)
                .readCount(readCount)
                .createdAt(createdAt)
                .isAdmin(isAdmin)
                .isMine(isMine)
                .build();
    }
}
