package org.team1.nbe1_3_team01.domain.board.exception

import org.team1.nbe1_3_team01.global.exception.AppException
import org.team1.nbe1_3_team01.global.util.ErrorCode

class NotFoundBoardException(errorCode: ErrorCode) : AppException(errorCode)
