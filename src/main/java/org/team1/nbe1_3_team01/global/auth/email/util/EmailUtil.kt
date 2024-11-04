package org.team1.nbe1_3_team01.global.auth.email.util

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class EmailUtil private constructor(){

    companion object {
        const val ENCODING: String = "UTF-8"
        const val SUBJECT: String = "데브코스 회원가입 링크"
        private const val CONTENT = "<p>회원가입을 하려면 <a href=\"%s\">여기</a>를 클릭하세요</p>"
        private const val SIGNUP_URL = "http://localhost:8080/user/sign-up"
        private const val CODE = "?code="
        private const val COURSE_ID = "&courseId="

        @OptIn(ExperimentalUuidApi::class)
        fun createSignupContent(uuid: Uuid, courseId: Long): String {
            return String.format(CONTENT, SIGNUP_URL + CODE + uuid + COURSE_ID + courseId)
        }
    }
}
