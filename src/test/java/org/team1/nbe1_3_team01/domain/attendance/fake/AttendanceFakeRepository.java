package org.team1.nbe1_3_team01.domain.attendance.fake;

import static org.springframework.test.util.ReflectionTestUtils.setField;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository;

public class AttendanceFakeRepository implements AttendanceRepository {

    private int id = 0;
    private final List<Attendance> attendanceStorage = new ArrayList<>();

    @Override
    public Optional<Attendance> findById(Long id) {
        if (id == null || id == 0) {
            return Optional.empty();
        }
        try {
            Attendance attendance = attendanceStorage.get(id.intValue() - 1);
            return Optional.of(attendance);
        } catch (IndexOutOfBoundsException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Attendance> findByUserId(Long userId) {
        return attendanceStorage.stream()
                .filter(attendance -> attendance.getRegistrant().getUserId().equals(userId))
                .toList();
    }

    @Override
    public List<Attendance> findAll() {
        return attendanceStorage.stream()
                .toList();
    }

    @Override
    public Attendance save(Attendance attendance) {
        Long id = attendance.getId();
        // INSERT
        if (id == null || id == 0) {
            setField(attendance, "id", (long)(this.id + 1));
            attendanceStorage.add(this.id++, attendance);
        }
        // UPDATE
        else {
            attendanceStorage.removeIf(a -> a.getId().equals(attendance.getId()));
            attendanceStorage.add((int)(attendance.getId() - 1), attendance);
        }
        return attendance;
    }

    @Override
    public void deleteById(Long id) {
        attendanceStorage.removeIf(a -> a.getId().equals(id));
    }
}
