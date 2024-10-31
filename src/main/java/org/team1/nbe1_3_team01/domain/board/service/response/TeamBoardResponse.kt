package org.team1.nbe1_3_team01.domain.board.service.response

import lombok.*
import org.team1.nbe1_3_team01.domain.board.service.converter.DateTimeToStringConverter
import java.time.LocalDateTime


@ToString
data class TeamBoardResponse (
    private val id: Long?,
    private val title: String?,
    private val writer: String?,
    private val categoryName: String?,
    private val createdAt: String?,
    private val commentCount: Long
) {
    companion object {
        fun of(
            id: Long?,
            title: String?,
            writer: String?,
            categoryName: String?,
            createdAt: LocalDateTime?,
            commentCount: Long
        ): TeamBoardResponse =
            TeamBoardResponse(
                id = id,
                title = title,
                writer = writer,
                categoryName = categoryName,
                createdAt = DateTimeToStringConverter.convert(createdAt),
                commentCount = commentCount
            )
    }
}
