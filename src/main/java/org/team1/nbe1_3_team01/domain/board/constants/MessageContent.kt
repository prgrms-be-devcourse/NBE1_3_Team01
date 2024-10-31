package org.team1.nbe1_3_team01.domain.board.constants

import lombok.Getter

@Getter
enum class MessageContent(
    val message: String
) {
    ADD_BOARD_COMPLETED("을 등록했습니다"),
    UPDATE_BOARD_COMPLETED("을 수정했습니다"),
    DELETE_BOARD_COMPLETED("이 삭제되었습니다."),

    ADD_COMMENT_COMPLETED("댓글을 등록했습니다."),
    DELETE_COMMENT_COMPLETED("댓글이 삭제되었습니다."),

    ADD_CATEGORY_COMPLETED("카테고리를 등록했습니다."),
    DELETE_CATEGORY_COMPLETED("카테고리가 삭제되었습니다.");


    companion object {
        private fun getBoardType(isNotice: Boolean): String =
            if (isNotice) {
             "공지사항"
            }
            else  "게시글"


        fun getAddMessage(isNotice: Boolean): String =
            getBoardType(isNotice) + ADD_BOARD_COMPLETED.message

        fun getDeleteMessage(isNotice: Boolean): String =
            getBoardType(isNotice) + DELETE_BOARD_COMPLETED.message

        fun getUpdateMessage(isNotice: Boolean): String =
            getBoardType(isNotice) + UPDATE_BOARD_COMPLETED.message

    }
}
