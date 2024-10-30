package org.team1.nbe1_3_team01.domain.board.exception;

import org.team1.nbe1_3_team01.global.exception.AppException;
import org.team1.nbe1_3_team01.global.util.ErrorCode;

public class NotFoundBoardException extends AppException {

    public NotFoundBoardException(ErrorCode errorCode) {
        super(errorCode);
    }
}
