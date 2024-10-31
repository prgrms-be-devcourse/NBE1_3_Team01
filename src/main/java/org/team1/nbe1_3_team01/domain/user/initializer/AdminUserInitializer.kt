package org.team1.nbe1_3_team01.domain.user.initializer;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.team1.nbe1_3_team01.domain.user.entity.Role;
import org.team1.nbe1_3_team01.domain.user.entity.User;
import org.team1.nbe1_3_team01.domain.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class AdminUserInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public void run(String... args) throws Exception {
        User user = User.builder()
                .username("admin")
                .password("admin1234")
                .email("admin@naver.com")
                .name("관리자")
                .role(Role.ADMIN)
                .build();
        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }
}
