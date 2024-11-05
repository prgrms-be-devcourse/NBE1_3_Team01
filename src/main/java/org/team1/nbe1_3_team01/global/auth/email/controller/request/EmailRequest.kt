package org.team1.nbe1_3_team01.global.auth.email.controller.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

data class EmailRequest(
    @field:NotBlank
    @field:Email(message = "유효하지 않은 이메일 형식입니다")
    val email: String
)