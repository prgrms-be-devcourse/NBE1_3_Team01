package org.team1.nbe1_3_team01.domain.user.service.response;

public record UserDetailsResponse(
        Long id,
        String username,
        String email,
        String name,
        String courseName
) {
}
