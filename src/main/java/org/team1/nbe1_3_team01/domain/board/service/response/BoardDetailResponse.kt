package org.team1.nbe1_3_team01.domain.board.service.response

import org.team1.nbe1_3_team01.domain.board.service.converter.DateTimeToStringConverter
import java.time.LocalDateTime

data class BoardDetailResponse(
    val id: Long?,
    val title: String?,
    val content: String?,
    val writer: String?,
    val categoryName: String?,
    val readCount: Long,
    val createdAt: String?,
    val isAdmin: Boolean?,
    val isMine: Boolean?
) {
    companion object {
        fun of(
            id: Long?,
            title: String?,
            content: String?,
            writer: String?,
            categoryName: String?,
            readCount: Long,
            createdAt: LocalDateTime?,
            isAdmin: Boolean,
            isMine: Boolean
        ): BoardDetailResponse =
            BoardDetailResponse(
                id = id,
                title = title,
                content = content,
                writer = writer,
                categoryName = categoryName,
                readCount = readCount,
                createdAt = DateTimeToStringConverter.convert(createdAt),
                isAdmin = isAdmin,
                isMine = isMine
            )
    }
}
