package org.team1.nbe1_3_team01.domain.attendance.service.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType
import org.team1.nbe1_3_team01.global.validation.DurationRequest
import org.team1.nbe1_3_team01.global.validation.PeriodCheck
import java.time.LocalDateTime

@PeriodCheck
data class AttendanceCreateRequest(
    @field:NotNull(message = "출결 상태를 선택 해야 합니다.")
    val issueType: IssueType,

    @field:Valid
    @field:NotNull(message = "기간 정보는 필수입니다.")
    val durationRequest: DurationRequest,

    @field:NotBlank(message = "출결 요청의 사례를 작성해야 합니다.")
    val description: String
) {

    companion object {
        @JsonCreator
        fun fromJson(
            @JsonProperty("issueType") issueType: IssueType,
            @JsonProperty("startAt") startAt: LocalDateTime,
            @JsonProperty("endAt") endAt: LocalDateTime,
            @JsonProperty("description") description: String
        ): AttendanceCreateRequest {
            return AttendanceCreateRequest(
                issueType = issueType,
                durationRequest = DurationRequest(startAt, endAt),
                description = description
            )
        }
    }

    fun toEntity(registrantId: Long): Attendance {
        return Attendance(
            issueType = this.issueType,
            startAt = this.durationRequest.startAt,
            endAt = this.durationRequest.endAt,
            description = this.description,
            registrantId = registrantId
        )
    }
}
