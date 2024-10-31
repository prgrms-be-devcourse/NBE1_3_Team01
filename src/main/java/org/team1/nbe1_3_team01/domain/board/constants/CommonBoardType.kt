package org.team1.nbe1_3_team01.domain.board.constants

import lombok.Getter
import org.team1.nbe1_3_team01.domain.board.exception.NotFoundTypeException
import org.team1.nbe1_3_team01.global.util.ErrorCode

@Getter
enum class CommonBoardType(private val type: String) {
    NOTICE("y"),
    STUDY("n");

    companion object {
        fun getType(type: String): CommonBoardType {
            for (enumType in entries) {
                if (enumType.type == type) {
                    return enumType
                }
            }
            throw NotFoundTypeException(ErrorCode.CATEGORY_NOT_FOUND)
        }
    }
}
