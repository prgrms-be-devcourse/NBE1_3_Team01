package org.team1.nbe1_3_team01.global.auth.email.controller.request

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class EmailsRequest(
    @field:NotNull(message = "코스 아이디는 필수 입력 값입니다")
    val courseId: Long,
    
    @field:Valid
    @field:Size(min = 1, message = "최소 하나 이상의 이메일 주소를 입력해야 합니다")
    val emails: List<EmailRequest>
)
