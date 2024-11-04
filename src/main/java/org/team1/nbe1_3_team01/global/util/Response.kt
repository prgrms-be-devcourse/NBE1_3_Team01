package org.team1.nbe1_3_team01.global.util

data class Response<T>(
    val msg: String?,
    val result: T?
) {
    companion object {
        fun error(msg: String?): Response<Void> {
            return Response(msg, null)
        }

        fun <T> success(result: T): Response<T> {
            return Response("Success", result)
        }
    }
}
