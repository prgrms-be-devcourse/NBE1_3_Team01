package org.team1.nbe1_3_team01.domain.board.service.converter

import lombok.AccessLevel
import lombok.NoArgsConstructor
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@NoArgsConstructor(access = AccessLevel.PRIVATE)
object DateTimeToStringConverter {
    @JvmStatic
    fun convert(dateTime: LocalDateTime?): String {
        return dateTime?.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))?:""
    }
}
