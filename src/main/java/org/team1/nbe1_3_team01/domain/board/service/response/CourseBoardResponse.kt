package org.team1.nbe1_3_team01.domain.board.service.response

import lombok.*
import org.team1.nbe1_3_team01.domain.board.service.converter.DateTimeToStringConverter
import java.time.LocalDateTime

@Getter
@ToString
class CourseBoardResponse @Builder private constructor(
    private val id: Long?,
    private val title: String?,
    private val writer: String?,
    private val createdAt: String?,
    private val readCount: Long?
) {
    companion object {
        fun of(
            id: Long?,
            title: String?,
            writer: String?,
            createdAt: LocalDateTime?,
            readCount: Long
        ): CourseBoardResponse =
            CourseBoardResponse(
                id = id,
                title = title,
                writer = writer,
                readCount = readCount,
                createdAt = DateTimeToStringConverter.convert(createdAt)
            )
    }
}
