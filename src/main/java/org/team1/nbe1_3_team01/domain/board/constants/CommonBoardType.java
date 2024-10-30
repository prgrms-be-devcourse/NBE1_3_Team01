package org.team1.nbe1_3_team01.domain.board.constants;

import lombok.Getter;
import org.team1.nbe1_3_team01.domain.board.exception.NotFoundTypeException;

import static org.team1.nbe1_3_team01.global.util.ErrorCode.CATEGORY_NOT_FOUND;

@Getter
public enum CommonBoardType {

    NOTICE("y"),
    STUDY("n");

    private final String type;

    CommonBoardType(String type) {
        this.type = type;
    }

    public static CommonBoardType getType(String type) {
        for (CommonBoardType enumType : values()) {
            if(enumType.getType().equals(type)) {
                return enumType;
            }
        }
        throw new NotFoundTypeException(CATEGORY_NOT_FOUND);
    }
}
