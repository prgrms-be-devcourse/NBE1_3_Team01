package org.team1.nbe1_3_team01.domain.board.comment.service.response

import lombok.*
import org.team1.nbe1_3_team01.domain.board.service.converter.DateTimeToStringConverter
import java.time.LocalDateTime

@Getter
@ToString
class CommentResponse @Builder private constructor(
    val id: Long?,
    val writer: String?,
    val content: String?,
    val createdAt: String?,
    val isAdmin: Boolean,
    val isMine: Boolean
) {
    companion object {
        @JvmStatic
        fun of(
            id: Long?,
            writer: String?,
            content: String?,
            createdAt: LocalDateTime?,
            isAdmin: Boolean,
            isMine: Boolean
        ): CommentResponse {
            return CommentResponse(
                id = id,
                writer = writer,
                content = content,
                createdAt = DateTimeToStringConverter.convert(createdAt),
                isAdmin = isAdmin,
                isMine = isMine
            )
        }
    }
}
