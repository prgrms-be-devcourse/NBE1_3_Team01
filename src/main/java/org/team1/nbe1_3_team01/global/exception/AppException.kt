package org.team1.nbe1_3_team01.global.exception

import org.team1.nbe1_3_team01.global.util.ErrorCode

open class AppException(
    val errorCode: ErrorCode
) : RuntimeException() {
    override fun toString(): String {
        return errorCode.message
    }
}
