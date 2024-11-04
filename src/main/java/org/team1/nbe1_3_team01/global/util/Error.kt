package org.team1.nbe1_3_team01.global.util

data class Error(
    val code: Int,
    val message: Any?
) {
    companion object {
        fun of(code: Int, message: Any?): Error {
            return Error(code, message)
        }
    }
}
