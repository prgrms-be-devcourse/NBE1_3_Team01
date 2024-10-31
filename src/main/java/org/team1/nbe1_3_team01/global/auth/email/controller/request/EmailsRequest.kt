package org.team1.nbe1_3_team01.global.auth.email.controller.request

import jakarta.validation.constraints.NotNull

data class EmailsRequest(
         val courseId: @NotNull(message = "코스 아이디는 필수 입력 값입니다") Long,
         val emails: List<String>
)
