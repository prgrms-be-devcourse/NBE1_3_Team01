package org.team1.nbe1_3_team01.domain.attendance.repository;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.team1.nbe1_3_team01.domain.attendance.entity.Attendance;
import org.team1.nbe1_3_team01.domain.attendance.service.port.AttendanceRepository;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryImpl implements AttendanceRepository {

    private final AttendanceJpaRepository attendanceJpaRepository;

    @Override
    public Optional<Attendance> findById(Long id) {
        return attendanceJpaRepository.findById(id);
    }

    @Override
    public List<Attendance> findByUserId(Long userId) {
        return attendanceJpaRepository.findByRegistrant_UserId(userId);
    }

    @Override
    public List<Attendance> findAll() {
        return attendanceJpaRepository.findAll();
    }

    @Override
    public Attendance save(Attendance attendance) {
        return attendanceJpaRepository.save(attendance);
    }

    @Override
    public void deleteById(Long id) {
        attendanceJpaRepository.deleteById(id);
    }
}
