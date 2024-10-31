package org.team1.nbe1_3_team01.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.team1.nbe1_3_team01.global.util.ErrorCode;

@RequiredArgsConstructor
@Getter
public class AppException extends RuntimeException {

    private final ErrorCode errorCode;

    @Override
    public String toString() {
        return errorCode.getMessage();
    }
}
