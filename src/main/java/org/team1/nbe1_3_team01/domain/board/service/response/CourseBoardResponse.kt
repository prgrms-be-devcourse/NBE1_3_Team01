package org.team1.nbe1_3_team01.domain.board.service.response

import lombok.*
import org.team1.nbe1_3_team01.domain.board.service.converter.DateTimeToStringConverter
import java.time.LocalDateTime


data class CourseBoardResponse (
    val id: Long?,
    val title: String?,
    val writer: String?,
    val createdAt: String?,
    val readCount: Long?
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
