package org.team1.nbe1_3_team01.domain.user.service.response;

import org.team1.nbe1_3_team01.domain.user.entity.Role;

public record UserBriefWithRoleResponse(
        Long id,
        String username,
        String name,
        Role role
) {
}
