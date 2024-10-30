package org.team1.nbe1_3_team01.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.team1.nbe1_3_team01.domain.user.entity.Course;
import org.team1.nbe1_3_team01.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByCourse(Course course);
}
