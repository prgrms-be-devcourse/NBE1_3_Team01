package org.team1.nbe1_3_team01.domain.attendance.service.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType
import org.team1.nbe1_3_team01.global.validation.DurationRequest
import java.time.LocalDateTime

data class AttendanceCreateRequest @JsonCreator constructor(
    @NotNull(message = "출결 상태를 선택 해야 합니다.")
    @JsonProperty("issueType")
    val issueType: IssueType,

    @NotNull(message = "시작 시간은 필수입니다.")
    @JsonProperty("startAt")
    val startAt: LocalDateTime,

    @NotNull(message = "종료 시간은 필수입니다.")
    @JsonProperty("endAt")
    val endAt: LocalDateTime,

    @NotBlank(message = "출결 요청의 사례를 작성해야 합니다.")
    @JsonProperty("description")
    val description: String
) {
    @Valid
    private val durationRequest: DurationRequest = DurationRequest(startAt, endAt)

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
