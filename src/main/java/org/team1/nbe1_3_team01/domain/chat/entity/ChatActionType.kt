package org.team1.nbe1_3_team01.domain.chat.entity

enum class ChatActionType(val code: Int) {
    EXIT(0),
    KICK(8),
    ENTER(9),
    SEND_MESSAGE(1),
    SEND_EMOTICON(2);

    companion object {
        fun fromCode(code: Int): ChatActionType {
            for (action in entries) {
                if (action.code == code) {
                    return action
                }
            }
            throw IllegalArgumentException("Invalid code: $code")
        }
    }
}
