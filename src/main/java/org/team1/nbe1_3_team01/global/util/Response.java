package org.team1.nbe1_3_team01.global.util;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public final class Response<T> {
    private String msg;
    private T result;

    public static Response<Void> error(String msg){
        return new Response<>(msg, null);
    }

    public static <T> Response<T> success(T result){
        return new Response<>("Success", result);
    }
}
