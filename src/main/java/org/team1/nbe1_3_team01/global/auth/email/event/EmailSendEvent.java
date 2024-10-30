package org.team1.nbe1_3_team01.global.auth.email.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class EmailSendEvent {
    private final String email;
    private final Long courseId;
}
