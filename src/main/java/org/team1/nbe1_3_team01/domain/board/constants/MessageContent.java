package org.team1.nbe1_3_team01.domain.board.constants;

import lombok.Getter;

@Getter
public enum MessageContent {

    ADD_BOARD_COMPLETED("을 등록했습니다"),
    UPDATE_BOARD_COMPLETED("을 수정했습니다"),
    DELETE_BOARD_COMPLETED("이 삭제되었습니다."),

    ADD_COMMENT_COMPLETED("댓글을 등록했습니다."),
    DELETE_COMMENT_COMPLETED("댓글이 삭제되었습니다."),

    ADD_CATEGORY_COMPLETED("카테고리를 등록했습니다."),
    DELETE_CATEGORY_COMPLETED("카테고리가 삭제되었습니다.");

    private final String message;


    MessageContent(String message) {
        this.message = message;
    }

    private static String getBoardType(boolean isNotice) {
        if(isNotice) {
            return "공지사항";
        }
        return "게시글";
    }

    public static String getAddMessage(boolean isNotice) {
       return getBoardType(isNotice) + ADD_BOARD_COMPLETED.getMessage();
    }

    public static String getDeleteMessage(boolean isNotice) {
        return getBoardType(isNotice) + DELETE_BOARD_COMPLETED.getMessage();
    }

    public static String getUpdateMessage(boolean isNotice) {
        return getBoardType(isNotice) + UPDATE_BOARD_COMPLETED.getMessage();
    }

}
