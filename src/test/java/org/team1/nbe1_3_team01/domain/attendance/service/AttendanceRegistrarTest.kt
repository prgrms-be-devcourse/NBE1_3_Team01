package org.team1.nbe1_3_team01.domain.attendance.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.team1.nbe1_3_team01.domain.attendance.service.dto.AttendanceCreateRequest;
import org.team1.nbe1_3_team01.domain.attendance.entity.IssueType;
import org.team1.nbe1_3_team01.domain.attendance.fake.AttendanceFakeRepository;

@SuppressWarnings("NonAsciiCharacters")
public class AttendanceRegistrarTest {

    private final AttendanceRegistrar attendanceRegistrar;

    public AttendanceRegistrarTest() {
        this.attendanceRegistrar = new AttendanceRegistrar(
                new AttendanceFakeRepository());
    }

    @Test
    void 출결_요청을_성공적으로_저장한다() {
        // given
        Long registrantId = 1L;
        AttendanceCreateRequest attendanceCreateRequest = AttendanceCreateRequest.builder()
                .issueType(IssueType.ABSENT)
                .startAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0, 0)))
                .endAt(LocalDateTime.of(LocalDate.now(), LocalTime.of(16, 0, 0)))
                .description("국취제로 인한 외출입니다.")
                .build();

        // when
        Long attendanceId = attendanceRegistrar.register(registrantId, attendanceCreateRequest);

        // then
        assertThat(attendanceId).isEqualTo(1L);
    }

    /*@Test
    void 출결_요청을_성공적으로_등록한다() {
        var registrantId = 1L;
        var attendanceCreateRequest = create_ATTENDANCE_CREATE_REQUEST_ABSENT();
        // 합의된 내용을 바탕으로 stub 생성. 성공한다는 가정.
        given(attendanceJpaRepository.findByRegistrant_UserId(registrantId)).willReturn(List.of());


        attendanceRegistrar.register(registrantId, attendanceCreateRequest);
    }*/

}
