package org.team1.nbe1_3_team01.domain.user.entity

import lombok.Getter
import lombok.RequiredArgsConstructor

@Getter
@RequiredArgsConstructor
enum class Role(private val key: String? = null) {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");
}
