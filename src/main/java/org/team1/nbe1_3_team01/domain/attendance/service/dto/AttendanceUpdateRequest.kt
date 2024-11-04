package org.team1.nbe1_3_team01.domain.attendance.service.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType
import org.team1.nbe1_3_team01.global.validation.DurationRequest
import java.time.LocalDateTime

data class AttendanceUpdateRequest(
    @NotNull(message = "id 값이 null일 수 없습니다")
    @Min(value = 1, message = "id 값은 1 이상이어야 합니다")
    val id: Long,
    @NotNull(message = "출결 상태를 선택 해야 합니다.")
    val issueType: IssueType,
    @field:Valid
    @field:NotNull(message = "기간 정보는 필수입니다.")
    val durationRequest: DurationRequest,
    @NotBlank(message = "출결 요청의 사례를 작성해야 합니다.")
    val description: String
) {

    companion object {
        @JsonCreator
        fun fromJson(
            @JsonProperty("id") id: Long,
            @JsonProperty("issueType") issueType: IssueType,
            @JsonProperty("startAt") startAt: LocalDateTime,
            @JsonProperty("endAt") endAt: LocalDateTime,
            @JsonProperty("description") description: String
        ): AttendanceUpdateRequest {
            return AttendanceUpdateRequest(
                id = id,
                issueType = issueType,
                durationRequest = DurationRequest(startAt, endAt),
                description = description
            )
        }
    }
}

