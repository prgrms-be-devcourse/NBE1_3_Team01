package org.team1.nbe1_3_team01.domain.chat.entity;

public enum ChatActionType {
    EXIT(0),
    KICK(8),
    ENTER(9),
    SEND_MESSAGE(1),
    SEND_EMOTICON(2);

    private final int code;

    ChatActionType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static ChatActionType fromCode(int code) {
        for (ChatActionType action : ChatActionType.values()) {
            if (action.getCode() == code) {
                return action;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
