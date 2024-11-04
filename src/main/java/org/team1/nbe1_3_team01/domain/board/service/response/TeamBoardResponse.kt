package org.team1.nbe1_3_team01.domain.board.service.response

import lombok.*
import org.team1.nbe1_3_team01.domain.board.service.converter.DateTimeToStringConverter
import java.time.LocalDateTime


data class TeamBoardResponse (
    val id: Long?,
    val title: String?,
    val writer: String?,
    val categoryName: String?,
    val createdAt: String?,
    val readCount: Long,
    val commentCount: Long
) {
    companion object {
        fun of(
            id: Long?,
            title: String?,
            writer: String?,
            categoryName: String?,
            createdAt: LocalDateTime?,
            readCount: Long,
            commentCount: Long
        ): TeamBoardResponse =
            TeamBoardResponse(
                id = id,
                title = title,
                writer = writer,
                categoryName = categoryName,
                createdAt = DateTimeToStringConverter.convert(createdAt),
                commentCount = commentCount,
                readCount = readCount
            )
    }
}
